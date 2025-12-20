# 第1章: 基本編 - 学習ノート

このドキュメントは、`mybatis-basic-crud`で実装した内容の学習ポイントをまとめたものです。実装コードと合わせて読むことで、MyBatisの基礎を理解できます。

---

## 📚 この章で学べること

1. MyBatisの3つのマッパー定義方式
2. XMLマッパーとアノテーションの使い分け
3. リレーションシップマッピング（association）
4. トランザクション管理
5. テスト駆動開発（TDD）によるMyBatis実装

---

## 1. MyBatisマッパーの3つの方式

### 方式1: XMLマッパー

**実装例**: `src/main/resources/mappers/OrganizationMapper.xml`

```xml
<mapper namespace="com.example.mybatislearning.mapper.OrganizationMapper">
    <resultMap id="organizationResultMap" type="...Organization">
        <id property="id" column="id"/>
        <result property="name" column="name"/>
        <result property="description" column="description"/>
    </resultMap>

    <insert id="insert" useGeneratedKeys="true" keyProperty="id">
        INSERT INTO organizations (name, description)
        VALUES (#{name}, #{description})
    </insert>
</mapper>
```

**特徴**:
- ✅ 複雑なSQLに適している
- ✅ resultMapで柔軟なマッピング
- ✅ 動的SQL（`<if>`, `<foreach>`など）が書きやすい
- ✅ SQLとJavaコードの分離

**いつ使う？**
- 複雑なJOIN、サブクエリ
- 長いSQL（100行以上）
- DBAとの協業（SQLを直接編集）

---

### 方式2: アノテーションマッパー

**実装例**: `src/main/java/.../mapper/OrganizationAnnotationMapper.java`

```java
@Mapper
public interface OrganizationAnnotationMapper {
    @Insert("INSERT INTO organizations (name, description) VALUES (#{name}, #{description})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insert(Organization organization);

    @Select("SELECT * FROM organizations WHERE id = #{id}")
    Organization selectById(Long id);
}
```

**特徴**:
- ✅ シンプルなCRUDに最適
- ✅ コード量が少ない
- ✅ インターフェースだけで完結
- ❌ 複雑なSQLは読みにくい

**いつ使う？**
- シンプルなCRUD（1-3行のSQL）
- 小規模プロジェクト
- プロトタイピング

---

### 方式3: SQLプロバイダー（未実装）

動的にSQLを生成する方式。第2章で学習予定。

```java
@SelectProvider(type = OrganizationSqlProvider.class, method = "selectSql")
List<Organization> search(@Param("name") String name);
```

**いつ使う？**
- 複雑な動的SQL
- 条件分岐が多い検索機能

---

## 2. 実装コード詳細解説

### 2.1 XMLマッパーの構造

**ファイル**: `OrganizationMapper.xml`

#### resultMapの定義（L9-13）
```xml
<resultMap id="organizationResultMap" type="com.example.mybatislearning.entity.Organization">
    <id property="id" column="id"/>
    <result property="name" column="name"/>
    <result property="description" column="description"/>
</resultMap>
```

**学習ポイント**:
- `<id>`: 主キーのマッピング
- `<result>`: 通常カラムのマッピング
- スネークケース（DB）⇔キャメルケース（Java）は自動変換（`application.yml`で設定済み）

#### INSERT文（L16-20）
```xml
<insert id="insert" useGeneratedKeys="true" keyProperty="id">
    INSERT INTO organizations (name, description)
    VALUES (#{name}, #{description})
</insert>
```

**学習ポイント**:
- `useGeneratedKeys="true"`: 自動採番されたIDを取得
- `keyProperty="id"`: 取得したIDをエンティティのidフィールドに設定
- `#{}`: パラメータバインディング（SQLインジェクション対策）

#### SELECT文（L23-28）
```xml
<select id="selectById" parameterType="long" resultMap="organizationResultMap">
    SELECT id, name, description
    FROM organizations
    WHERE id = #{id}
</select>
```

**学習ポイント**:
- `resultMap`: 定義済みのresultMapを参照
- `#{id}`: プレースホルダーとしてバインディング

---

### 2.2 リレーションシップマッピング（association）

**ファイル**: `ProjectMapper.xml`

#### resultMapWithAssociation（L16-27）
```xml
<resultMap id="projectWithOrganizationResultMap" type="...Project">
    <id property="id" column="id"/>
    <result property="projectName" column="project_name"/>
    <result property="organizationId" column="organization_id"/>
    <association property="organization" javaType="...Organization">
        <id property="id" column="org_id"/>
        <result property="name" column="org_name"/>
        <result property="description" column="org_description"/>
    </association>
</resultMap>
```

**学習ポイント**:
- `<association>`: 多対一のリレーションシップ
- `property="organization"`: Projectエンティティのorganizationフィールドにマッピング
- カラムエイリアス（`org_id`, `org_name`）で親子を区別

#### JOIN SQL（L59-71）
```xml
<select id="selectProjectWithOrganization" resultMap="projectWithOrganizationResultMap">
    SELECT p.id,
           p.project_name,
           p.organization_id,
           o.id          AS org_id,
           o.name        AS org_name,
           o.description AS org_description
    FROM projects p
    INNER JOIN organizations o ON p.organization_id = o.id
    WHERE p.id = #{id}
</select>
```

**学習ポイント**:
- **N+1問題を回避**: 1回のJOINで両方のデータを取得
- カラムエイリアスとresultMapの対応が重要
- これにより、`project.getOrganization()`でOrganizationオブジェクトが取得できる

