# Implementation Plan

## Task Overview
本実装計画は、Spring Boot + MyBatis + H2データベースを使用したProject/Organization管理システムの基本的なCRUD機能を実装します。XMLマッパーとアノテーションマッパーの両方のアプローチでデータベース操作を実装し、MyBatisの学習環境を構築します。

## Implementation Tasks

- [ ] 1. プロジェクト依存関係とMyBatis設定の構成
- [x] 1.1 Maven依存関係の追加
  - pom.xmlにSpring Boot Starter（spring-boot-starter-web、spring-boot-starter-test）を追加
  - MyBatis Spring Boot Starter 3.0.5を追加
  - H2 Database 2.2.224を追加
  - JUnit 5とSpring Boot Test依存関係を確認
  - _Requirements: 1.1, 1.2, 1.3, 1.4, 1.5_

- [x] 1.2 MyBatisとH2データベースの設定
  - application.ymlまたはapplication.propertiesを作成
  - H2データベース接続設定（jdbc:h2:mem:testdb）を追加
  - H2コンソールを有効化（spring.h2.console.enabled: true）
  - MyBatisマッパー位置を指定（mybatis.mapper-locations: classpath:mappers/*.xml）
  - スネークケース⇔キャメルケース自動変換を有効化（mybatis.configuration.map-underscore-to-camel-case: true）
  - SQLログ出力を有効化（mybatis.configuration.log-impl: org.apache.ibatis.logging.stdout.StdOutImpl）
  - スキーマ初期化モードを設定（spring.sql.init.mode: always）
  - _Requirements: 1.7, 8.1, 10.3, 10.4_

- [x] 1.3 パッケージ構成の作成
  - src/main/javaにベースパッケージ（例：com.example.mybatislearning）を作成
  - entity、mapper、serviceパッケージを作成
  - src/main/resourcesにmappersディレクトリを作成
  - src/test/javaにテストパッケージを作成
  - _Requirements: 1.6_

- [ ] 2. データベーススキーマとエンティティクラスの作成
- [x] 2.1 データベーススキーマ定義
  - schema.sqlを作成してorganizationsテーブルを定義（id、name、description）
  - projectsテーブルを定義（id、project_name、organization_id、外部キー制約）
  - data.sqlを作成してテストデータを投入（Organization 2件、Project 3件）
  - _Requirements: 3.1, 3.2, 3.3, 3.4, 3.5, 3.6_

- [x] 2.2 (P) Organizationエンティティクラスの作成
  - OrganizationクラスをPOJOとして定義
  - id（Long型）、name（String型）、description（String型）フィールドを追加
  - デフォルトコンストラクタとフィールド初期化コンストラクタを作成
  - getter/setterメソッドを実装
  - toString()メソッドを追加してデータ出力を容易化
  - _Requirements: 2.1, 2.2, 2.5, 2.6_

- [x] 2.3 (P) Projectエンティティクラスの作成
  - ProjectクラスをPOJOとして定義
  - id（Long型）、projectName（String型）、organizationId（Long型）フィールドを追加
  - Organizationオブジェクトを保持するフィールドを追加（リレーションシップ用）
  - デフォルトコンストラクタとフィールド初期化コンストラクタを作成
  - getter/setterメソッドを実装
  - toString()メソッドでリレーションシップ情報も出力
  - _Requirements: 2.3, 2.4, 2.5, 2.6, 7.1_

- [ ] 3. XMLマッパーによるCRUD操作の実装
- [x] 3.1 OrganizationMapper（XML）の実装
  - OrganizationMapperインターフェースを作成し、@Mapperアノテーションを付与
  - insert、selectById、selectAll、update、deleteByIdメソッドを定義
  - OrganizationMapper.xmlを作成し、namespaceを設定
  - resultMapでカラム（id、name、description）とフィールドのマッピングを定義
  - INSERT文（useGeneratedKeys="true"でID自動採番）を実装
  - SELECT文（WHERE id = #{id}でパラメータバインディング）を実装
  - UPDATE文とDELETE文を実装
  - _Requirements: 4.1, 4.2, 4.3, 4.4, 4.5, 4.6, 4.7, 4.8_

- [x] 3.2 ProjectMapper（XML）の実装
  - ProjectMapperインターフェースを作成し、@Mapperアノテーションを付与
  - insert、selectById、selectAll、selectByOrganizationId、selectProjectWithOrganization、update、deleteByIdメソッドを定義
  - ProjectMapper.xmlを作成し、namespaceを設定
  - resultMapでProjectフィールドのマッピングを定義
  - resultMapWithAssociationでProjectとOrganizationの結合マッピングを定義（association要素使用）
  - CRUD操作のSQL文を実装（INSERT、SELECT、UPDATE、DELETE）
  - selectByOrganizationIdメソッド（WHERE organization_id = #{organizationId}）を実装
  - selectProjectWithOrganizationメソッド（JOINクエリでProjectとOrganizationを取得）を実装
  - _Requirements: 5.1, 5.2, 5.3, 5.4, 5.5, 5.6, 5.7, 5.8, 7.2, 7.3, 7.4_

- [ ] 4. アノテーションマッパーによるCRUD操作の実装
- [x] 4.1 (P) OrganizationAnnotationMapperの実装
  - OrganizationAnnotationMapperインターフェースを作成し、@Mapperアノテーションを付与
  - @Insertアノテーションでinsertメソッドを定義（@Optionsで自動生成キー取得）
  - @SelectアノテーションでselectById、selectAllメソッドを定義
  - @Updateアノテーションでupdateメソッドを定義
  - @DeleteアノテーションでdeleteByIdメソッドを定義
  - XMLマッパーとの違いを明確化（アノテーション内にSQLを直接記述）
  - _Requirements: 6.1, 6.2, 6.3, 6.4, 6.5, 6.6_

- [ ] 5. サービス層の実装
- [x] 5.1 OrganizationServiceの実装
  - OrganizationServiceクラスを作成し、@Serviceアノテーションを付与
  - OrganizationMapperをコンストラクタインジェクションで受け取る
  - createOrganizationメソッド（@Transactional）を実装
  - getOrganizationメソッド（@Transactional(readOnly = true)）を実装
  - getAllOrganizationsメソッド（@Transactional(readOnly = true)）を実装
  - updateOrganizationメソッド（@Transactional）を実装
  - deleteOrganizationメソッド（@Transactional）を実装
  - _Requirements: 8.2, 8.3, 8.4, 9.1, 9.3, 9.4, 9.5, 9.6_

- [x] 5.2 (P) ProjectServiceの実装
  - ProjectServiceクラスを作成し、@Serviceアノテーションを付与
  - ProjectMapperをコンストラクタインジェクションで受け取る
  - createProjectメソッド（@Transactional）を実装
  - getProjectメソッド（@Transactional(readOnly = true)）を実装
  - getProjectWithOrganizationメソッド（@Transactional(readOnly = true)）を実装
  - getProjectsByOrganizationメソッド（@Transactional(readOnly = true)）を実装
  - getAllProjectsメソッド（@Transactional(readOnly = true)）を実装
  - updateProjectメソッド（@Transactional）を実装
  - deleteProjectメソッド（@Transactional）を実装
  - _Requirements: 8.2, 8.3, 8.4, 8.5, 9.2, 9.3, 9.4, 9.5, 9.6_

- [x] 6. 統合テストとデータ出力機能の実装
- [x] 6.1 MyBatisApplicationTestsの実装
  - MyBatisApplicationTestsクラスを作成し、@SpringBootTestアノテーションを付与
  - OrganizationServiceとProjectServiceをフィールドインジェクション
  - testOrganizationCrudメソッド（@Test）でOrganizationのCRUD操作をテスト（Create、Read、Update、Delete）
  - testProjectCrudメソッド（@Test）でProjectのCRUD操作をテスト
  - testProjectWithOrganizationメソッド（@Test）でリレーションシップ取得をテスト
  - testProjectsByOrganizationメソッド（@Test）で組織別Project検索をテスト
  - 各テストメソッドでSystem.out.printlnを使用してデータを標準出力
  - H2インメモリデータベースを使用したテスト実行を確認
  - SQLログ出力の確認（application.ymlの設定により自動出力）
  - _Requirements: 10.1, 10.2, 10.3, 10.5, 10.6, 10.7_

- [x] 6.2 (P) アノテーションマッパーのテスト追加
  - testOrganizationAnnotationMapperメソッド（@Test）を追加
  - OrganizationAnnotationMapperをフィールドインジェクション
  - アノテーションマッパーのCRUD操作をテスト
  - XMLマッパーとの違いを確認（同じ結果が得られることを確認）
  - System.out.printlnでデータを出力
  - _Requirements: 6.1, 6.2, 6.3, 6.4, 6.5, 10.5_
