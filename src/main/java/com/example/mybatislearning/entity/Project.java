package com.example.mybatislearning.entity;

import com.example.mybatislearning.enums.ProjectStatus;
import com.example.mybatislearning.enums.ProjectType;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * Project（プロジェクト）エンティティクラス
 * データベースのprojectsテーブルに対応するPOJO
 */
public class Project {

    private Long id;
    private String projectName;
    private Long organizationId;

    // 新規フィールド（Phase 3拡張）
    private String customerName;
    private Long industryId;
    private ProjectType projectType;
    private ProjectStatus status;
    private BigDecimal budget;
    private BigDecimal personMonths;
    private Integer teamSize;
    private LocalDate plannedStartDate;
    private LocalDate plannedEndDate;
    private LocalDate actualStartDate;
    private LocalDate actualEndDate;
    private Long projectManagerId;
    private Long technicalLeadId;

    /**
     * リレーションシップ用のフィールド
     * MyBatisのassociationマッピングで使用
     */
    private Organization organization;
    private Industry industry;
    private Person projectManager;
    private Person technicalLead;

    // Phase 4以降で実装予定のコレクション
    // private List<Phase> phases;
    // private List<ProjectMember> projectMembers;
    // private List<ProjectTechnology> projectTechnologies;

    /**
     * デフォルトコンストラクタ
     */
    public Project() {
    }

    /**
     * フィールド初期化コンストラクタ（基本フィールドのみ）
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

    /**
     * フィールド初期化コンストラクタ（拡張版）
     *
     * @param id             プロジェクトID
     * @param projectName    プロジェクト名
     * @param organizationId 組織ID
     * @param customerName   顧客名
     * @param industryId     業界ID
     * @param projectType    プロジェクト種別
     * @param status         ステータス
     */
    public Project(Long id, String projectName, Long organizationId, String customerName,
                   Long industryId, ProjectType projectType, ProjectStatus status) {
        this.id = id;
        this.projectName = projectName;
        this.organizationId = organizationId;
        this.customerName = customerName;
        this.industryId = industryId;
        this.projectType = projectType;
        this.status = status;
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

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public Long getIndustryId() {
        return industryId;
    }

    public void setIndustryId(Long industryId) {
        this.industryId = industryId;
    }

    public Industry getIndustry() {
        return industry;
    }

    public void setIndustry(Industry industry) {
        this.industry = industry;
    }

    public ProjectType getProjectType() {
        return projectType;
    }

    public void setProjectType(ProjectType projectType) {
        this.projectType = projectType;
    }

    public ProjectStatus getStatus() {
        return status;
    }

    public void setStatus(ProjectStatus status) {
        this.status = status;
    }

    public BigDecimal getBudget() {
        return budget;
    }

    public void setBudget(BigDecimal budget) {
        this.budget = budget;
    }

    public BigDecimal getPersonMonths() {
        return personMonths;
    }

    public void setPersonMonths(BigDecimal personMonths) {
        this.personMonths = personMonths;
    }

    public Integer getTeamSize() {
        return teamSize;
    }

    public void setTeamSize(Integer teamSize) {
        this.teamSize = teamSize;
    }

    public LocalDate getPlannedStartDate() {
        return plannedStartDate;
    }

    public void setPlannedStartDate(LocalDate plannedStartDate) {
        this.plannedStartDate = plannedStartDate;
    }

    public LocalDate getPlannedEndDate() {
        return plannedEndDate;
    }

    public void setPlannedEndDate(LocalDate plannedEndDate) {
        this.plannedEndDate = plannedEndDate;
    }

    public LocalDate getActualStartDate() {
        return actualStartDate;
    }

    public void setActualStartDate(LocalDate actualStartDate) {
        this.actualStartDate = actualStartDate;
    }

    public LocalDate getActualEndDate() {
        return actualEndDate;
    }

    public void setActualEndDate(LocalDate actualEndDate) {
        this.actualEndDate = actualEndDate;
    }

    public Long getProjectManagerId() {
        return projectManagerId;
    }

    public void setProjectManagerId(Long projectManagerId) {
        this.projectManagerId = projectManagerId;
    }

    public Person getProjectManager() {
        return projectManager;
    }

    public void setProjectManager(Person projectManager) {
        this.projectManager = projectManager;
    }

    public Long getTechnicalLeadId() {
        return technicalLeadId;
    }

    public void setTechnicalLeadId(Long technicalLeadId) {
        this.technicalLeadId = technicalLeadId;
    }

    public Person getTechnicalLead() {
        return technicalLead;
    }

    public void setTechnicalLead(Person technicalLead) {
        this.technicalLead = technicalLead;
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
                ", customerName='" + customerName + '\'' +
                ", industryId=" + industryId +
                ", industry=" + (industry != null ? industry.getName() : "null") +
                ", projectType=" + projectType +
                ", status=" + status +
                ", budget=" + budget +
                ", personMonths=" + personMonths +
                ", teamSize=" + teamSize +
                ", plannedStartDate=" + plannedStartDate +
                ", plannedEndDate=" + plannedEndDate +
                ", actualStartDate=" + actualStartDate +
                ", actualEndDate=" + actualEndDate +
                ", projectManagerId=" + projectManagerId +
                ", projectManager=" + (projectManager != null ? projectManager.getName() : "null") +
                ", technicalLeadId=" + technicalLeadId +
                ", technicalLead=" + (technicalLead != null ? technicalLead.getName() : "null") +
                '}';
    }
}
