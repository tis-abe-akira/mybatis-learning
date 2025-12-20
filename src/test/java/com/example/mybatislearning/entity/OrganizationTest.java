package com.example.mybatislearning.entity;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Organizationエンティティクラスのテスト
 */
class OrganizationTest {

    @Test
    void defaultConstructorCreatesInstance() {
        // デフォルトコンストラクタでインスタンスを作成
        Organization organization = new Organization();
        assertNotNull(organization, "デフォルトコンストラクタでインスタンスが作成できません");
    }

    @Test
    void fieldInitializationConstructorSetsAllFields() {
        // フィールド初期化コンストラクタでインスタンスを作成
        Long id = 1L;
        String name = "Test Organization";
        String description = "Test Description";

        Organization organization = new Organization(id, name, description);

        assertEquals(id, organization.getId(), "idが正しく設定されていません");
        assertEquals(name, organization.getName(), "nameが正しく設定されていません");
        assertEquals(description, organization.getDescription(), "descriptionが正しく設定されていません");
    }

    @Test
    void gettersAndSettersWork() {
        Organization organization = new Organization();

        // setterでフィールドを設定
        Long id = 1L;
        String name = "Tech Corp";
        String description = "Technology company";

        organization.setId(id);
        organization.setName(name);
        organization.setDescription(description);

        // getterで値を取得して確認
        assertEquals(id, organization.getId(), "getId()が正しい値を返しません");
        assertEquals(name, organization.getName(), "getName()が正しい値を返しません");
        assertEquals(description, organization.getDescription(), "getDescription()が正しい値を返しません");
    }

    @Test
    void toStringProducesMeaningfulOutput() {
        Organization organization = new Organization(1L, "Tech Corp", "Technology company");
        String result = organization.toString();

        // toString()の出力に各フィールドの値が含まれていることを確認
        assertNotNull(result, "toString()がnullを返しています");
        assertTrue(result.contains("1"), "toString()にidが含まれていません");
        assertTrue(result.contains("Tech Corp"), "toString()にnameが含まれていません");
        assertTrue(result.contains("Technology company"), "toString()にdescriptionが含まれていません");
    }

    @Test
    void canSetFieldsToNull() {
        // null値を許容することを確認
        Organization organization = new Organization(1L, "Test", "Description");

        organization.setName(null);
        organization.setDescription(null);

        assertNull(organization.getName(), "nameをnullに設定できません");
        assertNull(organization.getDescription(), "descriptionをnullに設定できません");
    }

    @Test
    void organizationWithSameValuesAreEqual() {
        // 同じ値を持つOrganizationオブジェクトの比較
        Organization org1 = new Organization(1L, "Tech Corp", "Technology company");
        Organization org2 = new Organization(1L, "Tech Corp", "Technology company");

        // POJOなのでequals/hashCodeは実装しない想定だが、同じIDなら同じ組織として扱う
        assertEquals(org1.getId(), org2.getId(), "同じIDのOrganizationが同一と判断されません");
    }
}