**テストコード**: `MyBatisApplicationTests.java:59-75`
```java
Project project = projectService.getProjectWithOrganization(1L);
assertNotNull(project.getOrganization());  // Organizationが取得されている
```

---

### 2.3 サービス層とトランザクション管理

**ファイル**: `OrganizationService.java`

#### トランザクション境界の設定（L35-38）
```java
@Transactional
public void createOrganization(Organization organization) {
    organizationMapper.insert(organization);
}
```

**学習ポイント**:
- `@Transactional`: メソッド実行中のトランザクション境界
- 例外発生時は自動ロールバック
- 正常終了時は自動コミット

#### 読み取り専用トランザクション（L47-50）
```java
@Transactional(readOnly = true)
public Organization getOrganization(Long id) {
    return organizationMapper.selectById(id);
}
```

**学習ポイント**:
- `readOnly = true`: パフォーマンス最適化のヒント
- データベースによっては読み取り専用接続を使用
- 更新操作は実行されない

---

## 3. テスト駆動開発（TDD）

このプロジェクトでは、**RED → GREEN → REFACTOR**のTDDサイクルで実装しました。

### 実装の流れ

1. **RED**: テストを先に書く（失敗する）
   - 例: `OrganizationMapperTest.java`を作成
   - コンパイルエラー（OrganizationMapperが存在しない）

2. **GREEN**: 最小限の実装でテストをパスさせる
   - `OrganizationMapper.java`（インターフェース）作成
   - `OrganizationMapper.xml`作成
   - テスト成功

3. **REFACTOR**: コードを改善（今回は不要）

### テストの構成

**統合テスト**: `MyBatisApplicationTests.java`

```java
@SpringBootTest
class MyBatisApplicationTests {
    @Autowired
    private OrganizationService organizationService;

    @Test
    void testOrganizationCrud() {
        // Create
        Organization org = new Organization(null, "Test Org", "Description");
        organizationService.createOrganization(org);
        assertNotNull(org.getId());  // IDが自動採番される

        // Read
        Organization retrieved = organizationService.getOrganization(org.getId());
        assertEquals("Test Org", retrieved.getName());

        // Update & Delete...
    }
}
```

**学習ポイント**:
- `@SpringBootTest`: Springコンテキストを起動
- H2インメモリデータベースでテスト実行
- SQLログが標準出力される（`application.yml`で設定）

---

## 4. XMLとアノテーションの比較表

| 項目 | XMLマッパー | アノテーション |
|------|------------|--------------|
| **適用範囲** | 複雑なSQL | シンプルなCRUD |
| **可読性** | ✅ 高い（長いSQLでも） | ❌ 低い（長いと読みにくい） |
| **動的SQL** | ✅ 容易（`<if>`, `<foreach>`） | ⚠️ 可能だが冗長（`<script>`タグ必要） |
| **resultMap** | ✅ 柔軟（association, collection） | ⚠️ 限定的（`@Results`） |
| **保守性** | ✅ SQLとJavaを分離 | ⚠️ Javaコードに混在 |
| **記述量** | 多い（XMLファイル） | 少ない（インターフェースのみ） |

### このプロジェクトでの使い分け

- **OrganizationMapper**: XMLとアノテーション**両方**実装して比較
- **ProjectMapper**: XMLのみ（JOIN、associationが必要なため）

---

## 5. 実装済みファイル一覧

### エンティティ
- `Organization.java` - 組織エンティティ（POJO）
- `Project.java` - プロジェクトエンティティ（Organizationへの参照を持つ）

### マッパー層
- `OrganizationMapper.java` + `OrganizationMapper.xml` - XMLマッパー
- `ProjectMapper.java` + `ProjectMapper.xml` - XMLマッパー（association使用）
- `OrganizationAnnotationMapper.java` - アノテーションマッパー

### サービス層
- `OrganizationService.java` - トランザクション管理
- `ProjectService.java` - トランザクション管理

### テスト
- `OrganizationMapperTest.java` - マッパー層テスト
- `ProjectMapperTest.java` - マッパー層テスト（association含む）
- `OrganizationAnnotationMapperTest.java` - アノテーションマッパーテスト
- `OrganizationServiceTest.java` - サービス層テスト
- `ProjectServiceTest.java` - サービス層テスト
- `MyBatisApplicationTests.java` - 統合テスト

---

## 6. よくある質問

### Q1: なぜXMLとアノテーションを両方実装したのか？

**A**: 学習目的で両方の方式を体験するため。実際のプロジェクトでは、チームの方針に従って統一するか、適材適所で使い分けます。

### Q2: associationとcollectionの違いは？

**A**:
- `association`: 多対一（Project → Organization）- 今回実装
- `collection`: 一対多（Organization → List<Project>）- 第4章で実装予定

### Q3: なぜH2データベースを使うのか？

**A**:
- インメモリで外部DBが不要
- テスト実行が高速
- 学習に集中できる

本番環境ではPostgreSQL、MySQLなどを使用します。

---

## 7. 次のステップ

✅ **第1章完了！習得したスキル**:
- XMLマッパーとアノテーションマッパーの実装
- リレーションシップマッピング（association）
- トランザクション管理
- TDDによるテスト実装

📝 **第2章: 動的SQL**へ進む準備ができました！

次の学習内容：
- 検索条件の動的生成（`<if>`, `<where>`）
- 一括操作（`<foreach>`）
- SQLプロバイダーの実装
- 実践的な検索機能の構築

新しい章を始めるには：
```bash
/kiro:spec-init "MyBatis動的SQL - 検索条件の動的生成と一括操作"
```

---

**学習ガイド全体**: [../../steering/learning-guide.md](../../steering/learning-guide.md)
