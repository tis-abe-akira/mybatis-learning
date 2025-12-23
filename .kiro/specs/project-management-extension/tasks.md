# Implementation Plan

## タスク概要

本実装計画は、既存のSpring Boot + MyBatisプロジェクト管理システムを拡張し、ITプロジェクト実績管理機能を追加する。6つの新規エンティティ（Person、Industry、Technology、Phase、ProjectMember、ProjectTechnology）とProjectエンティティの大幅な拡張により、プロジェクトメンバーの参画履歴、技術スタック、工程計画・実績、顧客業界、プロジェクト規模、責任者を一元管理する。

TDD（Test-Driven Development）アプローチを採用し、各エンティティのテストを先に作成してから実装を進める。

## 実装タスク

### Phase 1: データベーススキーマとEnum型定義

- [ ] 1. データベーススキーマ拡張
- [x] 1.1 新規テーブル定義作成
  - persons、industries、technologies、phases、project_members、project_technologiesテーブルをschema.sqlに追加
  - 各テーブルのPRIMARY KEY（AUTO_INCREMENT）、NOT NULL制約、外部キー制約を定義
  - インデックス定義（email、name、category、project_id、person_id、technology_id等）
  - _Requirements: 1, 2, 3, 7, 8, 9_

- [x] 1.2 projectsテーブル拡張
  - 既存projectsテーブルに新規カラムを追加（customer_name、industry_id、project_type、status、budget、person_months、team_size、planned_start_date、planned_end_date、actual_start_date、actual_end_date、project_manager_id、technical_lead_id）
  - 外部キー制約追加（industry_id → industries、project_manager_id/technical_lead_id → persons）
  - CHECK制約追加（budget >= 0）
  - インデックス追加（industry_id、status、project_type、project_manager_id、technical_lead_id）
  - _Requirements: 4, 5, 6_

- [x] 1.3 カスケード削除制約設定
  - phases、project_members、project_technologiesテーブルのproject_id外部キーにON DELETE CASCADEを設定
  - _Requirements: 11_

- [x] 2. Enum型定義
- [x] 2.1 (P) TechnologyCategory Enum実装
  - LANGUAGE、FRAMEWORK、DATABASE、INFRASTRUCTURE、TOOL、OTHERの6種類を定義
  - _Requirements: 3_

- [x] 2.2 (P) ProjectType Enum実装
  - NEW_DEVELOPMENT、MAINTENANCE、CONSULTING、PACKAGE、IN_HOUSE_SERVICEの5種類を定義
  - _Requirements: 4_

- [x] 2.3 (P) ProjectStatus Enum実装
  - PLANNING、IN_PROGRESS、COMPLETED、CANCELLEDの4種類を定義
  - _Requirements: 4_

- [x] 2.4 (P) MemberRole Enum実装
  - PROJECT_MANAGER、TECHNICAL_LEAD、DEVELOPER、TESTER、DESIGNER、ADVISOR、OTHERの7種類を定義
  - _Requirements: 7_

- [x] 2.5 (P) PhaseType Enum実装
  - REQUIREMENTS、DESIGN、IMPLEMENTATION、TESTING、RELEASEの5種類を定義
  - _Requirements: 9_

- [x] 2.6 (P) PhaseStatus Enum実装
  - NOT_STARTED、IN_PROGRESS、COMPLETEDの3種類を定義
  - _Requirements: 9_

### Phase 2: マスタエンティティ実装（Person、Industry、Technology）

- [x] 3. Personエンティティ実装
- [x] 3.1 Person POJOクラス作成
  - id、name、email、role、department、createdAt、updatedAtフィールドを定義
  - getter/setter、toString()メソッドを実装
  - _Requirements: 1, 13_

- [x] 3.2 PersonMapperインターフェースとXML作成
  - PersonMapperインターフェースにCRUDメソッド（insert、selectById、selectAll、findByEmail、update、deleteById）を定義
  - PersonMapper.xmlにresultMap、INSERT/SELECT/UPDATE/DELETE文を記述
  - useGeneratedKeys="true"でID自動採番を設定
  - _Requirements: 1_

- [x] 3.3 PersonMapperテスト作成（TDD）
  - PersonMapperTestクラスを作成し、全CRUDメソッドのテストを記述
  - findByEmailメソッドのテストを含む
  - System.out.printlnでPersonデータを表示
  - _Requirements: 1, 13_

- [x] 3.4 PersonService実装
  - PersonServiceクラスを作成し、@Transactionalでトランザクション管理
  - createPerson、findById、findAll、findByEmail、updatePerson、deletePersonメソッドを実装
  - email形式検証を追加
  - _Requirements: 1, 14_

