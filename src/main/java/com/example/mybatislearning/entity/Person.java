package com.example.mybatislearning.entity;

import java.time.LocalDateTime;

/**
 * Person（人物）エンティティクラス
 * データベースのpersonsテーブルに対応するPOJO
 * プロジェクトメンバーや責任者を表す
 */
public class Person {

    private Long id;
    private String name;
    private String email;
    private String role;
    private String department;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    /**
     * デフォルトコンストラクタ
     */
    public Person() {
    }

    /**
     * フィールド初期化コンストラクタ
     *
     * @param id         人物ID
     * @param name       氏名
     * @param email      メールアドレス
     * @param role       役職
     * @param department 部署
     */
    public Person(Long id, String name, String email, String role, String department) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.role = role;
        this.department = department;
    }

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    /**
     * データ出力用のtoString()メソッド
     *
     * @return 人物情報の文字列表現
     */
    @Override
    public String toString() {
        return "Person{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", role='" + role + '\'' +
                ", department='" + department + '\'' +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
