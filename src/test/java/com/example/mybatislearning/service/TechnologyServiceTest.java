package com.example.mybatislearning.service;

import com.example.mybatislearning.entity.Technology;
import com.example.mybatislearning.enums.TechnologyCategory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * TechnologyServiceTest
 * TechnologyServiceのビジネスロジックとトランザクション管理をテスト
 */
@SpringBootTest
@Transactional
class TechnologyServiceTest {

    @Autowired
    private TechnologyService technologyService;

    /**
     * Technologyの作成をテスト
     */
    @Test
    void testCreateTechnology() {
        System.out.println("\n=== TechnologyService Create Test ===");

        // Create
        Technology technology = new Technology(null, "Java", TechnologyCategory.LANGUAGE, "オブジェクト指向プログラミング言語");
        technologyService.createTechnology(technology);
        System.out.println("Created: " + technology);

        // 検証
        assertNotNull(technology.getId());
        assertTrue(technology.getId() > 0);

        System.out.println("=== TechnologyService Create Test Completed ===\n");
    }

    /**
     * TechnologyのfindById検索をテスト
     */
    @Test
    void testFindTechnologyById() {
        System.out.println("\n=== TechnologyService FindById Test ===");

        // 事前データ作成
        Technology technology = new Technology(null, "Spring Boot", TechnologyCategory.FRAMEWORK, "Javaアプリケーションフレームワーク");
        technologyService.createTechnology(technology);
        Long createdId = technology.getId();

        // Find by ID
        Technology found = technologyService.findById(createdId);
        System.out.println("Found: " + found);

        // 検証
        assertNotNull(found);
        assertEquals("Spring Boot", found.getName());
        assertEquals(TechnologyCategory.FRAMEWORK, found.getCategory());

        System.out.println("=== TechnologyService FindById Test Completed ===\n");
    }

    /**
     * TechnologyのfindAll検索をテスト
     */
    @Test
    void testFindAllTechnologies() {
        System.out.println("\n=== TechnologyService FindAll Test ===");

        // 事前データ作成
        Technology tech1 = new Technology(null, "PostgreSQL", TechnologyCategory.DATABASE, "オープンソースリレーショナルデータベース");
        Technology tech2 = new Technology(null, "Docker", TechnologyCategory.INFRASTRUCTURE, "コンテナ仮想化プラットフォーム");
        technologyService.createTechnology(tech1);
        technologyService.createTechnology(tech2);

        // Find All
        List<Technology> allTechnologies = technologyService.findAll();
        System.out.println("All Technologies: " + allTechnologies.size() + " records");
        for (Technology t : allTechnologies) {
            System.out.println("  - " + t);
        }

        // 検証
        assertNotNull(allTechnologies);
        assertTrue(allTechnologies.size() >= 2);

        System.out.println("=== TechnologyService FindAll Test Completed ===\n");
    }

    /**
     * TechnologyのfindByCategory検索をテスト
     */
    @Test
    void testFindTechnologyByCategory() {
        System.out.println("\n=== TechnologyService FindByCategory Test ===");

        // 事前データ作成
        Technology lang1 = new Technology(null, "Python", TechnologyCategory.LANGUAGE, "汎用プログラミング言語");
        Technology lang2 = new Technology(null, "JavaScript", TechnologyCategory.LANGUAGE, "Web開発言語");
        Technology db = new Technology(null, "MySQL", TechnologyCategory.DATABASE, "オープンソースRDB");
        technologyService.createTechnology(lang1);
        technologyService.createTechnology(lang2);
        technologyService.createTechnology(db);

        // Find by Category
        List<Technology> languages = technologyService.findByCategory(TechnologyCategory.LANGUAGE);
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

        System.out.println("=== TechnologyService FindByCategory Test Completed ===\n");
    }

    /**
     * Technologyの更新をテスト
     */
    @Test
    void testUpdateTechnology() {
        System.out.println("\n=== TechnologyService Update Test ===");

        // 事前データ作成
        Technology technology = new Technology(null, "React", TechnologyCategory.FRAMEWORK, "UIライブラリ");
        technologyService.createTechnology(technology);
        Long createdId = technology.getId();

        // Update
        Technology toUpdate = technologyService.findById(createdId);
        toUpdate.setName("React.js");
        toUpdate.setCategory(TechnologyCategory.FRAMEWORK);
        toUpdate.setDescription("Facebookが開発したユーザーインターフェース構築のためのJavaScriptライブラリ");
        technologyService.updateTechnology(toUpdate);

        // 更新後の確認
        Technology updated = technologyService.findById(createdId);
        System.out.println("Updated: " + updated);

        // 検証
        assertNotNull(updated);
        assertEquals("React.js", updated.getName());
        assertEquals(TechnologyCategory.FRAMEWORK, updated.getCategory());

        System.out.println("=== TechnologyService Update Test Completed ===\n");
    }

    /**
     * Technologyの削除をテスト
     */
    @Test
    void testDeleteTechnology() {
        System.out.println("\n=== TechnologyService Delete Test ===");

        // 事前データ作成
        Technology technology = new Technology(null, "Git", TechnologyCategory.TOOL, "バージョン管理システム");
        technologyService.createTechnology(technology);
        Long createdId = technology.getId();

        // Delete
        technologyService.deleteTechnology(createdId);
        System.out.println("Deleted technology with ID: " + createdId);

        // 削除後の確認
        Technology deleted = technologyService.findById(createdId);
        System.out.println("After deletion: " + deleted);

        // 検証
        assertNull(deleted);

        System.out.println("=== TechnologyService Delete Test Completed ===\n");
    }
}
