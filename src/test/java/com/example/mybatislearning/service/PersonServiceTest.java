package com.example.mybatislearning.service;

import com.example.mybatislearning.entity.Person;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * PersonServiceTest
 * PersonServiceのビジネスロジックとトランザクション管理をテスト
 */
@SpringBootTest
@Transactional
class PersonServiceTest {

    @Autowired
    private PersonService personService;

    /**
     * Personの作成をテスト
     */
    @Test
    void testCreatePerson() {
        System.out.println("\n=== PersonService Create Test ===");

        // Create
        Person person = new Person(null, "テスト太郎", "test@example.com", "エンジニア", "開発部");
        personService.createPerson(person);
        System.out.println("Created: " + person);

        // 検証
        assertNotNull(person.getId());
        assertTrue(person.getId() > 0);

        System.out.println("=== PersonService Create Test Completed ===\n");
    }

    /**
     * PersonのfindById検索をテスト
     */
    @Test
    void testFindPersonById() {
        System.out.println("\n=== PersonService FindById Test ===");

        // 事前データ作成
        Person person = new Person(null, "テスト花子", "hanako@example.com", "PM", "管理部");
        personService.createPerson(person);
        Long createdId = person.getId();

        // Find by ID
        Person found = personService.findById(createdId);
        System.out.println("Found: " + found);

        // 検証
        assertNotNull(found);
        assertEquals("テスト花子", found.getName());
        assertEquals("hanako@example.com", found.getEmail());

        System.out.println("=== PersonService FindById Test Completed ===\n");
    }

    /**
     * PersonのfindAll検索をテスト
     */
    @Test
    void testFindAllPersons() {
        System.out.println("\n=== PersonService FindAll Test ===");

        // 事前データ作成
        Person person1 = new Person(null, "テスト一郎", "test1@example.com", "開発者", "開発部");
        Person person2 = new Person(null, "テスト二郎", "test2@example.com", "テスター", "QA部");
        personService.createPerson(person1);
        personService.createPerson(person2);

        // Find All
        List<Person> allPersons = personService.findAll();
        System.out.println("All Persons: " + allPersons.size() + " records");
        for (Person p : allPersons) {
            System.out.println("  - " + p);
        }

        // 検証
        assertNotNull(allPersons);
        assertTrue(allPersons.size() >= 2);

        System.out.println("=== PersonService FindAll Test Completed ===\n");
    }

    /**
     * PersonのfindByEmail検索をテスト
     */
    @Test
    void testFindPersonByEmail() {
        System.out.println("\n=== PersonService FindByEmail Test ===");

        // 事前データ作成
        Person person = new Person(null, "テスト三郎", "test3@example.com", "デザイナー", "デザイン部");
        personService.createPerson(person);

        // Find by Email
        Person found = personService.findByEmail("test3@example.com");
        System.out.println("Found by Email: " + found);

        // 検証
        assertNotNull(found);
        assertEquals("テスト三郎", found.getName());

        // 存在しないメールアドレスで検索
        Person notFound = personService.findByEmail("nonexistent@example.com");
        assertNull(notFound);

        System.out.println("=== PersonService FindByEmail Test Completed ===\n");
    }

    /**
     * Personの更新をテスト
     */
    @Test
    void testUpdatePerson() {
        System.out.println("\n=== PersonService Update Test ===");

        // 事前データ作成
        Person person = new Person(null, "テスト四郎", "test4@example.com", "アーキテクト", "技術部");
        personService.createPerson(person);
        Long createdId = person.getId();

        // Update
        Person toUpdate = personService.findById(createdId);
        toUpdate.setName("テスト四郎（更新後）");
        toUpdate.setEmail("test4_updated@example.com");
        toUpdate.setRole("シニアアーキテクト");
        personService.updatePerson(toUpdate);

        // 更新後の確認
        Person updated = personService.findById(createdId);
        System.out.println("Updated: " + updated);

        // 検証
        assertNotNull(updated);
        assertEquals("テスト四郎（更新後）", updated.getName());
        assertEquals("test4_updated@example.com", updated.getEmail());
        assertEquals("シニアアーキテクト", updated.getRole());

        System.out.println("=== PersonService Update Test Completed ===\n");
    }

    /**
     * Personの削除をテスト
     */
    @Test
    void testDeletePerson() {
        System.out.println("\n=== PersonService Delete Test ===");

        // 事前データ作成
        Person person = new Person(null, "テスト五郎", "test5@example.com", "コンサルタント", "営業部");
        personService.createPerson(person);
        Long createdId = person.getId();

        // Delete
        personService.deletePerson(createdId);
        System.out.println("Deleted person with ID: " + createdId);

        // 削除後の確認
        Person deleted = personService.findById(createdId);

        // 検証
        assertNull(deleted);

        System.out.println("=== PersonService Delete Test Completed ===\n");
    }

    /**
     * Email形式検証をテスト（有効なメールアドレス）
     */
    @Test
    void testEmailValidation_ValidEmail() {
        System.out.println("\n=== PersonService Email Validation Test (Valid) ===");

        // 有効なメールアドレス
        Person person = new Person(null, "検証テスト", "valid@example.com", "エンジニア", "開発部");

        // 例外が発生しないことを確認
        assertDoesNotThrow(() -> personService.createPerson(person));
        assertNotNull(person.getId());
        System.out.println("Valid email accepted: " + person.getEmail());

        System.out.println("=== PersonService Email Validation Test (Valid) Completed ===\n");
    }

    /**
     * Email形式検証をテスト（無効なメールアドレス）
     */
    @Test
    void testEmailValidation_InvalidEmail() {
        System.out.println("\n=== PersonService Email Validation Test (Invalid) ===");

        // 無効なメールアドレス
        Person person = new Person(null, "検証テスト", "invalid-email", "エンジニア", "開発部");

        // 例外が発生することを確認
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            personService.createPerson(person);
        });

        System.out.println("Invalid email rejected: " + person.getEmail());
        System.out.println("Exception message: " + exception.getMessage());
        assertTrue(exception.getMessage().contains("メールアドレス") || exception.getMessage().contains("email"));

        System.out.println("=== PersonService Email Validation Test (Invalid) Completed ===\n");
    }

    /**
     * Email形式検証をテスト（nullメールアドレス）
     */
    @Test
    void testEmailValidation_NullEmail() {
        System.out.println("\n=== PersonService Email Validation Test (Null) ===");

        // nullメールアドレス（emailはNOT NULL制約があるため、DB制約違反となる）
        Person person = new Person(null, "検証テスト", null, "エンジニア", "開発部");

        // DB制約違反の例外が発生することを確認
        Exception exception = assertThrows(Exception.class, () -> {
            personService.createPerson(person);
        });

        System.out.println("Null email rejected by database constraint");
        System.out.println("Exception: " + exception.getClass().getSimpleName());

        System.out.println("=== PersonService Email Validation Test (Null) Completed ===\n");
    }
}