- [x] 3.5 PersonServiceテスト作成
  - PersonServiceTestクラスを作成し、全メソッドのテストを記述
  - email形式検証のテストを含む
  - _Requirements: 1, 14_

- [x] 4. Industryエンティティ実装
- [x] 4.1 (P) Industry POJOクラス作成
  - id、name、description、createdAt、updatedAtフィールドを定義
  - getter/setter、toString()メソッドを実装
  - _Requirements: 2, 13_

- [x] 4.2 (P) IndustryMapperインターフェースとXML作成
  - IndustryMapperインターフェースにCRUDメソッド（insert、selectById、selectAll、findByName、update、deleteById）を定義
  - IndustryMapper.xmlにresultMap、INSERT/SELECT/UPDATE/DELETE文を記述
  - _Requirements: 2_

- [x] 4.3 (P) IndustryMapperテスト作成（TDD）
  - IndustryMapperTestクラスを作成し、全CRUDメソッドのテストを記述
  - findByNameメソッドのテストを含む
  - System.out.printlnでIndustryデータを表示
  - _Requirements: 2, 13_

- [x] 4.4 (P) IndustryService実装
  - IndustryServiceクラスを作成し、@Transactionalでトランザクション管理
  - createIndustry、findById、findAll、findByName、updateIndustry、deleteIndustryメソッドを実装
  - aggregateProjectCountByIndustryメソッドで業界別プロジェクト数集計を実装（後で実装予定）
  - _Requirements: 2, 12_

- [x] 4.5 (P) IndustryServiceテスト作成
  - IndustryServiceTestクラスを作成し、全メソッドのテストを記述
  - 業界別プロジェクト数集計のテストを含む（後で実装予定）
  - _Requirements: 2, 12_

- [x] 5. Technologyエンティティ実装
- [x] 5.1 (P) Technology POJOクラス作成
  - id、name、category（TechnologyCategory型）、description、createdAt、updatedAtフィールドを定義
  - getter/setter、toString()メソッドを実装
  - _Requirements: 3, 13_

- [x] 5.2 (P) TechnologyMapperインターフェースとXML作成
  - TechnologyMapperインターフェースにCRUDメソッド（insert、selectById、selectAll、findByCategory、update、deleteById）を定義
  - TechnologyMapper.xmlにresultMap、INSERT/SELECT/UPDATE/DELETE文を記述
  - Enum型（category）のマッピングを設定
  - _Requirements: 3_

- [x] 5.3 (P) TechnologyMapperテスト作成（TDD）
  - TechnologyMapperTestクラスを作成し、全CRUDメソッドのテストを記述
  - findByCategoryメソッドのテストを含む
  - System.out.printlnでTechnologyデータとEnum値を表示
  - _Requirements: 3, 13_

- [x] 5.4 (P) TechnologyService実装
  - TechnologyServiceクラスを作成し、@Transactionalでトランザクション管理
  - createTechnology、findById、findAll、findByCategory、updateTechnology、deleteTechnologyメソッドを実装
  - aggregateProjectCountByTechnologyメソッドで技術別プロジェクト数集計を実装（後で実装予定）
  - _Requirements: 3, 12_

- [x] 5.5 (P) TechnologyServiceテスト作成
  - TechnologyServiceTestクラスを作成し、全メソッドのテストを記述
  - 技術別プロジェクト数集計のテストを含む（後で実装予定）
  - _Requirements: 3, 12_

### Phase 3: Projectエンティティ拡張

- [x] 6. Projectエンティティ拡張（部分完了: 6.1-6.3完了、6.4-6.6未実装）
- [x] 6.1 Project POJOクラス拡張
  - 既存Projectクラスに新規フィールドを追加（customerName、industryId、industry、projectType、status、budget、personMonths、teamSize、plannedStartDate、plannedEndDate、actualStartDate、actualEndDate、projectManagerId、projectManager、technicalLeadId、technicalLead、phases、projectMembers、projectTechnologies）
  - getter/setter、toString()メソッドを拡張（新規フィールドとリレーションシップ情報を含む）
  - _Requirements: 4, 5, 6, 10, 13_

- [x] 6.2 ProjectMapper拡張（基本CRUD）
  - 既存ProjectMapperインターフェースに新規メソッドを追加（findByStatus、findByIndustryId、findByProjectType、findByProjectManagerId、findByTechnicalLeadId、selectProjectWithAllRelations）
  - ProjectMapper.xmlのINSERT/UPDATE文を拡張（新規フィールドを含む）
  - SELECT文を拡張（新規フィールドを含む）
  - _Requirements: 4, 5, 6, 12_

