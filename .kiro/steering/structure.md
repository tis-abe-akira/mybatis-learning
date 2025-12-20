# Project Structure

## Organization Philosophy

学習プロジェクトとして、機能別・レイヤー別の明確な構造を採用。MyBatisの各機能を独立したサンプルとして実装し、段階的に学習できる構成。

## Directory Patterns

### ソースコードルート
**Location**: `/src/main/java/`
**Purpose**: Javaソースファイルの配置場所
**Example**: エンティティクラス、マッパーインターフェース、サービスクラス

### リソースルート
**Location**: `/src/main/resources/`
**Purpose**: 設定ファイル、XMLマッパー、SQLスクリプトの配置場所
**Example**: `mybatis-config.xml`, マッパーXMLファイル

### テストコードルート
**Location**: `/src/test/java/`
**Purpose**: テストケースとサンプル実行クラス
**Example**: 各機能の動作確認用クラス

## Naming Conventions

- **Files**: PascalCaseで機能を表現 (例: `UserMapper.java`, `BlogService.java`)
- **Mapper Interfaces**: `*Mapper`サフィックス
- **Mapper XML**: 対応するインターフェースと同名 (例: `UserMapper.xml`)
- **Entity Classes**: ドメインオブジェクト名をそのまま使用 (例: `User.java`, `Blog.java`)

## Configuration Organization

### MyBatis設定ファイル
**Location**: `src/main/resources/mybatis-config.xml`
**Purpose**: MyBatisの中央設定ファイル
**Contains**: データソース設定、マッパー登録、typeAlias設定

### SQLマッパーファイル
**Location**: `src/main/resources/mappers/`または対応するパッケージ配下
**Purpose**: SQLステートメントの定義
**Naming**: `<Entity>Mapper.xml`

## Code Organization Principles

- **機能単位の分離**: 各学習トピック(CRUD、動的SQL等)ごとに独立したサンプルを作成
- **マッパー層の明確化**: インターフェース(Java) + XMLマッパーの対応関係を明確に
- **設定の中央管理**: `mybatis-config.xml`で共通設定を一元管理
- **サンプルの独立性**: 各サンプルが独立して動作し、個別に学習できる構成

---
_学習の進行に応じて構造が拡張される前提。パターンの一貫性を重視_
