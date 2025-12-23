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

## Introduction
本仕様は、既存のSpring Boot + MyBatisプロジェクト管理システムを拡張し、実際のITプロジェクト実績を包括的に管理できる機能を追加する。Person、Industry、Technology、Phase、ProjectMember、ProjectTechnologyの6つの新規エンティティと、Projectエンティティの大幅な拡張により、プロジェクトメンバーの参画履歴、技術スタック、工程計画・実績、顧客業界、プロジェクト規模、責任者を一元管理する。これにより、association（多対一）、collection（一対多）、多対多（中間テーブル付き）、Enum型、日付型など、MyBatisの高度なマッピングパターンを実践的に学習できる環境を提供する。

## Requirements

### Requirement 1: Personエンティティ管理
**Objective:** 開発者として、PMやリーダー、メンバーを表現するPersonエンティティを管理したい。これにより、プロジェクトの責任者やメンバーを一元管理できる。

#### Acceptance Criteria
1. The システムは、Personエンティティ（id、name、email、role、department、createdAt、updatedAt）をPOJOクラスとして定義すること
2. When Personを新規作成する場合、the システムはname、email、role、departmentを必須項目として保存すること
3. When PersonのMapperがfindByEmailメソッドを実行する場合、the システムは指定されたemailアドレスに一致するPersonを返すこと
4. When PersonのServiceが全Person一覧を取得する場合、the システムはデータベースに登録されているすべてのPersonを返すこと
5. The システムは、PersonのCRUD操作（作成、読み取り、更新、削除）をサポートすること

### Requirement 2: Industry業界マスタ管理
**Objective:** 開発者として、顧客業界を管理するIndustryエンティティを定義したい。これにより、プロジェクトの顧客業界を分類・分析できる。

#### Acceptance Criteria
1. The システムは、Industryエンティティ（id、name、description、createdAt、updatedAt）をPOJOクラスとして定義すること
2. When Industryを新規作成する場合、the システムはname（業界名: 金融、製造、小売、通信等）を必須項目として保存すること
3. When IndustryのMapperがfindByNameメソッドを実行する場合、the システムは指定された業界名に一致するIndustryを返すこと
4. The システムは、IndustryのCRUD操作をサポートすること
5. The システムは、複数のProjectが同一のIndustryを参照できること

### Requirement 3: Technology技術マスタ管理
**Objective:** 開発者として、技術スタックを管理するTechnologyエンティティを定義したい。これにより、プロジェクトで使用された技術を体系的に管理・分析できる。

#### Acceptance Criteria
1. The システムは、Technologyエンティティ（id、name、category、description、createdAt、updatedAt）をPOJOクラスとして定義すること
2. The システムは、TechnologyCategoryをEnum型（LANGUAGE、FRAMEWORK、DATABASE、INFRASTRUCTURE、TOOL、OTHER）で定義すること
3. When Technologyを新規作成する場合、the システムはname（Java、React、AWS等）とcategoryを必須項目として保存すること
4. When TechnologyのMapperがfindByCategoryメソッドを実行する場合、the システムは指定されたカテゴリに一致するすべてのTechnologyを返すこと
5. The システムは、TechnologyのCRUD操作をサポートすること

### Requirement 4: Projectエンティティの拡張 - 顧客情報と特性
**Objective:** 開発者として、既存のProjectエンティティに顧客情報とプロジェクト特性を追加したい。これにより、プロジェクトの背景と特徴を把握できる。

#### Acceptance Criteria
1. The システムは、ProjectエンティティにcustomerName（顧客名）フィールドを追加すること
2. The システムは、ProjectエンティティにIndustryへのassociationマッピング（industry）を追加すること
3. The システムは、ProjectTypeをEnum型（NEW_DEVELOPMENT、MAINTENANCE、CONSULTING、PACKAGE、IN_HOUSE_SERVICE）で定義すること
4. The システムは、ProjectStatusをEnum型（PLANNING、IN_PROGRESS、COMPLETED、CANCELLED）で定義すること
5. The システムは、ProjectエンティティにprojectType（プロジェクト種別）とstatus（ステータス）フィールドを追加すること
6. When Projectを作成する場合、the システムはcustomerName、projectType、statusを必須項目として保存すること
7. When Projectが特定のIndustryに紐づく場合、the システムはIndustryへの参照を保存すること

