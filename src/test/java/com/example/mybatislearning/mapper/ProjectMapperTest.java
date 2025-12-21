package com.example.mybatislearning.mapper;

import com.example.mybatislearning.entity.Industry;
import com.example.mybatislearning.entity.Person;
import com.example.mybatislearning.entity.Project;
import com.example.mybatislearning.enums.ProjectStatus;
import com.example.mybatislearning.enums.ProjectType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * ProjectMapper（XML）の統合テスト（Phase 3拡張版）
 */
@SpringBootTest
@Transactional
class ProjectMapperTest {

    @Autowired
    private ProjectMapper projectMapper;

    @Autowired
    private IndustryMapper industryMapper;

    @Autowired
    private PersonMapper personMapper;

    @Test
    void testInsert() {
        // 新しいProjectを作成（organizationId=1を使用）
        Project project = new Project(null, "New Test Project", 1L);

        // INSERT実行
        projectMapper.insert(project);

        // IDが自動採番されていることを確認
        assertNotNull(project.getId());
        System.out.println("Inserted: " + project);
    }

    @Test
    void testSelectById() {
        // テストデータを投入
        Project project = new Project(null, "Select Test Project", 1L);
        projectMapper.insert(project);

        // SELECT実行
        Project retrieved = projectMapper.selectById(project.getId());

        // 取得したデータを検証
        assertNotNull(retrieved);
        assertEquals(project.getId(), retrieved.getId());
        assertEquals("Select Test Project", retrieved.getProjectName());
        assertEquals(1L, retrieved.getOrganizationId());
        System.out.println("Retrieved: " + retrieved);
    }

    @Test
    void testSelectAll() {
        // 初期データが存在することを確認（data.sqlで投入されたデータ）
        List<Project> projects = projectMapper.selectAll();

        // リストが空でないことを確認
        assertNotNull(projects);
        assertFalse(projects.isEmpty());
        System.out.println("All Projects: " + projects);
    }

    @Test
    void testSelectByOrganizationId() {
        // 組織ID=1のProjectを検索
        List<Project> projects = projectMapper.selectByOrganizationId(1L);

        // リストが空でないことを確認
        assertNotNull(projects);
        assertFalse(projects.isEmpty());

        // すべてのProjectがorganizationId=1であることを確認
        for (Project p : projects) {
            assertEquals(1L, p.getOrganizationId());
        }
        System.out.println("Projects for Organization 1: " + projects);
    }

    @Test
    void testSelectProjectWithOrganization() {
        // data.sqlで投入されたProject（ID=1）を取得
        Project project = projectMapper.selectProjectWithOrganization(1L);

        // Projectとそれに関連するOrganizationが取得されることを確認
        assertNotNull(project);
        assertNotNull(project.getOrganization());
        assertEquals(1L, project.getOrganizationId());
        assertEquals(project.getOrganization().getId(), project.getOrganizationId());

        System.out.println("Project with Organization: " + project);
        System.out.println("Organization Details: " + project.getOrganization());
    }

    @Test
    void testUpdate() {
        // テストデータを投入
        Project project = new Project(null, "Update Test Project", 1L);
        projectMapper.insert(project);

        // UPDATEするデータを準備
        project.setProjectName("Updated Project Name");
        project.setOrganizationId(2L);

        // UPDATE実行
        projectMapper.update(project);

        // 更新されたデータを取得
        Project updated = projectMapper.selectById(project.getId());

        // 更新内容を検証
        assertNotNull(updated);
        assertEquals("Updated Project Name", updated.getProjectName());
        assertEquals(2L, updated.getOrganizationId());
        System.out.println("Updated: " + updated);
    }

    @Test
    void testDeleteById() {
        // テストデータを投入
        Project project = new Project(null, "Delete Test Project", 1L);
        projectMapper.insert(project);
        Long projectId = project.getId();

        // DELETE実行
        projectMapper.deleteById(projectId);

        // 削除されたことを確認
        Project deleted = projectMapper.selectById(projectId);
        assertNull(deleted);
        System.out.println("Deleted project with ID: " + projectId);
    }

    // ========== Phase 3拡張メソッドのテスト ==========

