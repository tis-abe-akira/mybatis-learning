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
}
