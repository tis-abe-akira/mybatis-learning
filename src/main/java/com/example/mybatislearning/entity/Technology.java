package com.example.mybatislearning.entity;

import com.example.mybatislearning.enums.TechnologyCategory;

import java.time.LocalDateTime;

/**
 * Technology（技術）エンティティクラス
 * データベースのtechnologiesテーブルに対応するPOJO
 * 技術マスタを表す
 */
public class Technology {

    private Long id;
    private String name;
    private TechnologyCategory category;
    private String description;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    /**
     * デフォルトコンストラクタ
     */
    public Technology() {
    }

    /**
     * フィールド初期化コンストラクタ
     *
     * @param id          技術ID
     * @param name        技術名
     * @param category    技術カテゴリ
     * @param description 技術説明
     */
    public Technology(Long id, String name, TechnologyCategory category, String description) {
        this.id = id;
        this.name = name;
        this.category = category;
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

    public TechnologyCategory getCategory() {
        return category;
    }

    public void setCategory(TechnologyCategory category) {
        this.category = category;
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
     * @return 技術情報の文字列表現
     */
    @Override
    public String toString() {
        return "Technology{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", category=" + category +
                ", description='" + description + '\'' +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
