package com.example.mybatislearning.entity;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Projectエンティティクラスのテスト
 */
class ProjectTest {

    @Test
    void defaultConstructorCreatesInstance() {
        // デフォルトコンストラクタでインスタンスを作成
        Project project = new Project();
        assertNotNull(project, "デフォルトコンストラクタでインスタンスが作成できません");
    }

    @Test
    void fieldInitializationConstructorSetsAllFields() {
        // フィールド初期化コンストラクタでインスタンスを作成
        Long id = 1L;
        String projectName = "Test Project";
        Long organizationId = 2L;

        Project project = new Project(id, projectName, organizationId);

        assertEquals(id, project.getId(), "idが正しく設定されていません");
        assertEquals(projectName, project.getProjectName(), "projectNameが正しく設定されていません");
        assertEquals(organizationId, project.getOrganizationId(), "organizationIdが正しく設定されていません");
    }

    @Test
    void gettersAndSettersWork() {
        Project project = new Project();

        // setterでフィールドを設定
        Long id = 1L;
        String projectName = "Web Application";
        Long organizationId = 1L;

        project.setId(id);
        project.setProjectName(projectName);
        project.setOrganizationId(organizationId);

        // getterで値を取得して確認
        assertEquals(id, project.getId(), "getId()が正しい値を返しません");
        assertEquals(projectName, project.getProjectName(), "getProjectName()が正しい値を返しません");
        assertEquals(organizationId, project.getOrganizationId(), "getOrganizationId()が正しい値を返しません");
    }

    @Test
    void organizationRelationshipFieldWorks() {
        // Organizationオブジェクトを保持するフィールドのテスト
        Project project = new Project();
        Organization organization = new Organization(1L, "Tech Corp", "Technology company");

        project.setOrganization(organization);

        assertNotNull(project.getOrganization(), "getOrganization()がnullを返しています");
        assertEquals(organization, project.getOrganization(), "setした組織が正しく取得できません");
        assertEquals("Tech Corp", project.getOrganization().getName(), "関連する組織の名前が取得できません");
    }

    @Test
    void toStringIncludesAllFieldsAndRelationship() {
        // リレーションシップなしの場合
        Project project1 = new Project(1L, "Web App", 1L);
        String result1 = project1.toString();

        assertNotNull(result1, "toString()がnullを返しています");
        assertTrue(result1.contains("1"), "toString()にidが含まれていません");
        assertTrue(result1.contains("Web App"), "toString()にprojectNameが含まれていません");
        assertTrue(result1.contains("1"), "toString()にorganizationIdが含まれていません");

        // リレーションシップありの場合
        Project project2 = new Project(2L, "Mobile App", 1L);
        Organization organization = new Organization(1L, "Tech Corp", "Technology company");
        project2.setOrganization(organization);
        String result2 = project2.toString();

        assertTrue(result2.contains("Tech Corp"), "toString()に関連する組織情報が含まれていません");
    }

    @Test
    void canSetFieldsToNull() {
        // null値を許容することを確認
        Project project = new Project(1L, "Test Project", 1L);

        project.setProjectName(null);
        project.setOrganization(null);

        assertNull(project.getProjectName(), "projectNameをnullに設定できません");
        assertNull(project.getOrganization(), "organizationをnullに設定できません");
    }

    @Test
    void projectWithOrganizationRelationship() {
        // ProjectとOrganizationのリレーションシップを確認
        Organization organization = new Organization(1L, "Tech Corp", "Technology company");
        Project project = new Project(1L, "Web Application", 1L);
        project.setOrganization(organization);

        // projectからorganizationにアクセスできることを確認
        assertEquals(organization.getId(), project.getOrganization().getId(),
                "関連する組織のIDが一致しません");
        assertEquals(organization.getName(), project.getOrganization().getName(),
                "関連する組織の名前が一致しません");
    }
}
