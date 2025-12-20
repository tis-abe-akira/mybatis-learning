package com.example.mybatislearning.service;

import com.example.mybatislearning.entity.Project;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * ProjectServiceの統合テスト
 * トランザクション管理とサービス層の動作を確認
 */
@SpringBootTest
@Transactional  // テスト終了後にロールバック
class ProjectServiceTest {

    @Autowired
    private ProjectService projectService;

    @Test
    void testCreateProject() {
        // 新しいProjectを作成
        Project project = new Project(null, "Service Test Project", 1L);

        // サービス層経由でCREATE
        projectService.createProject(project);

        // IDが自動採番されていることを確認
        assertNotNull(project.getId());
        System.out.println("Created via Service: " + project);
    }

    @Test
    void testGetProject() {
        // テストデータを作成
        Project project = new Project(null, "Get Test Project", 1L);
        projectService.createProject(project);

        // サービス層経由でGET
        Project retrieved = projectService.getProject(project.getId());

        // 取得したデータを検証
        assertNotNull(retrieved);
        assertEquals(project.getId(), retrieved.getId());
        assertEquals("Get Test Project", retrieved.getProjectName());
        System.out.println("Retrieved via Service: " + retrieved);
    }

    @Test
    void testGetProjectWithOrganization() {
        // data.sqlで投入されたProject（ID=1）を取得
        Project project = projectService.getProjectWithOrganization(1L);

        // ProjectとそれにAssociateされたOrganizationが取得されることを確認
        assertNotNull(project);
        assertNotNull(project.getOrganization());
        assertEquals(1L, project.getOrganizationId());
        assertEquals(project.getOrganization().getId(), project.getOrganizationId());

        System.out.println("Project with Organization via Service: " + project);
        System.out.println("Organization Details: " + project.getOrganization());
    }

    @Test
    void testGetProjectsByOrganization() {
        // 組織ID=1のProjectを取得
        List<Project> projects = projectService.getProjectsByOrganization(1L);

        // リストが空でないことを確認
        assertNotNull(projects);
        assertFalse(projects.isEmpty());

        // すべてのProjectがorganizationId=1であることを確認
        for (Project p : projects) {
            assertEquals(1L, p.getOrganizationId());
        }
        System.out.println("Projects for Organization 1 via Service: " + projects);
    }

    @Test
    void testGetAllProjects() {
        // サービス層経由でALL取得
        List<Project> projects = projectService.getAllProjects();

        // リストが空でないことを確認
        assertNotNull(projects);
        assertFalse(projects.isEmpty());
        System.out.println("All Projects via Service: " + projects);
    }

    @Test
    void testUpdateProject() {
        // テストデータを作成
        Project project = new Project(null, "Update Test Project", 1L);
        projectService.createProject(project);

        // UPDATEするデータを準備
        project.setProjectName("Updated Project via Service");
        project.setOrganizationId(2L);

        // サービス層経由でUPDATE
        projectService.updateProject(project);

        // 更新されたデータを取得
        Project updated = projectService.getProject(project.getId());

        // 更新内容を検証
        assertNotNull(updated);
        assertEquals("Updated Project via Service", updated.getProjectName());
        assertEquals(2L, updated.getOrganizationId());
        System.out.println("Updated via Service: " + updated);
    }

    @Test
    void testDeleteProject() {
        // テストデータを作成
        Project project = new Project(null, "Delete Test Project", 1L);
        projectService.createProject(project);
        Long projectId = project.getId();

        // サービス層経由でDELETE
        projectService.deleteProject(projectId);

        // 削除されたことを確認
        Project deleted = projectService.getProject(projectId);
        assertNull(deleted);
        System.out.println("Deleted via Service, ID: " + projectId);
    }
}
