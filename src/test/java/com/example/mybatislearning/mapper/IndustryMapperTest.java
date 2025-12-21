package com.example.mybatislearning.mapper;

import com.example.mybatislearning.entity.Industry;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * IndustryMapperTest
 * IndustryMapperのCRUD操作をテスト
 */
@SpringBootTest
@Transactional
class IndustryMapperTest {

    @Autowired
    private IndustryMapper industryMapper;

    /**
     * Industryの作成をテスト
     */
    @Test
    void testInsertIndustry() {
        System.out.println("\n=== Industry Insert Test ===");

        // Create
        Industry industry = new Industry(null, "金融", "銀行、証券、保険などの金融業界");
        industryMapper.insert(industry);
        System.out.println("Created: " + industry);

        // 検証: IDが自動採番されること
        assertNotNull(industry.getId());
        assertTrue(industry.getId() > 0);

        System.out.println("=== Industry Insert Test Completed ===\n");
    }

    /**
     * IndustryのselectById検索をテスト
     */
    @Test
    void testSelectIndustryById() {
        System.out.println("\n=== Industry SelectById Test ===");

        // 事前データ作成
        Industry industry = new Industry(null, "製造", "自動車、電機、機械などの製造業界");
        industryMapper.insert(industry);
        Long insertedId = industry.getId();

        // Read
        Industry retrieved = industryMapper.selectById(insertedId);
        System.out.println("Retrieved: " + retrieved);

        // 検証
        assertNotNull(retrieved);
        assertEquals("製造", retrieved.getName());
        assertEquals("自動車、電機、機械などの製造業界", retrieved.getDescription());
        assertNotNull(retrieved.getCreatedAt());
        assertNotNull(retrieved.getUpdatedAt());

        System.out.println("=== Industry SelectById Test Completed ===\n");
    }

    /**
     * IndustryのselectAll検索をテスト
     */
    @Test
    void testSelectAllIndustries() {
        System.out.println("\n=== Industry SelectAll Test ===");

        // 事前データ作成
        Industry industry1 = new Industry(null, "小売", "百貨店、スーパー、ECなどの小売業界");
        Industry industry2 = new Industry(null, "通信", "通信キャリア、ISPなどの通信業界");
        industryMapper.insert(industry1);
        industryMapper.insert(industry2);

        // Read All
        List<Industry> allIndustries = industryMapper.selectAll();
        System.out.println("All Industries: " + allIndustries.size() + " records");
        for (Industry i : allIndustries) {
            System.out.println("  - " + i);
        }

        // 検証
        assertNotNull(allIndustries);
        assertTrue(allIndustries.size() >= 2);

        System.out.println("=== Industry SelectAll Test Completed ===\n");
    }

    /**
     * IndustryのfindByName検索をテスト
     */
    @Test
    void testFindIndustryByName() {
        System.out.println("\n=== Industry FindByName Test ===");

        // 事前データ作成
        Industry industry = new Industry(null, "医療", "病院、製薬、医療機器などの医療業界");
        industryMapper.insert(industry);

        // Find by Name
        Industry found = industryMapper.findByName("医療");
        System.out.println("Found by Name: " + found);

        // 検証
        assertNotNull(found);
        assertEquals("医療", found.getName());
        assertEquals("病院、製薬、医療機器などの医療業界", found.getDescription());

        // 存在しない業界名で検索
        Industry notFound = industryMapper.findByName("存在しない業界");
        System.out.println("Not Found by Name: " + notFound);
        assertNull(notFound);

        System.out.println("=== Industry FindByName Test Completed ===\n");
    }

    /**
     * Industryの更新をテスト
     */
    @Test
    void testUpdateIndustry() {
        System.out.println("\n=== Industry Update Test ===");

        // 事前データ作成
        Industry industry = new Industry(null, "IT", "ソフトウェア、インターネットサービスなどのIT業界");
        industryMapper.insert(industry);
        Long insertedId = industry.getId();

        // Update
        Industry toUpdate = industryMapper.selectById(insertedId);
        toUpdate.setName("情報技術");
        toUpdate.setDescription("ソフトウェア開発、クラウドサービス、AI・機械学習などの情報技術業界");
        industryMapper.update(toUpdate);

        // 更新後の確認
        Industry updated = industryMapper.selectById(insertedId);
        System.out.println("Updated: " + updated);

        // 検証
        assertNotNull(updated);
        assertEquals("情報技術", updated.getName());
        assertEquals("ソフトウェア開発、クラウドサービス、AI・機械学習などの情報技術業界", updated.getDescription());

        System.out.println("=== Industry Update Test Completed ===\n");
    }

    /**
     * Industryの削除をテスト
     */
    @Test
    void testDeleteIndustry() {
        System.out.println("\n=== Industry Delete Test ===");

        // 事前データ作成
        Industry industry = new Industry(null, "教育", "学校、予備校、eラーニングなどの教育業界");
        industryMapper.insert(industry);
        Long insertedId = industry.getId();

        // Delete
        industryMapper.deleteById(insertedId);
        System.out.println("Deleted industry with ID: " + insertedId);

        // 削除後の確認
        Industry deleted = industryMapper.selectById(insertedId);
        System.out.println("After deletion: " + deleted);

        // 検証
        assertNull(deleted);

        System.out.println("=== Industry Delete Test Completed ===\n");
    }
}