    @Test
    void testInsertWithExtendedFields() {
        System.out.println("\n=== Project Insert with Extended Fields Test ===");

        // 拡張フィールドを含むProjectを作成
        Project project = new Project(null, "Extended Project", 1L, "ACME Corp",
                null, ProjectType.NEW_DEVELOPMENT, ProjectStatus.PLANNING);
        project.setBudget(new BigDecimal("10000000"));
        project.setPersonMonths(new BigDecimal("12.5"));
        project.setTeamSize(5);
        project.setPlannedStartDate(LocalDate.of(2025, 4, 1));
        project.setPlannedEndDate(LocalDate.of(2025, 12, 31));

        // INSERT実行
        projectMapper.insert(project);

        // 取得して検証
        Project retrieved = projectMapper.selectById(project.getId());
        assertNotNull(retrieved);
        assertEquals("Extended Project", retrieved.getProjectName());
        assertEquals("ACME Corp", retrieved.getCustomerName());
        assertEquals(ProjectType.NEW_DEVELOPMENT, retrieved.getProjectType());
        assertEquals(ProjectStatus.PLANNING, retrieved.getStatus());
        assertEquals(0, new BigDecimal("10000000").compareTo(retrieved.getBudget()));
        assertEquals(5, retrieved.getTeamSize());

        System.out.println("Created with extended fields: " + retrieved);
        System.out.println("=== Test Completed ===\n");
    }

    @Test
    void testFindByStatus() {
        System.out.println("\n=== Project FindByStatus Test ===");

        // テストデータ作成
        Project project1 = new Project(null, "Active Project 1", 1L, "Client A",
                null, ProjectType.NEW_DEVELOPMENT, ProjectStatus.IN_PROGRESS);
        Project project2 = new Project(null, "Active Project 2", 1L, "Client B",
                null, ProjectType.MAINTENANCE, ProjectStatus.IN_PROGRESS);
        Project project3 = new Project(null, "Completed Project", 1L, "Client C",
                null, ProjectType.CONSULTING, ProjectStatus.COMPLETED);

        projectMapper.insert(project1);
        projectMapper.insert(project2);
        projectMapper.insert(project3);

        // IN_PROGRESSのProjectを検索
        List<Project> inProgressProjects = projectMapper.findByStatus(ProjectStatus.IN_PROGRESS);
        assertNotNull(inProgressProjects);
        assertTrue(inProgressProjects.size() >= 2);
        for (Project p : inProgressProjects) {
            assertEquals(ProjectStatus.IN_PROGRESS, p.getStatus());
            System.out.println("  - " + p);
        }

        System.out.println("=== Test Completed ===\n");
    }

    @Test
    void testFindByIndustryId() {
        System.out.println("\n=== Project FindByIndustryId Test ===");

        // Industry作成
        Industry industry = new Industry(null, "IT", "情報技術業界");
        industryMapper.insert(industry);

        // テストデータ作成
        Project project1 = new Project(null, "IT Project 1", 1L, "Tech Corp",
                industry.getId(), ProjectType.NEW_DEVELOPMENT, ProjectStatus.PLANNING);
        Project project2 = new Project(null, "IT Project 2", 1L, "StartUp Inc",
                industry.getId(), ProjectType.IN_HOUSE_SERVICE, ProjectStatus.IN_PROGRESS);

        projectMapper.insert(project1);
        projectMapper.insert(project2);

        // 業界IDで検索
        List<Project> projects = projectMapper.findByIndustryId(industry.getId());
        assertNotNull(projects);
        assertTrue(projects.size() >= 2);
        for (Project p : projects) {
            assertEquals(industry.getId(), p.getIndustryId());
            System.out.println("  - " + p);
        }

        System.out.println("=== Test Completed ===\n");
    }

    @Test
    void testFindByProjectType() {
        System.out.println("\n=== Project FindByProjectType Test ===");

        // テストデータ作成
        Project project1 = new Project(null, "New Dev 1", 1L, "Client X",
                null, ProjectType.NEW_DEVELOPMENT, ProjectStatus.PLANNING);
        Project project2 = new Project(null, "New Dev 2", 1L, "Client Y",
                null, ProjectType.NEW_DEVELOPMENT, ProjectStatus.IN_PROGRESS);
        Project project3 = new Project(null, "Maintenance", 1L, "Client Z",
                null, ProjectType.MAINTENANCE, ProjectStatus.IN_PROGRESS);

        projectMapper.insert(project1);
        projectMapper.insert(project2);
        projectMapper.insert(project3);

        // NEW_DEVELOPMENTのProjectを検索
        List<Project> newDevProjects = projectMapper.findByProjectType(ProjectType.NEW_DEVELOPMENT);
        assertNotNull(newDevProjects);
        assertTrue(newDevProjects.size() >= 2);
        for (Project p : newDevProjects) {
            assertEquals(ProjectType.NEW_DEVELOPMENT, p.getProjectType());
            System.out.println("  - " + p);
        }

        System.out.println("=== Test Completed ===\n");
    }

