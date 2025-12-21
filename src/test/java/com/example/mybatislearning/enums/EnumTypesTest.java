package com.example.mybatislearning.enums;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Enum型の定義を検証するテスト（タスク2.1～2.6）
 */
class EnumTypesTest {

    @Test
    void technologyCategoryEnumExists() {
        // TechnologyCategory Enumが6種類の値を持つことを確認
        Class<?> enumClass = null;
        try {
            enumClass = Class.forName("com.example.mybatislearning.enums.TechnologyCategory");
        } catch (ClassNotFoundException e) {
            fail("TechnologyCategory Enumが存在しません");
        }

        assertTrue(enumClass.isEnum(), "TechnologyCategoryはEnum型である必要があります");
        Object[] values = enumClass.getEnumConstants();
        assertEquals(6, values.length, "TechnologyCategoryは6種類の値を持つ必要があります");

        // 各値が正しく定義されていることを確認
        String[] expectedValues = {"LANGUAGE", "FRAMEWORK", "DATABASE", "INFRASTRUCTURE", "TOOL", "OTHER"};
        for (String expected : expectedValues) {
            boolean found = false;
            for (Object value : values) {
                if (value.toString().equals(expected)) {
                    found = true;
                    break;
                }
            }
            assertTrue(found, "TechnologyCategoryに" + expected + "が定義されていません");
        }
    }

    @Test
    void projectTypeEnumExists() {
        // ProjectType Enumが5種類の値を持つことを確認
        Class<?> enumClass = null;
        try {
            enumClass = Class.forName("com.example.mybatislearning.enums.ProjectType");
        } catch (ClassNotFoundException e) {
            fail("ProjectType Enumが存在しません");
        }

        assertTrue(enumClass.isEnum(), "ProjectTypeはEnum型である必要があります");
        Object[] values = enumClass.getEnumConstants();
        assertEquals(5, values.length, "ProjectTypeは5種類の値を持つ必要があります");

        String[] expectedValues = {"NEW_DEVELOPMENT", "MAINTENANCE", "CONSULTING", "PACKAGE", "IN_HOUSE_SERVICE"};
        for (String expected : expectedValues) {
            boolean found = false;
            for (Object value : values) {
                if (value.toString().equals(expected)) {
                    found = true;
                    break;
                }
            }
            assertTrue(found, "ProjectTypeに" + expected + "が定義されていません");
        }
    }

    @Test
    void projectStatusEnumExists() {
        // ProjectStatus Enumが4種類の値を持つことを確認
        Class<?> enumClass = null;
        try {
            enumClass = Class.forName("com.example.mybatislearning.enums.ProjectStatus");
        } catch (ClassNotFoundException e) {
            fail("ProjectStatus Enumが存在しません");
        }

        assertTrue(enumClass.isEnum(), "ProjectStatusはEnum型である必要があります");
        Object[] values = enumClass.getEnumConstants();
        assertEquals(4, values.length, "ProjectStatusは4種類の値を持つ必要があります");

        String[] expectedValues = {"PLANNING", "IN_PROGRESS", "COMPLETED", "CANCELLED"};
        for (String expected : expectedValues) {
            boolean found = false;
            for (Object value : values) {
                if (value.toString().equals(expected)) {
                    found = true;
                    break;
                }
            }
            assertTrue(found, "ProjectStatusに" + expected + "が定義されていません");
        }
    }

    @Test
    void memberRoleEnumExists() {
        // MemberRole Enumが7種類の値を持つことを確認
        Class<?> enumClass = null;
        try {
            enumClass = Class.forName("com.example.mybatislearning.enums.MemberRole");
        } catch (ClassNotFoundException e) {
            fail("MemberRole Enumが存在しません");
        }

        assertTrue(enumClass.isEnum(), "MemberRoleはEnum型である必要があります");
        Object[] values = enumClass.getEnumConstants();
        assertEquals(7, values.length, "MemberRoleは7種類の値を持つ必要があります");

        String[] expectedValues = {"PROJECT_MANAGER", "TECHNICAL_LEAD", "DEVELOPER", "TESTER", "DESIGNER", "ADVISOR", "OTHER"};
        for (String expected : expectedValues) {
            boolean found = false;
            for (Object value : values) {
                if (value.toString().equals(expected)) {
                    found = true;
                    break;
                }
            }
            assertTrue(found, "MemberRoleに" + expected + "が定義されていません");
        }
    }

    @Test
    void phaseTypeEnumExists() {
        // PhaseType Enumが5種類の値を持つことを確認
        Class<?> enumClass = null;
        try {
            enumClass = Class.forName("com.example.mybatislearning.enums.PhaseType");
        } catch (ClassNotFoundException e) {
            fail("PhaseType Enumが存在しません");
        }

        assertTrue(enumClass.isEnum(), "PhaseTypeはEnum型である必要があります");
        Object[] values = enumClass.getEnumConstants();
        assertEquals(5, values.length, "PhaseTypeは5種類の値を持つ必要があります");

        String[] expectedValues = {"REQUIREMENTS", "DESIGN", "IMPLEMENTATION", "TESTING", "RELEASE"};
        for (String expected : expectedValues) {
            boolean found = false;
            for (Object value : values) {
                if (value.toString().equals(expected)) {
                    found = true;
                    break;
                }
            }
            assertTrue(found, "PhaseTypeに" + expected + "が定義されていません");
        }
    }

    @Test
    void phaseStatusEnumExists() {
        // PhaseStatus Enumが3種類の値を持つことを確認
        Class<?> enumClass = null;
        try {
            enumClass = Class.forName("com.example.mybatislearning.enums.PhaseStatus");
        } catch (ClassNotFoundException e) {
            fail("PhaseStatus Enumが存在しません");
        }

        assertTrue(enumClass.isEnum(), "PhaseStatusはEnum型である必要があります");
        Object[] values = enumClass.getEnumConstants();
        assertEquals(3, values.length, "PhaseStatusは3種類の値を持つ必要があります");

        String[] expectedValues = {"NOT_STARTED", "IN_PROGRESS", "COMPLETED"};
        for (String expected : expectedValues) {
            boolean found = false;
            for (Object value : values) {
                if (value.toString().equals(expected)) {
                    found = true;
                    break;
                }
            }
            assertTrue(found, "PhaseStatusに" + expected + "が定義されていません");
        }
    }
}
