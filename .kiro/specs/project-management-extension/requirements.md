# Requirements Document

## Project Description (Input)
ITプロジェクト実績管理機能の拡張

既存のProject/Organizationエンティティを拡張し、実際のITプロジェクト実績を管理できるようにする。

【新規エンティティ】
1. Person（人）: PMやリーダー、メンバーを表現
2. ProjectMember（プロジェクトメンバー）: 誰がいつどのように支援したかを記録（Project ↔ Person の多対多中間テーブル）
3. Technology（技術）: 技術マスタ（Java, React, AWS等）
4. ProjectTechnology（プロジェクト技術）: プロジェクトで使用した技術スタック（Project ↔ Technology の多対多中間テーブル）
5. Phase（工程）: 主要工程の計画と実績（要件定義、設計、実装、テスト、リリース）
6. Industry（業界）: 顧客業界マスタ（金融、製造、小売、通信等）

【Projectエンティティの拡張項目】
- 顧客情報: customerName（顧客名）、industry（業界への参照）
- プロジェクト特性: projectType（新規開発/保守/コンサル/パッケージ/自社サービス）、status（計画中/進行中/完了/中止）
- 規模: budget（予算金額）、personMonths（人月）、teamSize（チームサイズ）
- 期間: plannedStartDate/plannedEndDate（計画）、actualStartDate/actualEndDate（実績）
- 責任者: projectManager（PM）、technicalLead（技術リード）← Person への参照

【学習できるMyBatisパターン】
- association: Project → Person, Project → Industry
- collection: Project → Phase, Project → ProjectMember, Project → ProjectTechnology
- 多対多（中間テーブル付き）: Project ↔ Person, Project ↔ Technology
- Enum型マッピング: ProjectType, ProjectStatus, MemberRole, PhaseType, TechnologyCategory
- 日付型: LocalDate（計画・実績日付）
- 複雑なクエリ: プロジェクトごとの人月集計、技術別プロジェクト数、業界別分析等

【管理したいポイント】
1. プロジェクト支援メンバーの行動履歴（誰がいつ参画し、どのような役割で、どれくらいの稼働率で活動したか）
2. プロジェクト計画概要（主要工程の計画開始終了日と実績開始終了日のざっくりした管理）
3. プロジェクト特性（顧客、業界、開発種別、サービス形態）
4. 技術スタック（使用技術とそのバージョン、用途）
5. プロジェクト規模（予算、人月、チームサイズ）
6. 責任者（PM、技術リード）

実装は既存のMaven + Spring Boot + MyBatis + H2環境を継続使用し、SpringTest（JUnit5）でテストし、System.out.printlnでデータを積極的に表示する。

## Requirements
<!-- Will be generated in /kiro:spec-requirements phase -->
