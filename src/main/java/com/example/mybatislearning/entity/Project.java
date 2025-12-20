package com.example.mybatislearning.entity;

/**
 * Project（プロジェクト）エンティティクラス
 * データベースのprojectsテーブルに対応するPOJO
 */
public class Project {

    private Long id;
    private String projectName;
    private Long organizationId;

    /**
     * リレーションシップ用のフィールド
     * MyBatisのassociationマッピングで使用
     */
    private Organization organization;

    /**
     * デフォルトコンストラクタ
     */
    public Project() {
    }

    /**
     * フィールド初期化コンストラクタ
     *
     * @param id             プロジェクトID
     * @param projectName    プロジェクト名
     * @param organizationId 組織ID
     */
    public Project(Long id, String projectName, Long organizationId) {
        this.id = id;
        this.projectName = projectName;
        this.organizationId = organizationId;
    }

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public Long getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Long organizationId) {
        this.organizationId = organizationId;
    }

    public Organization getOrganization() {
        return organization;
    }

    public void setOrganization(Organization organization) {
        this.organization = organization;
    }

    /**
     * データ出力用のtoString()メソッド
     * リレーションシップ情報も含めて出力
     *
     * @return プロジェクト情報の文字列表現
     */
    @Override
    public String toString() {
        return "Project{" +
                "id=" + id +
                ", projectName='" + projectName + '\'' +
                ", organizationId=" + organizationId +
                ", organization=" + (organization != null ? organization.getName() : "null") +
                '}';
    }
}