### Requirement 5: Projectエンティティの拡張 - 規模と期間
**Objective:** 開発者として、Projectエンティティにプロジェクト規模と期間情報を追加したい。これにより、プロジェクトの工数とスケジュールを管理できる。

#### Acceptance Criteria
1. The システムは、Projectエンティティにbudget（予算金額、BigDecimal型）フィールドを追加すること
2. The システムは、ProjectエンティティにpersonMonths（人月、BigDecimal型）フィールドを追加すること
3. The システムは、ProjectエンティティにteamSize（チームサイズ、Integer型）フィールドを追加すること
4. The システムは、Projectエンティティに計画期間（plannedStartDate、plannedEndDate、LocalDate型）を追加すること
5. The システムは、Projectエンティティに実績期間（actualStartDate、actualEndDate、LocalDate型）を追加すること
6. When Projectを作成する場合、the システムは計画期間（plannedStartDate、plannedEndDate）を必須項目として保存すること
7. When Projectの実績期間を記録する場合、the システムはactualStartDateとactualEndDateを更新できること

### Requirement 6: Projectエンティティの拡張 - 責任者
**Objective:** 開発者として、Projectエンティティに責任者情報を追加したい。これにより、プロジェクトのPMと技術リードを明確に管理できる。

#### Acceptance Criteria
1. The システムは、ProjectエンティティにprojectManager（PM）へのassociationマッピングを追加すること
2. The システムは、ProjectエンティティにtechnicalLead（技術リード）へのassociationマッピングを追加すること
3. When Projectを作成する場合、the システムはprojectManagerを必須項目として保存すること
4. When ProjectのMapperがfindByProjectManagerIdメソッドを実行する場合、the システムは指定されたPMが担当するすべてのProjectを返すこと
5. When ProjectのMapperがfindByTechnicalLeadIdメソッドを実行する場合、the システムは指定された技術リードが担当するすべてのProjectを返すこと

### Requirement 7: ProjectMemberによるメンバー参画履歴管理
**Objective:** 開発者として、プロジェクトメンバーの参画履歴を管理するProjectMemberエンティティを定義したい。これにより、誰がいつどのような役割でどれくらいの稼働率で活動したかを記録できる。

#### Acceptance Criteria
1. The システムは、ProjectMemberエンティティ（id、projectId、personId、role、joinDate、leaveDate、allocationRate、activities、createdAt、updatedAt）をPOJOクラスとして定義すること
2. The システムは、MemberRoleをEnum型（PROJECT_MANAGER、TECHNICAL_LEAD、DEVELOPER、TESTER、DESIGNER、ADVISOR、OTHER）で定義すること
3. The システムは、ProjectMemberからProjectへのassociationマッピングとPersonへのassociationマッピングを定義すること
4. When ProjectMemberを新規登録する場合、the システムはprojectId、personId、role、joinDate、allocationRate（稼働率）を必須項目として保存すること
5. When ProjectMemberのMapperがfindByProjectIdメソッドを実行する場合、the システムは指定されたProjectに参画しているすべてのメンバーを返すこと
6. When ProjectMemberのMapperがfindByPersonIdメソッドを実行する場合、the システムは指定されたPersonが参画したすべてのプロジェクトメンバー情報を返すこと
7. When ProjectMemberのleaveDate（参画終了日）が設定される場合、the システムはメンバーの参画が終了したことを記録すること
8. The システムは、allocationRateを0.0～1.0の範囲（0%～100%）で管理すること

### Requirement 8: ProjectTechnologyによる技術スタック管理
**Objective:** 開発者として、プロジェクトで使用した技術スタックを管理するProjectTechnologyエンティティを定義したい。これにより、どの技術をどのような用途でどのバージョンで使用したかを記録できる。

