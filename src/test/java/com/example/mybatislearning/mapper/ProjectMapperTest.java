package com.example.mybatislearning.mapper;

import com.example.mybatislearning.entity.Project;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * ProjectMapper（XML）の統合テスト
 */
@SpringBootTest
class ProjectMapperTest {

    @Autowired
    private ProjectMapper projectMapper;

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
}
