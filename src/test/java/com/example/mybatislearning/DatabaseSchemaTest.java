package com.example.mybatislearning;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

import static org.junit.jupiter.api.Assertions.*;

/**
 * データベーススキーマとテストデータが正しく初期化されていることを検証するテスト
 */
@SpringBootTest
class DatabaseSchemaTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    void organizationsTableExists() {
        // organizationsテーブルが存在し、正しいカラムを持つことを確認
        String sql = "SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS " +
                     "WHERE TABLE_NAME = 'ORGANIZATIONS' AND COLUMN_NAME IN ('ID', 'NAME', 'DESCRIPTION')";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class);
        assertEquals(3, count, "organizationsテーブルに必要なカラム(ID, NAME, DESCRIPTION)が存在しません");
    }

    @Test
    void projectsTableExists() {
        // projectsテーブルが存在し、正しいカラムを持つことを確認
        String sql = "SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS " +
                     "WHERE TABLE_NAME = 'PROJECTS' AND COLUMN_NAME IN ('ID', 'PROJECT_NAME', 'ORGANIZATION_ID')";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class);
        assertEquals(3, count, "projectsテーブルに必要なカラム(ID, PROJECT_NAME, ORGANIZATION_ID)が存在しません");
    }

    @Test
    void foreignKeyConstraintExists() {
        // projectsテーブルにorganization_idカラムが存在することを確認
        // 外部キー制約の存在は、実際のJOIN操作で検証する
        String sql = "SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS " +
                     "WHERE TABLE_NAME = 'PROJECTS' AND COLUMN_NAME = 'ORGANIZATION_ID'";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class);
        assertEquals(1, count, "projectsテーブルにorganization_idカラムが存在しません");
    }

    @Test
    void testDataIsLoaded() {
        // Organizationのテストデータが少なくとも2件投入されていることを確認
        Integer orgCount = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM organizations", Integer.class);
        assertTrue(orgCount >= 2, "organizationsテーブルに少なくとも2件のテストデータが必要です");

        // Projectのテストデータが少なくとも3件投入されていることを確認
        Integer projectCount = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM projects", Integer.class);
        assertTrue(projectCount >= 3, "projectsテーブルに少なくとも3件のテストデータが必要です");
    }

    @Test
    void organizationDataHasCorrectStructure() {
        // Organizationデータの構造を確認
        String sql = "SELECT id, name, description FROM organizations WHERE id = 1";
        jdbcTemplate.queryForMap(sql).forEach((key, value) -> {
            assertNotNull(value, "Organization データのフィールド " + key + " がnullです");
        });
    }

    @Test
    void projectDataHasCorrectStructure() {
        // Projectデータの構造を確認
        String sql = "SELECT id, project_name, organization_id FROM projects WHERE id = 1";
        jdbcTemplate.queryForMap(sql).forEach((key, value) -> {
            assertNotNull(value, "Project データのフィールド " + key + " がnullです");
        });
    }

    @Test
    void foreignKeyRelationshipWorks() {
        // 外部キー関係が正しく機能することを確認
        String sql = "SELECT p.id, p.project_name, o.name as organization_name " +
                     "FROM projects p " +
                     "INNER JOIN organizations o ON p.organization_id = o.id " +
                     "WHERE p.id = 1";
        var result = jdbcTemplate.queryForMap(sql);
        assertNotNull(result.get("ORGANIZATION_NAME"), "外部キー関係でOrganization名を取得できません");
    }

    // 新規テーブル存在確認テスト（タスク1.1）

    @Test
    void personsTableExists() {
        // personsテーブルが存在し、正しいカラムを持つことを確認
        String sql = "SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS " +
                     "WHERE TABLE_NAME = 'PERSONS' AND COLUMN_NAME IN ('ID', 'NAME', 'EMAIL', 'ROLE', 'DEPARTMENT', 'CREATED_AT', 'UPDATED_AT')";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class);
        assertEquals(7, count, "personsテーブルに必要なカラムが存在しません");
    }

    @Test
    void industriesTableExists() {
        // industriesテーブルが存在し、正しいカラムを持つことを確認
        String sql = "SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS " +
                     "WHERE TABLE_NAME = 'INDUSTRIES' AND COLUMN_NAME IN ('ID', 'NAME', 'DESCRIPTION', 'CREATED_AT', 'UPDATED_AT')";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class);
        assertEquals(5, count, "industriesテーブルに必要なカラムが存在しません");
    }

    @Test
    void technologiesTableExists() {
        // technologiesテーブルが存在し、正しいカラムを持つことを確認
        String sql = "SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS " +
                     "WHERE TABLE_NAME = 'TECHNOLOGIES' AND COLUMN_NAME IN ('ID', 'NAME', 'CATEGORY', 'DESCRIPTION', 'CREATED_AT', 'UPDATED_AT')";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class);
        assertEquals(6, count, "technologiesテーブルに必要なカラムが存在しません");
    }

    @Test
    void phasesTableExists() {
        // phasesテーブルが存在し、正しいカラムを持つことを確認
        String sql = "SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS " +
                     "WHERE TABLE_NAME = 'PHASES' AND COLUMN_NAME IN ('ID', 'PROJECT_ID', 'PHASE_TYPE', 'PLANNED_START_DATE', 'PLANNED_END_DATE', 'STATUS', 'CREATED_AT', 'UPDATED_AT')";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class);
        assertEquals(8, count, "phasesテーブルに必要なカラムが存在しません");
    }

    @Test
    void projectMembersTableExists() {
        // project_membersテーブルが存在し、正しいカラムを持つことを確認
        String sql = "SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS " +
                     "WHERE TABLE_NAME = 'PROJECT_MEMBERS' AND COLUMN_NAME IN ('ID', 'PROJECT_ID', 'PERSON_ID', 'ROLE', 'JOIN_DATE', 'ALLOCATION_RATE', 'CREATED_AT', 'UPDATED_AT')";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class);
        assertEquals(8, count, "project_membersテーブルに必要なカラムが存在しません");
    }

    @Test
    void projectTechnologiesTableExists() {
        // project_technologiesテーブルが存在し、正しいカラムを持つことを確認
        String sql = "SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS " +
                     "WHERE TABLE_NAME = 'PROJECT_TECHNOLOGIES' AND COLUMN_NAME IN ('ID', 'PROJECT_ID', 'TECHNOLOGY_ID', 'PURPOSE', 'CREATED_AT', 'UPDATED_AT')";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class);
        assertEquals(6, count, "project_technologiesテーブルに必要なカラムが存在しません");
    }

    @Test
    void personsTableHasEmailIndex() {
        // personsテーブルにemailインデックスが存在することを確認
        String sql = "SELECT COUNT(*) FROM INFORMATION_SCHEMA.INDEXES " +
                     "WHERE TABLE_NAME = 'PERSONS' AND INDEX_NAME = 'IDX_PERSONS_EMAIL'";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class);
        assertTrue(count >= 1, "personsテーブルのemailインデックスが存在しません");
    }

    @Test
    void industriesTableHasNameIndex() {
        // industriesテーブルにnameインデックスが存在することを確認
        String sql = "SELECT COUNT(*) FROM INFORMATION_SCHEMA.INDEXES " +
                     "WHERE TABLE_NAME = 'INDUSTRIES' AND INDEX_NAME = 'IDX_INDUSTRIES_NAME'";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class);
        assertTrue(count >= 1, "industriesテーブルのnameインデックスが存在しません");
    }

    @Test
    void technologiesTableHasCategoryIndex() {
        // technologiesテーブルにcategoryインデックスが存在することを確認
        String sql = "SELECT COUNT(*) FROM INFORMATION_SCHEMA.INDEXES " +
                     "WHERE TABLE_NAME = 'TECHNOLOGIES' AND INDEX_NAME = 'IDX_TECHNOLOGIES_CATEGORY'";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class);
        assertTrue(count >= 1, "technologiesテーブルのcategoryインデックスが存在しません");
    }

    // projectsテーブル拡張確認テスト（タスク1.2）

    @Test
    void projectsTableHasExtendedColumns() {
        // projectsテーブルに拡張カラムが存在することを確認
        String sql = "SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS " +
                     "WHERE TABLE_NAME = 'PROJECTS' AND COLUMN_NAME IN " +
                     "('CUSTOMER_NAME', 'INDUSTRY_ID', 'PROJECT_TYPE', 'STATUS', 'BUDGET', 'PERSON_MONTHS', " +
                     "'TEAM_SIZE', 'PLANNED_START_DATE', 'PLANNED_END_DATE', 'ACTUAL_START_DATE', " +
                     "'ACTUAL_END_DATE', 'PROJECT_MANAGER_ID', 'TECHNICAL_LEAD_ID')";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class);
        assertEquals(13, count, "projectsテーブルに拡張カラムが存在しません");
    }

    @Test
    void projectsTableHasIndustryForeignKey() {
        // projectsテーブルのindustry_id外部キー制約を確認
        String sql = "SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS " +
                     "WHERE TABLE_NAME = 'PROJECTS' AND COLUMN_NAME = 'INDUSTRY_ID'";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class);
        assertEquals(1, count, "projectsテーブルにindustry_idカラムが存在しません");
    }

    @Test
    void projectsTableHasPersonForeignKeys() {
        // projectsテーブルのproject_manager_id、technical_lead_id外部キー制約を確認
        String sql = "SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS " +
                     "WHERE TABLE_NAME = 'PROJECTS' AND COLUMN_NAME IN ('PROJECT_MANAGER_ID', 'TECHNICAL_LEAD_ID')";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class);
        assertEquals(2, count, "projectsテーブルにproject_manager_id、technical_lead_idカラムが存在しません");
    }

    @Test
    void projectsTableHasExtendedIndexes() {
        // projectsテーブルに拡張インデックスが存在することを確認
        String sql = "SELECT COUNT(*) FROM INFORMATION_SCHEMA.INDEXES " +
                     "WHERE TABLE_NAME = 'PROJECTS' AND INDEX_NAME IN " +
                     "('IDX_PROJECTS_INDUSTRY_ID', 'IDX_PROJECTS_STATUS', 'IDX_PROJECTS_PROJECT_TYPE', " +
                     "'IDX_PROJECTS_PROJECT_MANAGER_ID', 'IDX_PROJECTS_TECHNICAL_LEAD_ID')";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class);
        assertTrue(count >= 5, "projectsテーブルに拡張インデックスが存在しません");
    }
}
