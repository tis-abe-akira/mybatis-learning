#!/usr/bin/env python3
"""
Generate package-info.java files for Java packages.
"""

import os
import re
import sys
from pathlib import Path
from typing import List, Set, Dict, Tuple


def find_java_packages(root_dir: str, base_package: str = None) -> List[Path]:
    """
    Find all Java package directories (containing .java files).

    Args:
        root_dir: Root directory to search from
        base_package: Optional base package filter (e.g., 'com.example')

    Returns:
        List of package directories
    """
    packages = []
    root_path = Path(root_dir).resolve()

    for dirpath, dirnames, filenames in os.walk(root_path):
        # Skip hidden directories and common non-source directories
        dirnames[:] = [d for d in dirnames if not d.startswith('.') and d not in ['target', 'build', 'out']]

        # Check if this directory contains Java files
        has_java_files = any(f.endswith('.java') and f != 'package-info.java'
                            for f in filenames)

        if has_java_files:
            dir_path = Path(dirpath)

            # If base_package is specified, filter by package path
            if base_package:
                package_path = base_package.replace('.', os.sep)
                if package_path not in str(dir_path):
                    continue

            packages.append(dir_path)

    return packages


def get_package_name(package_dir: Path, src_root: str = 'src/main/java') -> str:
    """
    Extract package name from directory path.

    Args:
        package_dir: Package directory path
        src_root: Source root directory (default: 'src/main/java')

    Returns:
        Package name (e.g., 'com.example.myapp')
    """
    package_dir_str = str(package_dir)

    # Find src_root in path
    if src_root in package_dir_str:
        relative_path = package_dir_str.split(src_root + os.sep)[1]
        package_name = relative_path.replace(os.sep, '.')
        return package_name

    # Fallback: use last parts of path
    parts = package_dir.parts
    if len(parts) >= 3:
        return '.'.join(parts[-3:])
    return '.'.join(parts)


def analyze_java_file(file_path: Path) -> Dict[str, any]:
    """
    Analyze a Java file to extract class information.

    Args:
        file_path: Path to Java file

    Returns:
        Dictionary with class info (name, type, javadoc)
    """
    try:
        with open(file_path, 'r', encoding='utf-8') as f:
            content = f.read()
    except Exception:
        return {}

    info = {
        'name': file_path.stem,
        'type': None,
        'javadoc': None
    }

    # Extract class type (class, interface, enum, record)
    class_match = re.search(r'public\s+(class|interface|enum|record|@interface)\s+(\w+)', content)
    if class_match:
        info['type'] = class_match.group(1)
        info['name'] = class_match.group(2)

    # Extract class-level Javadoc
    javadoc_match = re.search(r'/\*\*\s*\n(.*?)\*/', content, re.DOTALL)
    if javadoc_match:
        javadoc = javadoc_match.group(1)
        # Extract first line of description
        lines = [line.strip().lstrip('*').strip() for line in javadoc.split('\n')]
        lines = [line for line in lines if line and not line.startswith('@')]
        if lines:
            info['javadoc'] = lines[0]

    return info


def analyze_package_contents(package_dir: Path) -> Tuple[List[Dict], str]:
    """
    Analyze all Java files in a package directory.

    Args:
        package_dir: Package directory path

    Returns:
        Tuple of (list of class info dicts, package category)
    """
    java_files = [f for f in package_dir.iterdir()
                  if f.is_file() and f.suffix == '.java' and f.name != 'package-info.java']

    classes_info = []
    for java_file in java_files:
        info = analyze_java_file(java_file)
        if info:
            classes_info.append(info)

    # Determine package category based on name and contents
    package_name = package_dir.name.lower()

    # Check for common patterns
    if package_name in ['entity', 'entities', 'model', 'models', 'domain']:
        category = 'entity'
    elif package_name in ['mapper', 'mappers', 'dao', 'repository', 'repositories']:
        category = 'mapper'
    elif package_name in ['service', 'services']:
        category = 'service'
    elif package_name in ['controller', 'controllers', 'rest', 'api']:
        category = 'controller'
    elif package_name in ['util', 'utils', 'utility', 'utilities', 'helper', 'helpers']:
        category = 'util'
    elif package_name in ['config', 'configuration', 'settings']:
        category = 'config'
    elif package_name in ['dto', 'dtos', 'vo', 'vos']:
        category = 'dto'
    elif package_name in ['exception', 'exceptions', 'error', 'errors']:
        category = 'exception'
    else:
        category = 'general'

    return classes_info, category


