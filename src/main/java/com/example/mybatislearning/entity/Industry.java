package com.example.mybatislearning.entity;

import java.time.LocalDateTime;

/**
 * Industry（業界）エンティティクラス
 * データベースのindustriesテーブルに対応するPOJO
 * 顧客業界マスタを表す
 */
public class Industry {

    private Long id;
    private String name;
    private String description;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    /**
     * デフォルトコンストラクタ
     */
    public Industry() {
    }

    /**
     * フィールド初期化コンストラクタ
     *
     * @param id          業界ID
     * @param name        業界名
     * @param description 業界説明
     */
    public Industry(Long id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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
     * @return 業界情報の文字列表現
     */
    @Override
    public String toString() {
        return "Industry{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
