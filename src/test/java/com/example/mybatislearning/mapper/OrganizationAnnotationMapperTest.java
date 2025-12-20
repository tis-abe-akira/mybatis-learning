package com.example.mybatislearning.mapper;

import com.example.mybatislearning.entity.Organization;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * OrganizationAnnotationMapper（アノテーション）の統合テスト
 * XMLマッパーとの違いを確認するためのテスト
 */
@SpringBootTest
class OrganizationAnnotationMapperTest {

    @Autowired
    private OrganizationAnnotationMapper organizationAnnotationMapper;

    @Test
    void testInsert() {
        // 新しいOrganizationを作成
        Organization org = new Organization(null, "Annotation Test Org", "Created via annotation mapper");

        // INSERT実行
        organizationAnnotationMapper.insert(org);

        // IDが自動採番されていることを確認
        assertNotNull(org.getId());
        System.out.println("Inserted (Annotation): " + org);
    }

    @Test
    void testSelectById() {
        // テストデータ投入
        Organization org = new Organization(null, "Annotation Select Test", "Description");
        organizationAnnotationMapper.insert(org);

        // SELECT実行
        Organization retrieved = organizationAnnotationMapper.selectById(org.getId());

        // 取得したデータを検証
        assertNotNull(retrieved);
        assertEquals(org.getId(), retrieved.getId());
        assertEquals("Annotation Select Test", retrieved.getName());
        System.out.println("Retrieved (Annotation): " + retrieved);
    }

    @Test
    void testSelectAll() {
        // 初期データが存在することを確認
        List<Organization> organizations = organizationAnnotationMapper.selectAll();

        // リストが空でないことを確認
        assertNotNull(organizations);
        assertFalse(organizations.isEmpty());
        System.out.println("All Organizations (Annotation): " + organizations);
    }

    @Test
    void testUpdate() {
        // テストデータ投入
        Organization org = new Organization(null, "Annotation Update Test", "Original");
        organizationAnnotationMapper.insert(org);

        // UPDATEするデータを準備
        org.setName("Updated via Annotation");
        org.setDescription("Updated Description");

        // UPDATE実行
        organizationAnnotationMapper.update(org);

        // 更新されたデータを取得
        Organization updated = organizationAnnotationMapper.selectById(org.getId());

        // 更新内容を検証
        assertNotNull(updated);
        assertEquals("Updated via Annotation", updated.getName());
        assertEquals("Updated Description", updated.getDescription());
        System.out.println("Updated (Annotation): " + updated);
    }

    @Test
    void testDeleteById() {
        // テストデータ投入
        Organization org = new Organization(null, "Annotation Delete Test", "To be deleted");
        organizationAnnotationMapper.insert(org);
        Long orgId = org.getId();

        // DELETE実行
        organizationAnnotationMapper.deleteById(orgId);

        // 削除されたことを確認
        Organization deleted = organizationAnnotationMapper.selectById(orgId);
        assertNull(deleted);
        System.out.println("Deleted organization (Annotation) with ID: " + orgId);
    }
}