def generate_package_description(package_name: str, classes_info: List[Dict], category: str) -> str:
    """
    Generate intelligent package description based on analysis.

    Args:
        package_name: Full package name
        classes_info: List of class information dicts
        category: Package category

    Returns:
        Package description text
    """
    package_parts = package_name.split('.')
    last_part = package_parts[-1] if package_parts else package_name

    # Category-specific descriptions
    category_descriptions = {
        'entity': 'データモデルとエンティティクラス',
        'mapper': 'データアクセス層のマッパーインターフェース',
        'service': 'ビジネスロジックを提供するサービスクラス',
        'controller': 'HTTPリクエストを処理するコントローラ',
        'util': 'ユーティリティクラスと補助機能',
        'config': 'アプリケーション設定クラス',
        'dto': 'データ転送オブジェクト (DTO)',
        'exception': '例外クラスとエラーハンドリング'
    }

    base_description = category_descriptions.get(category, f'{last_part.title()}パッケージ')

    # Add class count information
    if classes_info:
        class_count = len(classes_info)
        class_types = {}
        for info in classes_info:
            cls_type = info.get('type', 'class')
            class_types[cls_type] = class_types.get(cls_type, 0) + 1

        type_info = []
        if class_types.get('interface'):
            type_info.append(f"{class_types['interface']}個のインターフェース")
        if class_types.get('class'):
            type_info.append(f"{class_types['class']}個のクラス")
        if class_types.get('enum'):
            type_info.append(f"{class_types['enum']}個の列挙型")

        if type_info:
            base_description += f"を提供します（{', '.join(type_info)}）"

    return base_description


def generate_package_info_content(package_name: str, package_dir: Path = None) -> str:
    """
    Generate content for package-info.java file.

    Args:
        package_name: Full package name
        package_dir: Package directory path (for analysis)

    Returns:
        File content with Javadoc comment
    """
    # Analyze package contents if directory provided
    if package_dir and package_dir.exists():
        classes_info, category = analyze_package_contents(package_dir)
        description = generate_package_description(package_name, classes_info, category)

        # Generate detailed content
        content_lines = [
            "/**",
            f" * {description}",
            " *"
        ]

        # Add class list if there are classes
        if classes_info:
            content_lines.append(" * <p>主なクラス:</p>")
            content_lines.append(" * <ul>")
            for info in sorted(classes_info, key=lambda x: x['name'])[:10]:  # Limit to 10
                class_desc = info.get('javadoc', '')
                if class_desc:
                    content_lines.append(f" *   <li>{info['name']}: {class_desc}</li>")
                else:
                    content_lines.append(f" *   <li>{info['name']}</li>")
            if len(classes_info) > 10:
                content_lines.append(f" *   <li>... 他 {len(classes_info) - 10} クラス</li>")
            content_lines.append(" * </ul>")

        content_lines.append(" */")
        content_lines.append(f"package {package_name};")

        content = '\n'.join(content_lines) + '\n'
    else:
        # Fallback to simple generation
        package_parts = package_name.split('.')
        last_part = package_parts[-1] if package_parts else package_name
        description = last_part.replace('_', ' ').title()

        content = f"""/**
 * {description}パッケージ
 *
 * <p>このパッケージは{description}に関連する機能を提供します。</p>
 */
package {package_name};
"""

    return content


def create_package_info(package_dir: Path, src_root: str = 'src/main/java',
                       force: bool = False, dry_run: bool = False) -> bool:
    """
    Create package-info.java file for a package directory.

    Args:
        package_dir: Package directory path
        src_root: Source root directory
        force: If True, overwrite existing package-info.java
        dry_run: If True, only show what would be created

    Returns:
        True if file was created (or would be created in dry-run), False otherwise
    """
    package_info_path = package_dir / 'package-info.java'

    # Check if package-info.java already exists
    if package_info_path.exists() and not force:
        return False

    package_name = get_package_name(package_dir, src_root)
    content = generate_package_info_content(package_name, package_dir)

    if dry_run:
        print(f"Would create: {package_info_path}")
        print(f"Package: {package_name}")
        return True

    # Create the file
    with open(package_info_path, 'w', encoding='utf-8') as f:
        f.write(content)

    return True


def main():
    """Main function."""
    import argparse

    parser = argparse.ArgumentParser(
        description='Generate package-info.java files for Java packages'
    )
    parser.add_argument(
        'directory',
        help='Root directory to search for Java packages'
    )
    parser.add_argument(
        '--base-package',
        help='Base package to filter (e.g., com.example.myapp)'
    )
    parser.add_argument(
        '--src-root',
        default='src/main/java',
        help='Source root directory (default: src/main/java)'
    )
    parser.add_argument(
        '--force',
        action='store_true',
        help='Overwrite existing package-info.java files'
    )
    parser.add_argument(
        '--dry-run',
        action='store_true',
        help='Show what would be created without creating files'
    )

    args = parser.parse_args()

    # Find all Java packages
    packages = find_java_packages(args.directory, args.base_package)

    if not packages:
        print("No Java packages found.")
        return 0

    print(f"Found {len(packages)} Java package(s)")
    print()

    # Generate package-info.java for each package
    created_count = 0
    skipped_count = 0

    for package_dir in sorted(packages):
        if create_package_info(package_dir, args.src_root, args.force, args.dry_run):
            created_count += 1
            package_name = get_package_name(package_dir, args.src_root)
            status = "Would create" if args.dry_run else "Created"
            print(f"✅ {status}: {package_name}")
        else:
            skipped_count += 1
            package_name = get_package_name(package_dir, args.src_root)
            print(f"⏭️  Skipped: {package_name} (already exists)")

    print()
    print(f"Summary:")
    print(f"  Created: {created_count}")
    print(f"  Skipped: {skipped_count}")
    print(f"  Total: {len(packages)}")

    return 0


if __name__ == '__main__':
    sys.exit(main())