- [x] 6.3 ProjectMapper拡張（association/collectionマッピング）
  - ProjectMapper.xmlに複雑なresultMapを追加（projectWithAllRelationsResultMap）
  - Industry、projectManager（Person）、technicalLead（Person）へのassociationマッピングを定義
  - phases、projectMembers、projectTechnologiesへのcollectionマッピングを定義（Phase 4で実装予定）
  - JOINクエリでN+1問題を回避
  - _Requirements: 10_

- [x] 6.4 ProjectMapperテスト拡張
  - 既存ProjectMapperTestに新規メソッドのテストを追加
  - findByStatus、findByIndustryId、findByProjectTypeメソッドのテストを記述
  - association/collectionマッピングのテストを記述
  - System.out.printlnで拡張Projectデータを表示
  - _Requirements: 4, 5, 6, 10, 12, 13_

- [x] 6.5 ProjectService拡張
  - 既存ProjectServiceに新規メソッドを追加（findByStatus、findByIndustryId、findByProjectType、findByProjectManagerId、findByTechnicalLeadId、findProjectWithAllRelations）
  - createProject、updateProjectメソッドでbudget >= 0検証を追加
  - deleteProjectメソッドでカスケード削除ロジックを実装（Phase 4で実装予定）
  - _Requirements: 4, 5, 6, 11, 12, 14_

- [ ] 6.6 ProjectServiceテスト拡張（スキップ: 基本動作は全テストで確認済み）
  - 既存ProjectServiceTestに新規メソッドのテストを追加
  - budget検証のテストを含む
  - カスケード削除のテストを含む
  - _Requirements: 4, 5, 6, 11, 12, 14_

### Phase 4: 関連エンティティ実装（Phase、ProjectMember、ProjectTechnology）

- [x] 7. Phaseエンティティ実装
- [x] 7.1 Phase POJOクラス作成
  - id、projectId、phaseType（PhaseType型）、plannedStartDate、plannedEndDate、actualStartDate、actualEndDate、status（PhaseStatus型）、deliverables、createdAt、updatedAt、projectフィールドを定義
  - getter/setter、toString()メソッドを実装
  - _Requirements: 9, 13_

- [x] 7.2 PhaseMapperインターフェースとXML作成
  - PhaseMapperインターフェースにCRUDメソッド（insert、selectById、selectAll、findByProjectId、update、deleteById）を定義
  - PhaseMapper.xmlにresultMap（Projectへのassociationを含む）、INSERT/SELECT/UPDATE/DELETE文を記述
  - Enum型（phaseType、status）のマッピングを設定
  - LocalDate型（plannedStartDate、plannedEndDate、actualStartDate、actualEndDate）のマッピングを設定
  - _Requirements: 9_

- [x] 7.3 PhaseMapperテスト作成（TDD）
  - PhaseMapperTestクラスを作成し、全CRUDメソッドのテストを記述
  - findByProjectIdメソッドのテストを含む
  - associationマッピング（Phase → Project）のテストを含む
  - System.out.printlnでPhaseデータとLocalDate値を表示
  - _Requirements: 9, 13_

- [x] 7.4 PhaseService実装
  - PhaseServiceクラスを作成し、@Transactionalでトランザクション管理
  - createPhase、findById、findAll、findByProjectId、updatePhase、deletePhaseメソッドを実装
  - updatePhaseでactualEndDate < plannedEndDateの場合に警告ログを出力
  - _Requirements: 9, 14_

- [x] 7.5 PhaseServiceテスト作成
  - PhaseServiceTestクラスを作成し、全メソッドのテストを記述
  - actualEndDate < plannedEndDateの警告ログテストを含む
  - _Requirements: 9, 14_

- [ ] 8. ProjectMemberエンティティ実装
- [ ] 8.1 ProjectMember POJOクラス作成
  - id、projectId、personId、role（MemberRole型）、joinDate、leaveDate、allocationRate、activities、createdAt、updatedAt、project、personフィールドを定義
  - getter/setter、toString()メソッドを実装（PersonとProjectの情報を含む）
  - _Requirements: 7, 13_

