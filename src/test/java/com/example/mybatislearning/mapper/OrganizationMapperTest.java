package com.example.mybatislearning.mapper;

import com.example.mybatislearning.entity.Organization;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * OrganizationMapper（XML）の統合テスト
 */
@SpringBootTest
class OrganizationMapperTest {

    @Autowired
    private OrganizationMapper organizationMapper;

    @Test
    void testInsert() {
        // 新しいOrganizationを作成
        Organization org = new Organization(null, "Test Organization", "Test Description");

        // INSERT実行
        organizationMapper.insert(org);

        // IDが自動採番されていることを確認
        assertNotNull(org.getId());
        System.out.println("Inserted: " + org);
    }

    @Test
    void testSelectById() {
        // テストデータ投入
        Organization org = new Organization(null, "Select Test Org", "Description for select");
        organizationMapper.insert(org);

        // SELECT実行
        Organization retrieved = organizationMapper.selectById(org.getId());

        // 取得したデータを検証
        assertNotNull(retrieved);
        assertEquals(org.getId(), retrieved.getId());
        assertEquals("Select Test Org", retrieved.getName());
        assertEquals("Description for select", retrieved.getDescription());
        System.out.println("Retrieved: " + retrieved);
    }

    @Test
    void testSelectAll() {
        // 初期データが存在することを確認（data.sqlで投入されたデータ）
        List<Organization> organizations = organizationMapper.selectAll();

        // リストが空でないことを確認
        assertNotNull(organizations);
        assertFalse(organizations.isEmpty());
        System.out.println("All Organizations: " + organizations);
    }

    @Test
    void testUpdate() {
        // テストデータ投入
        Organization org = new Organization(null, "Update Test Org", "Original Description");
        organizationMapper.insert(org);

        // UPDATEするデータを準備
        org.setName("Updated Organization");
        org.setDescription("Updated Description");

        // UPDATE実行
        organizationMapper.update(org);

        // 更新されたデータを取得
        Organization updated = organizationMapper.selectById(org.getId());

        // 更新内容を検証
        assertNotNull(updated);
        assertEquals("Updated Organization", updated.getName());
        assertEquals("Updated Description", updated.getDescription());
        System.out.println("Updated: " + updated);
    }

    @Test
    void testDeleteById() {
        // テストデータ投入
        Organization org = new Organization(null, "Delete Test Org", "To be deleted");
        organizationMapper.insert(org);
        Long orgId = org.getId();

        // DELETE実行
        organizationMapper.deleteById(orgId);

        // 削除されたことを確認
        Organization deleted = organizationMapper.selectById(orgId);
        assertNull(deleted);
        System.out.println("Deleted organization with ID: " + orgId);
    }
}
