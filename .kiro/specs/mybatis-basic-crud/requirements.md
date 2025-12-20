# Requirements Document

## Project Description (Input)
MyBatisの機能を学習するためのプロジェクト。Spring BootとMyBatisを使用したProject/Organization管理システムを構築する。ProjectとOrganization（組織）の2つのエンティティがあり、Projectは組織に従属するデータモデル。XMLマッパーとアノテーションマッパーの両方を使用して基本的なCRUD操作を実装し、MyBatisのSQLマッピング、動的SQL、トランザクション管理などの機能を実践的に学習する。データベースはH2を使用し、SpringTest（JUnit5）でサービス層からMyBatisを経由してデータベース操作を行い、実際のデータを標準出力で確認する。

## Introduction
本仕様は、Spring BootとMyBatisを使用したProject/Organization管理システムの基本的なCRUD機能を実装するための要件を定義します。XMLマッパーとアノテーションマッパーの両方のアプローチでデータベース操作を実装し、MyBatisのSQLマッピング機能、トランザクション管理、およびエンティティ間のリレーションシップ（多対一）を学習できる環境を構築します。

## Requirements

### Requirement 1: プロジェクトセットアップ
**Objective:** 開発者として、Spring BootベースのMyBatisプロジェクトを初期化したい。これにより、MyBatis機能の学習基盤を整備できる。

#### Acceptance Criteria
1. The システムは、Spring Bootプロジェクト構成を含むこと
2. The システムは、Mavenビルド構成（pom.xml）を持つこと
3. The システムは、MyBatis Spring Boot Starterの依存関係を含むこと
4. The システムは、データベースドライバー（H2）の依存関係を含むこと
5. The システムは、JUnit5とSpring Testの依存関係を含むこと
6. The システムは、適切なパッケージ構成（entity、mapper、service）を持つこと
7. The システムは、application.ymlまたはapplication.propertiesでMyBatis設定を管理できること

### Requirement 2: エンティティクラスの定義
**Objective:** 開発者として、OrganizationとProjectのエンティティクラスを定義したい。これにより、データモデルとJavaオブジェクトのマッピングを理解できる。

#### Acceptance Criteria
1. The システムは、OrganizationエンティティクラスをPOJOとして定義すること
2. The Organizationエンティティは、ID（Long型）、名前（String型）、説明（String型）のフィールドを持つこと
3. The システムは、ProjectエンティティクラスをPOJOとして定義すること
4. The Projectエンティティは、ID（Long型）、プロジェクト名（String型）、organizationId（Long型）のフィールドを持つこと
5. The エンティティクラスは、getter/setterメソッドを持つこと
6. The エンティティクラスは、デフォルトコンストラクタとフィールド初期化コンストラクタを持つこと

### Requirement 3: データベーススキーマの作成
**Objective:** 開発者として、OrganizationとProjectテーブルのスキーマを作成したい。これにより、MyBatisが操作するデータベース構造を準備できる。

#### Acceptance Criteria
1. The システムは、organizationsテーブルを定義するDDLを含むこと
2. The organizationsテーブルは、id（主キー、自動採番）、name、descriptionカラムを持つこと
3. The システムは、projectsテーブルを定義するDDLを含むこと
4. The projectsテーブルは、id（主キー、自動採番）、project_name、organization_id（外部キー）カラムを持つこと
5. When アプリケーションが起動する場合、the システムはスキーマを自動的に初期化すること（schema.sqlまたはdata.sql使用）
6. The システムは、テストデータを初期投入する仕組みを持つこと

### Requirement 4: XMLマッパーによるCRUD操作（Organization）
**Objective:** 開発者として、XMLマッパーを使用してOrganizationエンティティのCRUD操作を実装したい。これにより、MyBatisのXMLベースSQLマッピングを理解できる。

#### Acceptance Criteria
1. The システムは、OrganizationMapper.javaインターフェースを定義すること
2. The システムは、OrganizationMapper.xmlファイルを持つこと
3. When insertメソッドが呼び出される場合、the システムはINSERT文を実行し新しいOrganizationレコードを作成すること
4. When selectByIdメソッドが呼び出される場合、the システムは指定されたIDのOrganizationを取得すること
5. When selectAllメソッドが呼び出される場合、the システムはすべてのOrganizationをリストで返すこと
6. When updateメソッドが呼び出される場合、the システムはUPDATE文を実行し既存のOrganizationレコードを更新すること
7. When deleteByIdメソッドが呼び出される場合、the システムはDELETE文を実行し指定されたOrganizationレコードを削除すること
8. The XMLマッパーは、resultMapを使用してカラムとフィールドのマッピングを定義すること

### Requirement 5: XMLマッパーによるCRUD操作（Project）
**Objective:** 開発者として、XMLマッパーを使用してProjectエンティティのCRUD操作を実装したい。これにより、リレーションシップを含むSQLマッピングを理解できる。