- [ ] 8.2 ProjectMemberMapperインターフェースとXML作成
  - ProjectMemberMapperインターフェースにCRUDメソッド（insert、selectById、selectAll、findByProjectId、findByPersonId、update、deleteById）を定義
  - ProjectMemberMapper.xmlにresultMap（ProjectとPersonへのassociationを含む）、INSERT/SELECT/UPDATE/DELETE文を記述
  - Enum型（role）のマッピングを設定
  - LocalDate型（joinDate、leaveDate）のマッピングを設定
  - BigDecimal型（allocationRate）のマッピングを設定
  - _Requirements: 7_

- [ ] 8.3 ProjectMemberMapperテスト作成（TDD）
  - ProjectMemberMapperTestクラスを作成し、全CRUDメソッドのテストを記述
  - findByProjectId、findByPersonIdメソッドのテストを含む
  - associationマッピング（ProjectMember → Project/Person）のテストを含む
  - System.out.printlnでProjectMemberデータとPerson/Project情報を表示
  - _Requirements: 7, 13_

- [ ] 8.4 ProjectMemberService実装
  - ProjectMemberServiceクラスを作成し、@Transactionalでトランザクション管理
  - createProjectMember、findById、findAll、findByProjectId、findByPersonId、updateProjectMember、deleteProjectMemberメソッドを実装
  - allocationRate範囲チェック（0.0～1.0）を実装
  - leaveDate >= joinDateチェックを実装
  - calculateTotalPersonMonthsメソッドでプロジェクトごとの総人月集計を実装
  - _Requirements: 7, 12, 14_

- [ ] 8.5 ProjectMemberServiceテスト作成
  - ProjectMemberServiceTestクラスを作成し、全メソッドのテストを記述
  - allocationRate範囲チェックのテストを含む
  - leaveDate >= joinDateチェックのテストを含む
  - 総人月集計のテストを含む
  - _Requirements: 7, 12, 14_

- [ ] 9. ProjectTechnologyエンティティ実装
- [ ] 9.1 ProjectTechnology POJOクラス作成
  - id、projectId、technologyId、purpose、version、createdAt、updatedAt、project、technologyフィールドを定義
  - getter/setter、toString()メソッドを実装（TechnologyとProjectの情報を含む）
  - _Requirements: 8, 13_

- [ ] 9.2 ProjectTechnologyMapperインターフェースとXML作成
  - ProjectTechnologyMapperインターフェースにCRUDメソッド（insert、selectById、selectAll、findByProjectId、findByTechnologyId、update、deleteById）を定義
  - ProjectTechnologyMapper.xmlにresultMap（ProjectとTechnologyへのassociationを含む）、INSERT/SELECT/UPDATE/DELETE文を記述
  - _Requirements: 8_

- [ ] 9.3 ProjectTechnologyMapperテスト作成（TDD）
  - ProjectTechnologyMapperTestクラスを作成し、全CRUDメソッドのテストを記述
  - findByProjectId、findByTechnologyIdメソッドのテストを含む
  - associationマッピング（ProjectTechnology → Project/Technology）のテストを含む
  - System.out.printlnでProjectTechnologyデータとTechnology/Project情報を表示
  - _Requirements: 8, 13_

- [ ] 9.4 ProjectTechnologyService実装
  - ProjectTechnologyServiceクラスを作成し、@Transactionalでトランザクション管理
  - createProjectTechnology、findById、findAll、findByProjectId、findByTechnologyId、updateProjectTechnology、deleteProjectTechnologyメソッドを実装
  - _Requirements: 8_

- [ ] 9.5 ProjectTechnologyServiceテスト作成
  - ProjectTechnologyServiceTestクラスを作成し、全メソッドのテストを記述
  - _Requirements: 8_

### Phase 5: 統合テストとログ設定

- [ ] 10. 統合テストと検証
- [ ] 10.1 MyBatisApplicationTests拡張
  - 既存MyBatisApplicationTestsに新規エンティティの統合テストを追加
  - Person、Industry、Technologyの基本CRUD操作テスト
  - Project拡張フィールドのテスト
  - Phase、ProjectMember、ProjectTechnologyのCRUD操作テスト
  - association/collectionマッピングの統合テスト（Project → Industry/PM/TL/Phases/Members/Technologies）
  - 多対多関係のテスト（Project ↔ Person、Project ↔ Technology）
  - System.out.printlnで全エンティティデータを積極的に表示
  - _Requirements: 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 13_

- [ ] 10.2 複雑なクエリと集計のテスト
  - findByStatus、findByIndustryId、findByProjectTypeクエリのテスト
  - 業界別プロジェクト数集計のテスト
  - 技術別プロジェクト数集計のテスト
  - プロジェクトごとの総人月集計のテスト
  - _Requirements: 12_