#### Acceptance Criteria
1. The システムは、ProjectTechnologyエンティティ（id、projectId、technologyId、purpose、version、createdAt、updatedAt）をPOJOクラスとして定義すること
2. The システムは、ProjectTechnologyからProjectへのassociationマッピングとTechnologyへのassociationマッピングを定義すること
3. When ProjectTechnologyを新規登録する場合、the システムはprojectId、technologyId、purposeを必須項目として保存すること
4. When ProjectTechnologyのMapperがfindByProjectIdメソッドを実行する場合、the システムは指定されたProjectで使用されているすべての技術を返すこと
5. When ProjectTechnologyのMapperがfindByTechnologyIdメソッドを実行する場合、the システムは指定されたTechnologyが使用されているすべてのプロジェクトを返すこと
6. When ProjectTechnologyにversion（バージョン情報: Java 17、React 18等）を記録する場合、the システムはversion文字列を保存できること

### Requirement 9: Phaseによる工程計画・実績管理
**Objective:** 開発者として、主要工程の計画と実績を管理するPhaseエンティティを定義したい。これにより、プロジェクトの各工程の計画開始終了日と実績開始終了日をざっくり管理できる。

#### Acceptance Criteria
1. The システムは、Phaseエンティティ（id、projectId、phaseType、plannedStartDate、plannedEndDate、actualStartDate、actualEndDate、status、deliverables、createdAt、updatedAt）をPOJOクラスとして定義すること
2. The システムは、PhaseTypeをEnum型（REQUIREMENTS、DESIGN、IMPLEMENTATION、TESTING、RELEASE）で定義すること
3. The システムは、PhaseStatusをEnum型（NOT_STARTED、IN_PROGRESS、COMPLETED）で定義すること
4. The システムは、PhaseからProjectへのassociationマッピングを定義すること
5. When Phaseを新規作成する場合、the システムはprojectId、phaseType、plannedStartDate、plannedEndDateを必須項目として保存すること
6. When PhaseのMapperがfindByProjectIdメソッドを実行する場合、the システムは指定されたProjectに紐づくすべてのPhaseを返すこと
7. When Phaseのstatusが更新される場合、the システムは工程の進行状況（未着手、進行中、完了）を記録すること
8. When Phaseの実績期間を記録する場合、the システムはactualStartDateとactualEndDateを更新できること
9. The システムは、Phaseのdeliverables（成果物情報）をテキストとして保存できること

### Requirement 10: リレーションシップマッピング
**Objective:** 開発者として、エンティティ間のリレーションシップを適切にマッピングしたい。これにより、関連データを効率的に取得できる。

#### Acceptance Criteria
1. When ProjectからIndustry情報を取得する場合、the システムはassociationマッピングでIndustryデータを含むProjectを返すこと
2. When ProjectからprojectManagerまたはtechnicalLeadを取得する場合、the システムはassociationマッピングでPersonデータを含むProjectを返すこと
3. When ProjectからPhaseリストを取得する場合、the システムはcollectionマッピングで関連するすべてのPhaseを返すこと
4. When ProjectからProjectMemberリストを取得する場合、the システムはcollectionマッピングで関連するすべてのProjectMemberを返すこと
5. When ProjectからProjectTechnologyリストを取得する場合、the システムはcollectionマッピングで関連するすべてのProjectTechnologyを返すこと
6. When ProjectMemberからPersonとProjectの両方を取得する場合、the システムはassociationマッピングで関連データを返すこと
7. When ProjectTechnologyからTechnologyとProjectの両方を取得する場合、the システムはassociationマッピングで関連データを返すこと

### Requirement 11: 削除時の関連データ処理
**Objective:** 開発者として、エンティティ削除時の関連データ処理を適切に実装したい。これにより、データの整合性を保つことができる。

