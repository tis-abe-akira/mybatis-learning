package com.example.mybatislearning.entity;

/**
 * Organization（組織）エンティティクラス
 * データベースのorganizationsテーブルに対応するPOJO
 */
public class Organization {

    private Long id;
    private String name;
    private String description;

    /**
     * デフォルトコンストラクタ
     */
    public Organization() {
    }

    /**
     * フィールド初期化コンストラクタ
     *
     * @param id          組織ID
     * @param name        組織名
     * @param description 組織の説明
     */
    public Organization(Long id, String name, String description) {
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

    /**
     * データ出力用のtoString()メソッド
     *
     * @return 組織情報の文字列表現
     */
    @Override
    public String toString() {
        return "Organization{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