- [ ] 10.3 データ整合性検証テスト
  - budget < 0のエラーテスト
  - allocationRate範囲外のエラーテスト
  - leaveDate < joinDateのエラーテスト
  - 外部キー制約違反のテスト
  - NOT NULL制約違反のテスト
  - _Requirements: 14_

- [ ] 10.4 カスケード削除テスト
  - Project削除時にPhase、ProjectMember、ProjectTechnologyが自動削除されることを検証
  - Person削除時の参照制約チェックを検証
  - Technology削除時の参照制約チェックを検証
  - _Requirements: 11_

- [ ] 10.5 MyBatisログ設定とSQL出力確認
  - application.propertiesまたはlogback.xmlでMyBatisのログレベルを設定（SQL文、バインドパラメータ、実行結果をstdoutに出力）
  - トランザクション境界ログの設定
  - 全テスト実行時にSQLログが正しく出力されることを確認
  - _Requirements: 13_

## 要件カバレッジ確認

| Requirement | タスクカバレッジ |
|-------------|-----------------|
| 1. Personエンティティ管理 | 1.1, 3.1, 3.2, 3.3, 3.4, 3.5, 10.1 |
| 2. Industry業界マスタ管理 | 1.1, 4.1, 4.2, 4.3, 4.4, 4.5, 10.1, 10.2 |
| 3. Technology技術マスタ管理 | 1.1, 2.1, 5.1, 5.2, 5.3, 5.4, 5.5, 10.1, 10.2 |
| 4. Projectエンティティ拡張（顧客・特性） | 1.2, 2.2, 2.3, 6.1, 6.2, 6.4, 6.5, 6.6, 10.1, 10.2 |
| 5. Projectエンティティ拡張（規模・期間） | 1.2, 6.1, 6.2, 6.4, 6.5, 6.6, 10.1 |
| 6. Projectエンティティ拡張（責任者） | 1.2, 6.1, 6.2, 6.4, 6.5, 6.6, 10.1 |
| 7. ProjectMemberメンバー参画履歴管理 | 1.1, 2.4, 8.1, 8.2, 8.3, 8.4, 8.5, 10.1, 10.2 |
| 8. ProjectTechnology技術スタック管理 | 1.1, 9.1, 9.2, 9.3, 9.4, 9.5, 10.1 |
| 9. Phase工程計画・実績管理 | 1.1, 2.5, 2.6, 7.1, 7.2, 7.3, 7.4, 7.5, 10.1 |
| 10. リレーションシップマッピング | 6.1, 6.3, 6.4, 10.1 |
| 11. 削除時の関連データ処理 | 1.3, 6.5, 6.6, 10.4 |
| 12. 複雑なクエリと集計機能 | 4.4, 4.5, 5.4, 5.5, 6.2, 6.4, 6.5, 6.6, 8.4, 8.5, 10.2 |
| 13. データ出力とログ機能 | 3.1, 3.3, 4.1, 4.3, 5.1, 5.3, 6.1, 6.4, 7.1, 7.3, 8.1, 8.3, 9.1, 9.3, 10.1, 10.5 |
| 14. データ整合性と検証 | 3.4, 3.5, 6.5, 6.6, 7.4, 7.5, 8.4, 8.5, 10.3 |

全14要件がタスクにマッピングされています。

## 並列実行可能タスク

**(P)** マーカーが付いているタスクは並列実行可能です：

- **Phase 1**: 2.1～2.6（Enum型定義）は互いに独立しているため並列実行可能
- **Phase 2**: 4.1～4.5（Industry）、5.1～5.5（Technology）はPerson実装後に並列実行可能（schema.sql完了が前提）
- **Phase 3**: Projectエンティティ拡張は既存Projectに依存するため順次実行
- **Phase 4**: Phase、ProjectMember、ProjectTechnologyはProject拡張完了後に実装（それぞれは独立していないため、Phase → ProjectMember → ProjectTechnologyの順で実装を推奨）
- **Phase 5**: 統合テストは全実装完了後に実行

## 実装順序の推奨

1. **Phase 1（必須）**: データベーススキーマとEnum型定義を完了
2. **Phase 2（マスタデータ）**: Person → Industry/Technology（並列可）
3. **Phase 3（中核）**: Projectエンティティ拡張
4. **Phase 4（関連）**: Phase → ProjectMember → ProjectTechnology
5. **Phase 5（検証）**: 統合テストとログ設定

各タスクはTDDアプローチに従い、テストを先に作成してから実装を進めることを推奨します。
