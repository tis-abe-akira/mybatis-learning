package com.example.mybatislearning.mapper;

import com.example.mybatislearning.entity.Person;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * PersonMapperTest
 * PersonMapperのCRUD操作をテスト
 */
@SpringBootTest
@Transactional
class PersonMapperTest {

    @Autowired
    private PersonMapper personMapper;

    /**
     * Personの作成をテスト
     */
    @Test
    void testInsertPerson() {
        System.out.println("\n=== Person Insert Test ===");

        // Create
        Person person = new Person(null, "田中太郎", "tanaka@example.com", "エンジニア", "開発部");
        personMapper.insert(person);
        System.out.println("Created: " + person);

        // 検証: IDが自動採番されること
        assertNotNull(person.getId());
        assertTrue(person.getId() > 0);

        System.out.println("=== Person Insert Test Completed ===\n");
    }

    /**
     * PersonのselectById検索をテスト
     */
    @Test
    void testSelectPersonById() {
        System.out.println("\n=== Person SelectById Test ===");

        // 事前データ作成
        Person person = new Person(null, "佐藤花子", "sato@example.com", "PM", "管理部");
        personMapper.insert(person);
        Long insertedId = person.getId();

        // Read
        Person retrieved = personMapper.selectById(insertedId);
        System.out.println("Retrieved: " + retrieved);

        // 検証
        assertNotNull(retrieved);
        assertEquals("佐藤花子", retrieved.getName());
        assertEquals("sato@example.com", retrieved.getEmail());
        assertEquals("PM", retrieved.getRole());
        assertEquals("管理部", retrieved.getDepartment());
        assertNotNull(retrieved.getCreatedAt());
        assertNotNull(retrieved.getUpdatedAt());

        System.out.println("=== Person SelectById Test Completed ===\n");
    }

    /**
     * PersonのselectAll検索をテスト
     */
    @Test
    void testSelectAllPersons() {
        System.out.println("\n=== Person SelectAll Test ===");

        // 事前データ作成
        Person person1 = new Person(null, "山田一郎", "yamada@example.com", "開発者", "開発部");
        Person person2 = new Person(null, "鈴木二郎", "suzuki@example.com", "テスター", "QA部");
        personMapper.insert(person1);
        personMapper.insert(person2);

        // Read All
        List<Person> allPersons = personMapper.selectAll();
        System.out.println("All Persons: " + allPersons.size() + " records");
        for (Person p : allPersons) {
            System.out.println("  - " + p);
        }

        // 検証
        assertNotNull(allPersons);
        assertTrue(allPersons.size() >= 2);

        System.out.println("=== Person SelectAll Test Completed ===\n");
    }

    /**
     * PersonのfindByEmail検索をテスト
     */
    @Test
    void testFindPersonByEmail() {
        System.out.println("\n=== Person FindByEmail Test ===");

        // 事前データ作成
        Person person = new Person(null, "高橋三郎", "takahashi@example.com", "デザイナー", "デザイン部");
        personMapper.insert(person);

        // Find by Email
        Person found = personMapper.findByEmail("takahashi@example.com");
        System.out.println("Found by Email: " + found);

        // 検証
        assertNotNull(found);
        assertEquals("高橋三郎", found.getName());
        assertEquals("takahashi@example.com", found.getEmail());

        // 存在しないメールアドレスで検索
        Person notFound = personMapper.findByEmail("nonexistent@example.com");
        System.out.println("Not Found by Email: " + notFound);
        assertNull(notFound);

        System.out.println("=== Person FindByEmail Test Completed ===\n");
    }

    /**
     * Personの更新をテスト
     */
    @Test
    void testUpdatePerson() {
        System.out.println("\n=== Person Update Test ===");

        // 事前データ作成
        Person person = new Person(null, "伊藤四郎", "ito@example.com", "アーキテクト", "技術部");
        personMapper.insert(person);
        Long insertedId = person.getId();

        // Update
        Person toUpdate = personMapper.selectById(insertedId);
        toUpdate.setName("伊藤四郎（更新後）");
        toUpdate.setEmail("ito_updated@example.com");
        toUpdate.setRole("シニアアーキテクト");
        toUpdate.setDepartment("技術統括部");
        personMapper.update(toUpdate);

        // 更新後の確認
        Person updated = personMapper.selectById(insertedId);
        System.out.println("Updated: " + updated);

        // 検証
        assertNotNull(updated);
        assertEquals("伊藤四郎（更新後）", updated.getName());
        assertEquals("ito_updated@example.com", updated.getEmail());
        assertEquals("シニアアーキテクト", updated.getRole());
        assertEquals("技術統括部", updated.getDepartment());

        System.out.println("=== Person Update Test Completed ===\n");
    }

    /**
     * Personの削除をテスト
     */
    @Test
    void testDeletePerson() {
        System.out.println("\n=== Person Delete Test ===");

        // 事前データ作成
        Person person = new Person(null, "渡辺五郎", "watanabe@example.com", "コンサルタント", "営��部");
        personMapper.insert(person);
        Long insertedId = person.getId();

        // Delete
        personMapper.deleteById(insertedId);
        System.out.println("Deleted person with ID: " + insertedId);

        // 削除後の確認
        Person deleted = personMapper.selectById(insertedId);
        System.out.println("After deletion: " + deleted);

        // 検証
        assertNull(deleted);

        System.out.println("=== Person Delete Test Completed ===\n");
    }
}
