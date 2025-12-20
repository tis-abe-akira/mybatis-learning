package com.example.mybatislearning.service;

import com.example.mybatislearning.entity.Organization;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * OrganizationServiceの統合テスト
 * トランザクション管理とサービス層の動作を確認
 */
@SpringBootTest
@Transactional  // テスト終了後にロールバック
class OrganizationServiceTest {

    @Autowired
    private OrganizationService organizationService;

    @Test
    void testCreateOrganization() {
        // 新しいOrganizationを作成
        Organization org = new Organization(null, "Service Test Org", "Created via service");

        // サービス層経由でCREATE
        organizationService.createOrganization(org);

        // IDが自動採番されていることを確認
        assertNotNull(org.getId());
        System.out.println("Created via Service: " + org);
    }

    @Test
    void testGetOrganization() {
        // テストデータを作成
        Organization org = new Organization(null, "Get Test Org", "Description");
        organizationService.createOrganization(org);

        // サービス層経由でGET
        Organization retrieved = organizationService.getOrganization(org.getId());

        // 取得したデータを検証
        assertNotNull(retrieved);
        assertEquals(org.getId(), retrieved.getId());
        assertEquals("Get Test Org", retrieved.getName());
        System.out.println("Retrieved via Service: " + retrieved);
    }

    @Test
    void testGetAllOrganizations() {
        // サービス層経由でALL取得
        List<Organization> organizations = organizationService.getAllOrganizations();

        // リストが空でないことを確認
        assertNotNull(organizations);
        assertFalse(organizations.isEmpty());
        System.out.println("All Organizations via Service: " + organizations);
    }

    @Test
    void testUpdateOrganization() {
        // テストデータを作成
        Organization org = new Organization(null, "Update Test Org", "Original");
        organizationService.createOrganization(org);

        // UPDATEするデータを準備
        org.setName("Updated via Service");
        org.setDescription("Updated Description");

        // サービス層経由でUPDATE
        organizationService.updateOrganization(org);

        // 更新されたデータを取得
        Organization updated = organizationService.getOrganization(org.getId());

        // 更新内容を検証
        assertNotNull(updated);
        assertEquals("Updated via Service", updated.getName());
        assertEquals("Updated Description", updated.getDescription());
        System.out.println("Updated via Service: " + updated);
    }

    @Test
    void testDeleteOrganization() {
        // テストデータを作成
        Organization org = new Organization(null, "Delete Test Org", "To be deleted");
        organizationService.createOrganization(org);
        Long orgId = org.getId();

        // サービス層経由でDELETE
        organizationService.deleteOrganization(orgId);

        // 削除されたことを確認
        Organization deleted = organizationService.getOrganization(orgId);
        assertNull(deleted);
        System.out.println("Deleted via Service, ID: " + orgId);
    }
}
