package com.example.mybatislearning.service;

import com.example.mybatislearning.entity.Industry;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * IndustryServiceTest
 * IndustryServiceのビジネスロジックとトランザクション管理をテスト
 */
@SpringBootTest
@Transactional
class IndustryServiceTest {

    @Autowired
    private IndustryService industryService;

    /**
     * Industryの作成をテスト
     */
    @Test
    void testCreateIndustry() {
        System.out.println("\n=== IndustryService Create Test ===");

        // Create
        Industry industry = new Industry(null, "金融", "銀行、証券、保険などの金融業界");
        industryService.createIndustry(industry);
        System.out.println("Created: " + industry);

        // 検証
        assertNotNull(industry.getId());
        assertTrue(industry.getId() > 0);

        System.out.println("=== IndustryService Create Test Completed ===\n");
    }

    /**
     * IndustrのfindById検索をテスト
     */
    @Test
    void testFindIndustryById() {
        System.out.println("\n=== IndustryService FindById Test ===");

        // 事前データ作成
        Industry industry = new Industry(null, "製造", "自動車、電機、機械などの製造業界");
        industryService.createIndustry(industry);
        Long createdId = industry.getId();

        // Find by ID
        Industry found = industryService.findById(createdId);
        System.out.println("Found: " + found);

        // 検証
        assertNotNull(found);
        assertEquals("製造", found.getName());
        assertEquals("自動車、電機、機械などの製造業界", found.getDescription());

        System.out.println("=== IndustryService FindById Test Completed ===\n");
    }

    /**
     * IndustryのfindAll検索をテスト
     */
    @Test
    void testFindAllIndustries() {
        System.out.println("\n=== IndustryService FindAll Test ===");

        // 事前データ作成
        Industry industry1 = new Industry(null, "小売", "百貨店、スーパー、ECなどの小売業界");
        Industry industry2 = new Industry(null, "通信", "通信キャリア、ISPなどの通信業界");
        industryService.createIndustry(industry1);
        industryService.createIndustry(industry2);

        // Find All
        List<Industry> allIndustries = industryService.findAll();
        System.out.println("All Industries: " + allIndustries.size() + " records");
        for (Industry i : allIndustries) {
            System.out.println("  - " + i);
        }

        // 検証
        assertNotNull(allIndustries);
        assertTrue(allIndustries.size() >= 2);

        System.out.println("=== IndustryService FindAll Test Completed ===\n");
    }

    /**
     * IndustryのfindByName検索をテスト
     */
    @Test
    void testFindIndustryByName() {
        System.out.println("\n=== IndustryService FindByName Test ===");

        // 事前データ作成
        Industry industry = new Industry(null, "医療", "病院、製薬、医療機器などの医療業界");
        industryService.createIndustry(industry);

        // Find by Name
        Industry found = industryService.findByName("医療");
        System.out.println("Found: " + found);

        // 検証
        assertNotNull(found);
        assertEquals("医療", found.getName());

        System.out.println("=== IndustryService FindByName Test Completed ===\n");
    }

    /**
     * Industryの更新をテスト
     */
    @Test
    void testUpdateIndustry() {
        System.out.println("\n=== IndustryService Update Test ===");

        // 事前データ作成
        Industry industry = new Industry(null, "IT", "ソフトウェア、インターネットサービスなどのIT業界");
        industryService.createIndustry(industry);
        Long createdId = industry.getId();

        // Update
        Industry toUpdate = industryService.findById(createdId);
        toUpdate.setName("情報技術");
        toUpdate.setDescription("ソフトウェア開発、クラウドサービス、AI・機械学習などの情報技術業界");
        industryService.updateIndustry(toUpdate);

        // 更新後の確認
        Industry updated = industryService.findById(createdId);
        System.out.println("Updated: " + updated);

        // 検証
        assertNotNull(updated);
        assertEquals("情報技術", updated.getName());
        assertEquals("ソフトウェア開発、クラウドサービス、AI・機械学習などの情報技術業界", updated.getDescription());

        System.out.println("=== IndustryService Update Test Completed ===\n");
    }

    /**
     * Industryの削除をテスト
     */
    @Test
    void testDeleteIndustry() {
        System.out.println("\n=== IndustryService Delete Test ===");

        // 事前データ作成
        Industry industry = new Industry(null, "教育", "学校、予備校、eラーニングなどの教育業界");
        industryService.createIndustry(industry);
        Long createdId = industry.getId();

        // Delete
        industryService.deleteIndustry(createdId);
        System.out.println("Deleted industry with ID: " + createdId);

        // 削除後の確認
        Industry deleted = industryService.findById(createdId);
        System.out.println("After deletion: " + deleted);

        // 検証
        assertNull(deleted);

        System.out.println("=== IndustryService Delete Test Completed ===\n");
    }
}