#### Acceptance Criteria
1. The システムは、ProjectMapper.javaインターフェースを定義すること
2. The システムは、ProjectMapper.xmlファイルを持つこと
3. When insertメソッドが呼び出される場合、the システムはINSERT文を実行し新しいProjectレコードを作成すること
4. When selectByIdメソッドが呼び出される場合、the システムは指定されたIDのProjectを取得すること
5. When selectAllメソッドが呼び出される場合、the システムはすべてのProjectをリストで返すこと
6. When selectByOrganizationIdメソッドが呼び出される場合、the システムは指定されたorganizationIdに紐づくProjectリストを取得すること
7. When updateメソッドが呼び出される場合、the システムはUPDATE文を実行し既存のProjectレコードを更新すること
8. When deleteByIdメソッドが呼び出される場合、the システムはDELETE文を実行し指定されたProjectレコードを削除すること

### Requirement 6: アノテーションマッパーによるCRUD操作
**Objective:** 開発者として、アノテーションベースのマッパーを使用してCRUD操作を実装したい。これにより、XMLを使わないSQLマッピングアプローチを理解できる。

#### Acceptance Criteria
1. The システムは、アノテーションベースのマッパーインターフェース（例：OrganizationAnnotationMapper）を定義すること
2. When @Selectアノテーションが付与されたメソッドが呼び出される場合、the システムはSELECT文を実行しデータを取得すること
3. When @Insertアノテーションが付与されたメソッドが呼び出される場合、the システムはINSERT文を実行しデータを挿入すること
4. When @Updateアノテーションが付与されたメソッドが呼び出される場合、the システムはUPDATE文を実行しデータを更新すること
5. When @Deleteアノテーションが付与されたメソッドが呼び出される場合、the システムはDELETE文を実行しデータを削除すること
6. The システムは、@Optionsアノテーションを使用して生成されたキーを取得できること

### Requirement 7: リレーションシップの実装（多対一）
**Objective:** 開発者として、ProjectからOrganizationへの多対一リレーションシップを実装したい。これにより、MyBatisの関連データ取得方法を理解できる。

#### Acceptance Criteria
1. The Projectエンティティは、Organizationオブジェクトを保持するフィールドを持つこと
2. When ProjectをIDで取得する場合、the システムは関連するOrganization情報も同時に取得できること
3. The XMLマッパーは、associationまたはJOINを使用してリレーションシップを表現すること
4. When selectProjectWithOrganizationメソッドが呼び出される場合、the システムはProjectとOrganizationの結合データを返すこと

### Requirement 8: データベース接続とトランザクション管理
**Objective:** 開発者として、データベース接続とトランザクションが適切に管理されることを確認したい。これにより、MyBatisのトランザクション管理機能を理解できる。

#### Acceptance Criteria
1. The システムは、application設定ファイルで指定されたH2データベースに接続できること
2. When サービス層のメソッドに@Transactionalアノテーションが付与される場合、the システムはトランザクション境界を設定すること
3. If トランザクション内で例外が発生した場合、the システムは変更をロールバックすること
4. When トランザクションが正常に完了した場合、the システムは変更をコミットすること
5. The システムは、MyBatisのSqlSessionを通じてデータベース操作を実行すること

### Requirement 9: サービス層の実装
**Objective:** 開発者として、マッパーを呼び出すサービス層を実装したい。これにより、ビジネスロジック層とデータアクセス層の分離を理解できる。

#### Acceptance Criteria
1. The システムは、OrganizationServiceクラスを持つこと
2. The システムは、ProjectServiceクラスを持つこと
3. When サービスメソッドが呼び出される場合、the システムは対応するマッパーメソッドを呼び出すこと
4. The サービスクラスは、@Serviceアノテーションでマークされること
5. The サービスクラスは、マッパーインターフェースをDI（依存性注入）で受け取ること
6. The サービスクラスは、@Transactionalアノテーションでトランザクション管理を行うこと

### Requirement 10: テストとデータ出力機能
**Objective:** 開発者として、MyBatis操作の詳細とデータ内容を確認したい。これにより、MyBatisの内部動作とデータ状態を理解できる。

#### Acceptance Criteria
1. The システムは、Spring Boot Test（@SpringBootTest）を使用したテストクラスを持つこと
2. The システムは、JUnit5を使用してサービス層のCRUD操作をテストできること
3. When テストが実行される場合、the システムは実行されたSQL文をログに出力すること
4. The システムは、MyBatis設定でSQLログ出力を有効化できること（mybatis.configuration.log-impl設定）
5. When データ取得後、the システムはエンティティデータを標準出力（System.out.println）で表示すること
6. The システムは、@Testアノテーションが付与されたメソッドで各CRUD操作を検証できること
7. The システムは、テスト実行時にH2インメモリデータベースを使用すること


