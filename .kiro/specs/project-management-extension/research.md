# Research & Design Decisions

---
**Purpose**: プロジェクト管理機能拡張における技術調査結果と設計判断の記録

**Usage**:
- 既存システムとの統合ポイント分析
- MyBatis高度パターンの技術検証
- 設計判断の根拠と代替案の記録
---

## Summary
- **Feature**: `project-management-extension`
- **Discovery Scope**: Extension（既存Project/Organizationエンティティの拡張）
- **Key Findings**:
  - MyBatis 3.5.16はJava 8 LocalDate/LocalDateTimeを標準サポート
  - Enum型は2種類のTypeHandler（EnumTypeHandler、EnumOrdinalTypeHandler）でサポート
  - 多対多関係はcollection + nested associationパターンで実装可能
  - 既存パターン（POJO + XML Mapper + resultMap）を踏襲することで一貫性を保つ

## Research Log

### 既存システム分析

**Context**: Project/Organizationエンティティの実装パターンと統合ポイントの確認

**Findings**:
- **エンティティパターン**: プライベートフィールド + getter/setter + toString()を持つPOJO
- **Mapperパターン**: XMLベースマッパー（resultMap、association使用）
- **データベーススキーマ**: AUTO_INCREMENT主キー、外部キー制約
- **既存リレーション**: Project → Organization（多対一、associationマッピング）

**Implications**: 新規エンティティも同一パターンで実装することで、学習の一貫性と保守性を確保

### MyBatis Enum型サポート

**Context**: ProjectType、ProjectStatus、MemberRole等のEnum型マッピング方法の調査

**Sources Consulted**:
- MyBatis公式ドキュメント: https://mybatis.org/mybatis-3/configuration.html

**Findings**:
- **EnumTypeHandler（デフォルト）**: Enum値を文字列（Enum名）としてVARCHARカラムに保存
- **EnumOrdinalTypeHandler**: Enum値を序数（ordinal）としてNUMERICカラムに保存
- 明示的な設定なしでEnumTypeHandlerが自動適用される
- 必要に応じてresultMapやSQL文でtypeHandlerを明示指定可能

**Implications**:
- Enum型フィールドはそのまま定義でき、MyBatisが自動的に文字列として永続化
- データベーススキーマではVARCHARカラムを使用
- 人間可読性を優先してEnumTypeHandler（デフォルト）を採用

### MyBatis Java 8日付型サポート

**Context**: LocalDate型（計画・実績日付）のマッピング方法の調査

**Sources Consulted**:
- MyBatis公式ドキュメント: https://mybatis.org/mybatis-3/configuration.html

**Findings**:
- MyBatis 3.4.5以降、JSR-310（Date and Time API）を標準サポート
- LocalDate、LocalDateTime、LocalTime等のビルトインTypeHandlerが自動登録
- LocalDate → DATEカラム、LocalDateTime → TIMESTAMPカラムにマッピング
- カスタムTypeHandlerの登録不要

**Implications**:
- プロジェクト使用中のMyBatis 3.5.16は完全サポート
- エンティティでLocalDate型を直接使用可能
- データベーススキーマではDATE型カラムを定義

### 多対多マッピングパターン

**Context**: Project ↔ Person、Project ↔ Technologyの多対多関係の実装方法

**Sources Consulted**:
- MyBatis公式ドキュメント: https://mybatis.org/mybatis-3/sqlmap-xml.html

**Findings**:
- 中間テーブル（ProjectMember、ProjectTechnology）を明示的にエンティティ化
- collectionマッピングでネストされたassociationを使用
- JOIN文で必要なデータを一度に取得し、N+1問題を回避
- columnPrefixを活用して結合テーブルのカラムを区別

**Implications**:
- ProjectMember、ProjectTechnologyを独立したエンティティとして定義
- 各々がProject/Personへのassociationを持つ
- Projectエンティティはcollectionとして<ProjectMember>と<ProjectTechnology>を保持

## Architecture Pattern Evaluation

| Option | Description | Strengths | Risks / Limitations | Notes |
|--------|-------------|-----------|---------------------|-------|
| 中間テーブルエンティティ化 | ProjectMember、ProjectTechnologyを独立エンティティとして定義 | 追加属性（role、allocationRate、version、purpose）を保持可能。CRUD操作が明確。 | エンティティ数が増加 | 要件に合致。追加属性の管理が必須のため、この方式を採用 |
| 直接多対多 | ProjectにList<Person>、List<Technology>を直接保持 | シンプルなデータモデル | 中間テーブルの追加属性を保持できない | 要件では役割・稼働率・バージョン等の管理が必要なため不採用 |

