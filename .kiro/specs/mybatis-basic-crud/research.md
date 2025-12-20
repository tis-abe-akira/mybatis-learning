# Research & Design Decisions

## Summary
- **Feature**: `mybatis-basic-crud`
- **Discovery Scope**: 新規機能（Greenfield） / 複雑な統合
- **Key Findings**:
  - MyBatis Spring Boot Starter 3.0.5が最新の安定版（Spring Boot 3.x対応）
  - XMLマッパーは複雑なクエリと動的SQL、アノテーションは単純なCRUDに適する
  - Spring Bootの自動設定により、MyBatisとH2の統合が大幅に簡素化される

## Research Log

### MyBatis Spring Boot Starter バージョンと互換性
- **Context**: Spring Boot 3.x環境でのMyBatis統合に最適なバージョンを特定
- **Sources Consulted**:
  - [Maven Repository: mybatis-spring-boot-starter](https://mvnrepository.com/artifact/org.mybatis.spring.boot/mybatis-spring-boot-starter)
  - [GitHub: mybatis/spring-boot-starter](https://github.com/mybatis/spring-boot-starter)
  - [MyBatis Spring Boot Autoconfigure Documentation](https://mybatis.org/spring-boot-starter/mybatis-spring-boot-autoconfigure/)
- **Findings**:
  - バージョン 3.0.5: MyBatis 3.5、MyBatis-Spring 3.0、Java 17+、Spring Boot 3.2-3.5をサポート
  - バージョン 4.0.0: Spring Boot 4.0対応（2025年11月30日リリース）
  - 自動設定により、SqlSessionFactory、SqlSessionTemplateの作成と登録が自動化
  - @Mapperアノテーションまたは@MapperScanでマッパーを自動検出
- **Implications**:
  - バージョン 3.0.5を採用（安定版、広く使用されている）
  - pom.xmlに`mybatis-spring-boot-starter:3.0.5`を追加
  - マッパーインターフェースに@Mapperアノテーションを付与

### H2データベース設定とSpring Boot統合
- **Context**: インメモリH2データベースの設定方法と初期化戦略
- **Sources Consulted**:
  - [Spring Boot With H2 Database | Baeldung](https://www.baeldung.com/spring-boot-h2-database)
  - [Configuring H2 Database in Spring Boot | Medium](https://medium.com/@humbleCoder007/configuring-h2-database-in-a-spring-boot-application-3c5b1ec49189)
  - [Spring Boot + MyBatis CRUD + H2 Example](https://www.javaguides.net/2019/08/spring-boot-mybatis-crud-h2-database-example.html)
- **Findings**:
  - application.ymlでの基本設定：
    - `spring.datasource.url: jdbc:h2:mem:testdb`
    - `spring.h2.console.enabled: true` （H2コンソールアクセス用）
    - `spring.sql.init.mode: always` （schema.sql、data.sqlの自動実行）
  - `src/main/resources/schema.sql` でDDL定義
  - `src/main/resources/data.sql` でテストデータ投入
- **Implications**:
  - スキーマ初期化をschema.sqlで管理
  - テストデータをdata.sqlで管理
  - H2コンソールを有効化して学習時のデータ確認を容易化

### XMLマッパー vs アノテーションマッパー
- **Context**: 学習プロジェクトとして両方のアプローチを実装する際の使い分け
- **Sources Consulted**:
  - [MyBatis: XML Configuration vs Annotations – CodingTechRoom](https://codingtechroom.com/question/mybatis-xml-vs-annotations)
  - [MyBatis Mapper XML Files Documentation](https://mybatis.org/mybatis-3/sqlmap-xml.html)
  - [MyBatis Annotations Tutorial](https://www.tutorialspoint.com/mybatis/mybatis_annotations.htm)
- **Findings**:
  - **XML マッパーの利点**:
    - 複雑なクエリと動的SQLに適している
    - SQLとJavaコードの明確な分離
    - resultMapによる柔軟なマッピング
    - ネストされたJOINマッピングなど高度な機能をサポート
  - **アノテーション マッパーの利点**:
    - シンプルなCRUD操作に適している
    - 設定が簡潔で迅速
    - XMLファイル不要
  - **ベストプラクティス**:
    - 同じマッパーでXMLとアノテーションを混在させない
    - プロジェクト全体では混在可能（マッパーごとに選択）
- **Implications**:
  - Organizationマッパー: XMLとアノテーションの両方を別々のインターフェースで実装
  - Projectマッパー: XMLマッパーでリレーションシップ（association）を学習
  - 学習目的で両方のアプローチを実装し、違いを理解

### MyBatis設定とSQLログ出力
- **Context**: 学習時にSQL実行内容を確認するための設定
- **Sources Consulted**:
  - [Introduction – mybatis-spring-boot-autoconfigure](https://mybatis.org/spring-boot-starter/mybatis-spring-boot-autoconfigure/)
  - [Integrating MyBatis with Spring Boot | Medium](https://medium.com/@rajrangaraj/integrating-mybatis-with-spring-boot-advanced-configuration-and-practices-40f25b3179d3)
- **Findings**:
  - `mybatis.configuration.log-impl: org.apache.ibatis.logging.stdout.StdOutImpl` でSQL出力
  - `mybatis.mapper-locations: classpath:mappers/*.xml` でマッパーXMLの場所指定
  - `mybatis.type-aliases-package` でエンティティクラスのエイリアス設定
  - `mybatis.configuration.map-underscore-to-camel-case: true` でスネークケース⇔キャメルケース自動変換
- **Implications**:
  - application.ymlでSQLログ出力を有効化
  - マッパーXMLファイルを`src/main/resources/mappers/`に配置
  - スネークケース⇔キャメルケース自動変換を有効化してカラム名とフィールド名の差異を吸収

## Architecture Pattern Evaluation

| Option | Description | Strengths | Risks / Limitations | Notes |
|--------|-------------|-----------|---------------------|-------|
| レイヤードアーキテクチャ | エンティティ、マッパー、サービス、テストの層分離 | 明確な責務分離、学習しやすい、Spring Bootの標準パターン | シンプルな学習プロジェクトには過剰設計の可能性 | ステアリングで定義された構造原則に適合 |
| ドメイン駆動設計（DDD） | ドメインモデル中心の設計 | ビジネスロジックの明確化 | 学習プロジェクトには複雑すぎる | 本プロジェクトでは不採用 |
| クリーンアーキテクチャ | 依存関係の逆転とポートアダプタ | 高い柔軟性とテスタビリティ | 小規模学習プロジェクトには過剰 | 本プロジェクトでは不採用 |

**選択**: レイヤードアーキテクチャ
**理由**: Spring Bootとの親和性が高く、学習目的に最適。ステアリングで定義された構造パターンに適合。

## Design Decisions

### Decision: MyBatis Spring Boot Starter バージョン選択
- **Context**: Spring Boot 3.x環境でMyBatisを使用するための依存関係バージョン
- **Alternatives Considered**:
  1. MyBatis Spring Boot Starter 3.0.3 - 安定版
  2. MyBatis Spring Boot Starter 3.0.5 - 最新の3.xライン
  3. MyBatis Spring Boot Starter 4.0.0 - Spring Boot 4.0対応（最新）
- **Selected Approach**: バージョン 3.0.5
- **Rationale**:
  - Spring Boot 3.x（3.2-3.5）との互換性が確認済み
  - 3.xラインの最新版で、バグフィックスとセキュリティパッチが適用済み
  - 4.0.0はSpring Boot 4.0が必要で、学習環境としては安定性を優先
- **Trade-offs**:
  - 利点: 安定性、広く使用されている、豊富なドキュメント
  - 欠点: 4.0.0の新機能は利用できない（学習には影響なし）
- **Follow-up**: Spring Boot 4.0への移行時に4.0.0へアップグレード検討

### Decision: XMLマッパーとアノテーションマッパーの使い分け
- **Context**: 学習目的で両方のアプローチを実装する際の方針
- **Alternatives Considered**:
  1. すべてXMLマッパーで実装
  2. すべてアノテーションマッパーで実装
  3. マッパーごとにXMLとアノテーションを使い分け
- **Selected Approach**: マッパーごとに使い分け
- **Rationale**:
  - 学習目的で両方のアプローチを経験することが重要
  - XMLマッパー: リレーションシップ（association）など複雑な機能の学習
  - アノテーションマッパー: シンプルなCRUD操作の学習
  - ベストプラクティスに従い、同じマッパーで混在させない
- **Trade-offs**:
  - 利点: 両方のアプローチの理解、実際のプロジェクトでの選択基準を学べる
  - 欠点: コード量増加、メンテナンス対象の増加（学習には問題なし）
- **Follow-up**: 実装後に両方のアプローチの違いを明確に理解

### Decision: データベーススキーマ初期化方法
- **Context**: H2データベースのテーブル作成とテストデータ投入方法
- **Alternatives Considered**:
  1. schema.sql + data.sql（Spring Bootの自動実行）
  2. Flyway/Liquibaseなどのマイグレーションツール
  3. @Sqlアノテーションでテストごとに初期化
- **Selected Approach**: schema.sql + data.sql
- **Rationale**:
  - Spring Bootの標準機能で設定が簡単
  - 学習プロジェクトとして、マイグレーションツールは過剰
  - テストデータの確認が容易
- **Trade-offs**:
  - 利点: シンプル、Spring Bootとの統合が容易
  - 欠点: バージョン管理や段階的マイグレーションには不向き（学習には問題なし）
- **Follow-up**: 本番プロジェクトではFlywayなどの使用を推奨

## Risks & Mitigations

- **リスク1**: MyBatisとSpring Bootの設定ミスによる起動エラー
  - **Mitigation**: 公式ドキュメントの設定例に従い、application.ymlの設定を段階的に追加。エラーログを確認しながら修正。

- **リスク2**: XMLマッパーとアノテーションマッパーの混在による設定競合
  - **Mitigation**: 同じマッパーインターフェースでXMLとアノテーションを混在させない。別々のインターフェースとして実装。

- **リスク3**: H2データベースのスキーマ初期化タイミングの問題
  - **Mitigation**: `spring.sql.init.mode: always`でスキーマを毎回初期化。テスト環境ではインメモリDBを使用。

- **リスク4**: SQLログ出力が多すぎてコンソールが読みにくくなる
  - **Mitigation**: 必要に応じてロギングレベルを調整。学習時はSQL出力を有効化し、理解後は無効化可能。

## References
- [Maven Repository: mybatis-spring-boot-starter](https://mvnrepository.com/artifact/org.mybatis.spring.boot/mybatis-spring-boot-starter)
- [GitHub: mybatis/spring-boot-starter](https://github.com/mybatis/spring-boot-starter)
- [MyBatis Spring Boot Autoconfigure Documentation](https://mybatis.org/spring-boot-starter/mybatis-spring-boot-autoconfigure/)
- [Spring Boot With H2 Database | Baeldung](https://www.baeldung.com/spring-boot-h2-database)
- [MyBatis: XML Configuration vs Annotations](https://codingtechroom.com/question/mybatis-xml-vs-annotations)
- [MyBatis Mapper XML Files Documentation](https://mybatis.org/mybatis-3/sqlmap-xml.html)
- [Integrating MyBatis with Spring Boot | Medium](https://medium.com/@rajrangaraj/integrating-mybatis-with-spring-boot-advanced-configuration-and-practices-40f25b3179d3)
