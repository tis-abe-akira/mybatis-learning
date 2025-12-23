package com.example.mybatislearning.mapper;

import com.example.mybatislearning.entity.Technology;
import com.example.mybatislearning.enums.TechnologyCategory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * TechnologyMapperTest
 * TechnologyMapperのCRUD操作とEnum型マッピングをテスト
 */
@SpringBootTest
@Transactional
class TechnologyMapperTest {

    @Autowired
    private TechnologyMapper technologyMapper;

    /**
     * Technologyの作成をテスト
     */
    @Test
    void testInsertTechnology() {
        System.out.println("\n=== Technology Insert Test ===");

        // Create
        Technology technology = new Technology(null, "Java", TechnologyCategory.LANGUAGE, "オブジェクト指向プログラミング言語");
        technologyMapper.insert(technology);
        System.out.println("Created: " + technology);

        // 検証: IDが自動採番されること
        assertNotNull(technology.getId());
        assertTrue(technology.getId() > 0);

        System.out.println("=== Technology Insert Test Completed ===\n");
    }

    /**
     * TechnologyのselectById検索をテスト
     */
    @Test
    void testSelectTechnologyById() {
        System.out.println("\n=== Technology SelectById Test ===");

        // 事前データ作成
        Technology technology = new Technology(null, "Spring Boot", TechnologyCategory.FRAMEWORK, "Javaアプリケーションフレームワーク");
        technologyMapper.insert(technology);
        Long insertedId = technology.getId();

        // Read
        Technology retrieved = technologyMapper.selectById(insertedId);
        System.out.println("Retrieved: " + retrieved);

        // 検証
        assertNotNull(retrieved);
        assertEquals("Spring Boot", retrieved.getName());
        assertEquals(TechnologyCategory.FRAMEWORK, retrieved.getCategory());
        assertEquals("Javaアプリケーションフレームワーク", retrieved.getDescription());
        assertNotNull(retrieved.getCreatedAt());
        assertNotNull(retrieved.getUpdatedAt());

        System.out.println("=== Technology SelectById Test Completed ===\n");
    }

    /**
     * TechnologyのselectAll検索をテスト
     */
    @Test
    void testSelectAllTechnologies() {
        System.out.println("\n=== Technology SelectAll Test ===");

        // 事前データ作成
        Technology tech1 = new Technology(null, "PostgreSQL", TechnologyCategory.DATABASE, "オープンソースリレーショナルデータベース");
        Technology tech2 = new Technology(null, "Docker", TechnologyCategory.INFRASTRUCTURE, "コンテナ仮想化プラットフォーム");
        technologyMapper.insert(tech1);
        technologyMapper.insert(tech2);

        // Read All
        List<Technology> allTechnologies = technologyMapper.selectAll();
        System.out.println("All Technologies: " + allTechnologies.size() + " records");
        for (Technology t : allTechnologies) {
            System.out.println("  - " + t);
        }

        // 検証
        assertNotNull(allTechnologies);
        assertTrue(allTechnologies.size() >= 2);

        System.out.println("=== Technology SelectAll Test Completed ===\n");
    }

    /**
     * TechnologyのfindByCategory検索をテスト（Enum型マッピング確認）
     */
    @Test
    void testFindTechnologyByCategory() {
        System.out.println("\n=== Technology FindByCategory Test ===");

        // 事前データ作成（複数カテゴリ）
        Technology lang1 = new Technology(null, "Python", TechnologyCategory.LANGUAGE, "汎用プログラミング言語");
        Technology lang2 = new Technology(null, "JavaScript", TechnologyCategory.LANGUAGE, "Web開発言語");
        Technology db = new Technology(null, "MySQL", TechnologyCategory.DATABASE, "オープンソースRDB");
        technologyMapper.insert(lang1);
        technologyMapper.insert(lang2);
        technologyMapper.insert(db);

        // Find by Category LANGUAGE
        List<Technology> languages = technologyMapper.findByCategory(TechnologyCategory.LANGUAGE);
        System.out.println("Found LANGUAGE category: " + languages.size() + " records");
        for (Technology t : languages) {
            System.out.println("  - " + t);
        }

        // 検証
        assertNotNull(languages);
        assertTrue(languages.size() >= 2);
        for (Technology t : languages) {
            assertEquals(TechnologyCategory.LANGUAGE, t.getCategory());
        }

        // Find by Category DATABASE
        List<Technology> databases = technologyMapper.findByCategory(TechnologyCategory.DATABASE);
        System.out.println("Found DATABASE category: " + databases.size() + " records");
        for (Technology t : databases) {
            System.out.println("  - " + t);
        }

        // 検証
        assertNotNull(databases);
        assertTrue(databases.size() >= 1);
        for (Technology t : databases) {
            assertEquals(TechnologyCategory.DATABASE, t.getCategory());
        }

        System.out.println("=== Technology FindByCategory Test Completed ===\n");
    }

    /**
     * Technologyの更新をテスト
     */
    @Test
    void testUpdateTechnology() {
        System.out.println("\n=== Technology Update Test ===");

        // 事前データ作成
        Technology technology = new Technology(null, "React", TechnologyCategory.FRAMEWORK, "UIライブラリ");
        technologyMapper.insert(technology);
        Long insertedId = technology.getId();

        // Update
        Technology toUpdate = technologyMapper.selectById(insertedId);
        toUpdate.setName("React.js");
        toUpdate.setCategory(TechnologyCategory.FRAMEWORK);
        toUpdate.setDescription("Facebookが開発したユーザーインターフェース構築のためのJavaScriptライブラリ");
        technologyMapper.update(toUpdate);

        // 更新後の確認
        Technology updated = technologyMapper.selectById(insertedId);
        System.out.println("Updated: " + updated);

        // 検証
        assertNotNull(updated);
        assertEquals("React.js", updated.getName());
        assertEquals(TechnologyCategory.FRAMEWORK, updated.getCategory());
        assertEquals("Facebookが開発したユーザーインターフェース構築のためのJavaScriptライブラリ", updated.getDescription());

        System.out.println("=== Technology Update Test Completed ===\n");
    }

    /**
     * Technologyの削除をテスト
     */
    @Test
    void testDeleteTechnology() {
        System.out.println("\n=== Technology Delete Test ===");

        // 事前データ作成
        Technology technology = new Technology(null, "Git", TechnologyCategory.TOOL, "バージョン管理システム");
        technologyMapper.insert(technology);
        Long insertedId = technology.getId();

        // Delete
        technologyMapper.deleteById(insertedId);
        System.out.println("Deleted technology with ID: " + insertedId);

        // 削除後の確認
        Technology deleted = technologyMapper.selectById(insertedId);
        System.out.println("After deletion: " + deleted);

        // 検証
        assertNull(deleted);

        System.out.println("=== Technology Delete Test Completed ===\n");
    }
}