    @Test
    void testFindByProjectManagerId() {
        System.out.println("\n=== Project FindByProjectManagerId Test ===");

        // Person（PM）作成
        Person pm = new Person(null, "山田太郎", "yamada@example.com", "PM", "管理部");
        personMapper.insert(pm);

        // テストデータ作成
        Project project1 = new Project(null, "PM Project 1", 1L);
        project1.setProjectManagerId(pm.getId());
        project1.setStatus(ProjectStatus.PLANNING);
        Project project2 = new Project(null, "PM Project 2", 1L);
        project2.setProjectManagerId(pm.getId());
        project2.setStatus(ProjectStatus.IN_PROGRESS);

        projectMapper.insert(project1);
        projectMapper.insert(project2);

        // PMのIDで検索
        List<Project> projects = projectMapper.findByProjectManagerId(pm.getId());
        assertNotNull(projects);
        assertTrue(projects.size() >= 2);
        for (Project p : projects) {
            assertEquals(pm.getId(), p.getProjectManagerId());
            System.out.println("  - " + p);
        }

        System.out.println("=== Test Completed ===\n");
    }

    @Test
    void testFindByTechnicalLeadId() {
        System.out.println("\n=== Project FindByTechnicalLeadId Test ===");

        // Person（TL）作成
        Person tl = new Person(null, "佐藤花子", "sato@example.com", "Tech Lead", "開発部");
        personMapper.insert(tl);

        // テストデータ作成
        Project project1 = new Project(null, "TL Project 1", 1L);
        project1.setTechnicalLeadId(tl.getId());
        project1.setStatus(ProjectStatus.IN_PROGRESS);
        Project project2 = new Project(null, "TL Project 2", 1L);
        project2.setTechnicalLeadId(tl.getId());
        project2.setStatus(ProjectStatus.COMPLETED);

        projectMapper.insert(project1);
        projectMapper.insert(project2);

        // TLのIDで検索
        List<Project> projects = projectMapper.findByTechnicalLeadId(tl.getId());
        assertNotNull(projects);
        assertTrue(projects.size() >= 2);
        for (Project p : projects) {
            assertEquals(tl.getId(), p.getTechnicalLeadId());
            System.out.println("  - " + p);
        }

        System.out.println("=== Test Completed ===\n");
    }

    @Test
    void testSelectProjectWithAllRelations() {
        System.out.println("\n=== Project SelectProjectWithAllRelations Test ===");

        // 関連エンティティを作成
        Industry industry = new Industry(null, "金融", "銀行・証券業界");
        industryMapper.insert(industry);

        Person pm = new Person(null, "田中一郎", "tanaka@example.com", "PM", "プロジェクト管理部");
        personMapper.insert(pm);

        Person tl = new Person(null, "鈴木次郎", "suzuki@example.com", "Tech Lead", "技術部");
        personMapper.insert(tl);

        // Projectを作成
        Project project = new Project(null, "Banking System", 1L, "Mega Bank",
                industry.getId(), ProjectType.NEW_DEVELOPMENT, ProjectStatus.IN_PROGRESS);
        project.setBudget(new BigDecimal("50000000"));
        project.setPersonMonths(new BigDecimal("24.0"));
        project.setTeamSize(10);
        project.setProjectManagerId(pm.getId());
        project.setTechnicalLeadId(tl.getId());
        projectMapper.insert(project);

        // 全リレーションシップを含めて取得
        Project retrieved = projectMapper.selectProjectWithAllRelations(project.getId());

        // 検証
        assertNotNull(retrieved);
        assertEquals("Banking System", retrieved.getProjectName());

        // Industry association
        assertNotNull(retrieved.getIndustry());
        assertEquals("金融", retrieved.getIndustry().getName());
        System.out.println("Industry: " + retrieved.getIndustry());

        // ProjectManager association
        assertNotNull(retrieved.getProjectManager());
        assertEquals("田中一郎", retrieved.getProjectManager().getName());
        System.out.println("Project Manager: " + retrieved.getProjectManager());

        // TechnicalLead association
        assertNotNull(retrieved.getTechnicalLead());
        assertEquals("鈴木次郎", retrieved.getTechnicalLead().getName());
        System.out.println("Technical Lead: " + retrieved.getTechnicalLead());

        System.out.println("Full Project Details: " + retrieved);
        System.out.println("=== Test Completed ===\n");
    }
}