## Design Decisions

### Decision: Enum型の保存形式（文字列 vs 序数）

- **Context**: ProjectType、ProjectStatus、MemberRole等のEnum値をどの形式で保存するか
- **Alternatives Considered**:
  1. EnumTypeHandler（文字列名） - デフォルト
  2. EnumOrdinalTypeHandler（序数）
- **Selected Approach**: EnumTypeHandler（文字列名）
- **Rationale**:
  - データベースの可読性が高い
  - Enum定義の並び順変更に強い
  - デバッグ時に状態が明確
  - 学習プロジェクトとして、データを直接確認しやすい
- **Trade-offs**: VARCHAR型のため若干のストレージ増加（学習用途では問題なし）
- **Follow-up**: テスト時にSystem.out.printlnでEnum値を確認し、文字列として保存されることを検証

### Decision: Projectエンティティの拡張方法（既存クラス拡張 vs 新規クラス作成）

- **Context**: Projectエンティティに多数の新規フィールドを追加する必要がある
- **Alternatives Considered**:
  1. 既存Projectクラスにフィールド追加
  2. ProjectExtensionクラスを作成し、Projectを継承
- **Selected Approach**: 既存Projectクラスに直接フィールド追加
- **Rationale**:
  - 学習プロジェクトとして、エンティティの進化を一箇所で追跡できる
  - 継承による複雑性を避ける
  - 既存テストとの互換性を保ちやすい
  - MyBatisは継承よりもフラットなPOJOを推奨
- **Trade-offs**: 既存Projectエンティティが大きくなる
- **Follow-up**: 既存のProjectMapperTest、ProjectServiceTestが引き続きパスすることを確認

### Decision: Mapper実装方式（XML vs Annotation）

- **Context**: 新規6エンティティのMapper実装方式の選択
- **Alternatives Considered**:
  1. XMLマッパー
  2. アノテーションマッパー
  3. ハイブリッド（基本CRUD:アノテーション、複雑クエリ:XML）
- **Selected Approach**: XMLマッパーを主とする
- **Rationale**:
  - association、collection等の複雑なマッピングはXMLが明瞭
  - resultMapによる明示的なマッピング定義が学習に適している
  - 既存パターン（OrganizationMapper.xml、ProjectMapper.xml）との一貫性
  - 複雑なJOIN文やdynamic SQLはXMLで管理しやすい
- **Trade-offs**: ファイル数が増える
- **Follow-up**: 学習ノート（learning-notes.md）でXML vs Annotationの使い分けを解説

## Risks & Mitigations

- **Risk**: Projectエンティティの拡張により既存テストが失敗する可能性
  - **Mitigation**: 既存フィールド（id、projectName、organizationId）はそのまま維持。新規フィールドはすべてnullableとしてデフォルト値を持たせる。既存テストを実行し、互換性を確認。

- **Risk**: 多対多マッピングでN+1クエリ問題が発生する可能性
  - **Mitigation**: JOINクエリで必要なデータを一度に取得。ログ出力でSQL実行回数を確認。

- **Risk**: Enum型のマイグレーション時に文字列とordinalの不一致
  - **Mitigation**: EnumTypeHandler（文字列）を一貫して使用。データベースにもVARCHAR型を定義。

- **Risk**: LocalDate型のタイムゾーン問題
  - **Mitigation**: LocalDateは日付のみを扱うためタイムゾーン問題なし。必要に応じてUTCで統一。

## References

- [MyBatis 3 Configuration - Type Handlers](https://mybatis.org/mybatis-3/configuration.html#typeHandlers)
- [MyBatis 3 SQL Maps - Result Maps](https://mybatis.org/mybatis-3/sqlmap-xml.html#Result_Maps)
- [MyBatis 3 SQL Maps - Association & Collection](https://mybatis.org/mybatis-3/sqlmap-xml.html#association)
- [MyBatis JSR-310 Support](https://mybatis.org/mybatis-3/configuration.html#typeHandlers) - LocalDate/LocalDateTime native support since 3.4.5