#### Acceptance Criteria
1. When Projectが削除される場合、the システムは関連するPhaseを自動削除すること（DELETE文またはON CASCADE制約）
2. When Projectが削除される場合、the システムは関連するProjectMemberを自動削除すること（DELETE文またはON CASCADE制約）
3. When Projectが削除される場合、the システムは関連するProjectTechnologyを自動削除すること（DELETE文またはON CASCADE制約）
4. When PersonまたはTechnologyが削除される場合、the システムは参照制約違反をチェックすること
5. If 削除対象のPersonがProjectのprojectManagerまたはtechnicalLeadとして参照されている場合、the システムは削除を拒否するかNULLを設定すること

### Requirement 12: 複雑なクエリと集計機能
**Objective:** 開発者として、プロジェクト実績を分析するための複雑なクエリと集計機能を実装したい。これにより、プロジェクトごとの人月集計、技術別プロジェクト数、業界別分析等を実行できる。

#### Acceptance Criteria
1. When ProjectのServiceがプロジェクトごとの総人月を計算する場合、the システムはProjectMemberのallocationRateと参画期間から総人月を集計すること
2. When TechnologyのServiceが技術別プロジェクト数を集計する場合、the システムはProjectTechnologyから各技術が使用されているプロジェクト数を返すこと
3. When IndustryのServiceが業界別プロジェクト数を集計する場合、the システムは各業界に紐づくプロジェクト数を返すこと
4. When ProjectのMapperが特定ステータスのプロジェクトを検索する場合、the システムはfindByStatusメソッドで該当するProjectリストを返すこと
5. When ProjectのMapperが特定業界のプロジェクトを検索する場合、the システムはfindByIndustryIdメソッドで該当するProjectリストを返すこと
6. When ProjectのMapperが特定プロジェクト種別のプロジェクトを検索する場合、the システムはfindByProjectTypeメソッドで該当するProjectリストを返すこと

### Requirement 13: データ出力とログ機能
**Objective:** 開発者として、エンティティデータとSQL実行ログを詳細に出力したい。これにより、MyBatisの内部動作とデータ状態を理解できる。

#### Acceptance Criteria
1. The システムは、すべてのエンティティにtoString()メソッドを実装し、全フィールド値とリレーションシップ情報を含むこと
2. When SpringBootTestで統合テストを実行する場合、the システムはSystem.out.printlnでエンティティデータを積極的に表示すること
3. When エンティティを作成・更新・削除する場合、the システムは実行されたSQL文（INSERT、UPDATE、DELETE）をログに出力すること
4. When リレーションシップを伴うクエリを実行する場合、the システムはJOIN文またはN+1クエリを含むSQL文をログに出力すること
5. The システムは、トランザクション境界（開始、コミット、ロールバック）をログに記録すること
6. The システムは、バインドパラメータの値をログに出力できること
7. When テスト実行時にPhaseの計画・実績日付を表示する場合、the システムは計画日と実績日の差分情報も出力すること
8. When テスト実行時にProjectMemberを表示する場合、the システムはメンバーのPerson情報（名前、役職、部署）とプロジェクト情報を含めて出力すること

### Requirement 14: データ整合性と検証
**Objective:** 開発者として、エンティティのデータ整合性を保証する検証機能を実装したい。これにより、不正なデータの登録を防止できる。

#### Acceptance Criteria
1. When Projectのbudgetが負の値で登録される場合、the システムは検証エラーをスローすること
2. When ProjectMemberのallocationRateが0.0未満または1.0超過で登録される場合、the システムは検証エラーをスローすること
3. When Phaseのactual期間が計画期間より前の日付で登録される場合、the システムは警告ログを出力すること（エラーではない）
4. When ProjectMemberのleaveDateがjoinDateより前の日付で登録される場合、the システムは検証エラーをスローすること
5. If Projectに存在しないIndustryが参照される場合、the システムは外部キー制約違反エラーをスローすること
6. If Projectに存在しないPersonがprojectManagerとして参照される場合、the システムは外部キー制約違反エラーをスローすること
7. The システムは、必須フィールドが未設定の場合、データベース制約違反エラーをスローすること
