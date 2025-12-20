package com.example.mybatislearning;

import com.example.mybatislearning.entity.Organization;
import com.example.mybatislearning.entity.Project;
import com.example.mybatislearning.mapper.OrganizationAnnotationMapper;
import com.example.mybatislearning.service.OrganizationService;
import com.example.mybatislearning.service.ProjectService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * MyBatisApplicationTests
 * MyBatis統合テストとCRUD操作の動作確認
 * データ取得後の標準出力によるデータ確認
 */
@SpringBootTest
class MyBatisApplicationTests {

    @Autowired
    private OrganizationService organizationService;

    @Autowired
    private ProjectService projectService;

    @Autowired
    private OrganizationAnnotationMapper organizationAnnotationMapper;

    /**
     * OrganizationのCRUD操作をテスト
     */
    @Test
    void testOrganizationCrud() {
        System.out.println("\n=== Organization CRUD Test ===");

        // Create
        Organization org = new Organization(null, "Test Organization", "Test Description");
        organizationService.createOrganization(org);
        System.out.println("Created: " + org);
        assertNotNull(org.getId());

        // Read
        Organization retrieved = organizationService.getOrganization(org.getId());
        System.out.println("Retrieved: " + retrieved);
        assertNotNull(retrieved);
        assertEquals("Test Organization", retrieved.getName());

        // Update
        retrieved.setName("Updated Organization");
        retrieved.setDescription("Updated Description");
        organizationService.updateOrganization(retrieved);
        Organization updated = organizationService.getOrganization(org.getId());
        System.out.println("Updated: " + updated);
        assertEquals("Updated Organization", updated.getName());

        // Delete
        organizationService.deleteOrganization(retrieved.getId());
        Organization deleted = organizationService.getOrganization(retrieved.getId());
        System.out.println("Deleted organization with ID: " + retrieved.getId());
        assertNull(deleted);

        System.out.println("=== Organization CRUD Test Completed ===\n");
    }

    /**
     * ProjectのCRUD操作をテスト
     */
    @Test
    void testProjectCrud() {
        System.out.println("\n=== Project CRUD Test ===");

        // Create
        Project project = new Project(null, "Test Project", 1L);
        projectService.createProject(project);
        System.out.println("Created: " + project);
        assertNotNull(project.getId());

        // Read
        Project retrieved = projectService.getProject(project.getId());
        System.out.println("Retrieved: " + retrieved);
        assertNotNull(retrieved);
        assertEquals("Test Project", retrieved.getProjectName());

        // Update
        retrieved.setProjectName("Updated Project");
        retrieved.setOrganizationId(2L);
        projectService.updateProject(retrieved);
        Project updated = projectService.getProject(project.getId());
        System.out.println("Updated: " + updated);
        assertEquals("Updated Project", updated.getProjectName());
        assertEquals(2L, updated.getOrganizationId());

        // Delete
        projectService.deleteProject(retrieved.getId());
        Project deleted = projectService.getProject(retrieved.getId());
        System.out.println("Deleted project with ID: " + retrieved.getId());
        assertNull(deleted);

        System.out.println("=== Project CRUD Test Completed ===\n");
    }

    /**
     * ProjectとOrganizationのリレーションシップ取得をテスト
     */
    @Test
    void testProjectWithOrganization() {
        System.out.println("\n=== Project with Organization Test ===");

        // data.sqlで投入されたProject（ID=1）を取得
        Project project = projectService.getProjectWithOrganization(1L);
        System.out.println("Project with Organization: " + project);
        System.out.println("Organization Details: " + project.getOrganization());

        // 検証
        assertNotNull(project);
        assertNotNull(project.getOrganization());
        assertEquals(project.getOrganizationId(), project.getOrganization().getId());

        System.out.println("=== Project with Organization Test Completed ===\n");
    }

    /**
     * 組織別Project検索をテスト
     */
    @Test
    void testProjectsByOrganization() {
        System.out.println("\n=== Projects by Organization Test ===");

        // 組織ID=1のProjectを取得
        List<Project> projects = projectService.getProjectsByOrganization(1L);
        System.out.println("Projects for Organization 1:");
        for (Project p : projects) {
            System.out.println("  - " + p);
        }

        // 検証
        assertNotNull(projects);
        assertFalse(projects.isEmpty());
        for (Project p : projects) {
            assertEquals(1L, p.getOrganizationId());
        }

        System.out.println("=== Projects by Organization Test Completed ===\n");
    }

    /**
     * アノテーションマッパーのCRUD操作をテスト
     * XMLマッパーとの違いを確認
     */
    @Test
    void testOrganizationAnnotationMapper() {
        System.out.println("\n=== Organization Annotation Mapper Test ===");

        // Create
        Organization org = new Organization(null, "Annotation Org", "Created via annotation");
        organizationAnnotationMapper.insert(org);
        System.out.println("Created (Annotation): " + org);
        assertNotNull(org.getId());

        // Read
        Organization retrieved = organizationAnnotationMapper.selectById(org.getId());
        System.out.println("Retrieved (Annotation): " + retrieved);
        assertNotNull(retrieved);
        assertEquals("Annotation Org", retrieved.getName());

        // Read All
        List<Organization> all = organizationAnnotationMapper.selectAll();
        System.out.println("All Organizations (Annotation): " + all.size() + " records");
        assertFalse(all.isEmpty());

        // Update
        retrieved.setName("Updated Annotation Org");
        organizationAnnotationMapper.update(retrieved);
        Organization updated = organizationAnnotationMapper.selectById(org.getId());
        System.out.println("Updated (Annotation): " + updated);
        assertEquals("Updated Annotation Org", updated.getName());

        // Delete
        organizationAnnotationMapper.deleteById(retrieved.getId());
        Organization deleted = organizationAnnotationMapper.selectById(retrieved.getId());
        System.out.println("Deleted organization (Annotation) with ID: " + retrieved.getId());
        assertNull(deleted);

        System.out.println("=== Organization Annotation Mapper Test Completed ===\n");
    }

    /**
     * H2インメモリデータベースとSQLログ出力の確認
     */
    @Test
    void testDatabaseAndSqlLogging() {
        System.out.println("\n=== Database and SQL Logging Test ===");

        // 初期データの確認（data.sqlで投入されたデータ）
        List<Organization> organizations = organizationService.getAllOrganizations();
        System.out.println("Initial Organizations:");
        for (Organization org : organizations) {
            System.out.println("  - " + org);
        }

        List<Project> projects = projectService.getAllProjects();
        System.out.println("Initial Projects:");
        for (Project p : projects) {
            System.out.println("  - " + p);
        }

        // 検証
        assertNotNull(organizations);
        assertNotNull(projects);
        assertFalse(organizations.isEmpty());
        assertFalse(projects.isEmpty());

        System.out.println("SQL logs are automatically output via MyBatis configuration");
        System.out.println("=== Database and SQL Logging Test Completed ===\n");
    }
}
