package com.example.mybatislearning.service;

import com.example.mybatislearning.entity.Project;
import com.example.mybatislearning.enums.ProjectStatus;
import com.example.mybatislearning.enums.ProjectType;
import com.example.mybatislearning.mapper.ProjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

/**
 * ProjectService（Phase 3拡張版）
 * Project管理のビジネスロジックとトランザクション管理
 */
@Service
public class ProjectService {

    private final ProjectMapper projectMapper;

    /**
     * コンストラクタインジェクション
     *
     * @param projectMapper ProjectMapper
     */
    @Autowired
    public ProjectService(ProjectMapper projectMapper) {
        this.projectMapper = projectMapper;
    }

    /**
     * 新しいProjectを作成
     * Phase 3拡張: budget >= 0の検証を追加
     *
     * @param project 作成するProject
     * @throws IllegalArgumentException budgetが負の値の場合
     */
    @Transactional
    public void createProject(Project project) {
        // Budget検証
        if (project.getBudget() != null && project.getBudget().compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Budget must be >= 0");
        }
        projectMapper.insert(project);
    }

    /**
     * IDでProjectを取得
     *
     * @param id Project ID
     * @return 該当するProject、存在しない場合はnull
     */
    @Transactional(readOnly = true)
    public Project getProject(Long id) {
        return projectMapper.selectById(id);
    }

    /**
     * ProjectとOrganizationの結合データを取得
     *
     * @param id Project ID
     * @return Projectとそれに関連するOrganization、存在しない場合はnull
     */
    @Transactional(readOnly = true)
    public Project getProjectWithOrganization(Long id) {
        return projectMapper.selectProjectWithOrganization(id);
    }

    /**
     * 組織IDでProjectを取得
     *
     * @param organizationId 組織ID
     * @return 該当する組織に属するProjectのリスト
     */
    @Transactional(readOnly = true)
    public List<Project> getProjectsByOrganization(Long organizationId) {
        return projectMapper.selectByOrganizationId(organizationId);
    }

    /**
     * すべてのProjectを取得
     *
     * @return Projectのリスト
     */
    @Transactional(readOnly = true)
    public List<Project> getAllProjects() {
        return projectMapper.selectAll();
    }

    /**
     * Projectを更新
     * Phase 3拡張: budget >= 0の検証を追加
     *
     * @param project 更新するProject
     * @throws IllegalArgumentException budgetが負の値の場合
     */
    @Transactional
    public void updateProject(Project project) {
        // Budget検証
        if (project.getBudget() != null && project.getBudget().compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Budget must be >= 0");
        }
        projectMapper.update(project);
    }

    /**
     * Projectを削除
     * 注: Phase 4でカスケード削除ロジックを実装予定
     *
     * @param id 削除するProject ID
     */
    @Transactional
    public void deleteProject(Long id) {
        // Phase 4でPhase、ProjectMember、ProjectTechnologyを先に削除するロジックを追加予定
        projectMapper.deleteById(id);
    }

    // ========== Phase 3拡張メソッド ==========

    /**
     * ステータスでProjectを検索
     *
     * @param status 検索するステータス
     * @return 該当するProjectのリスト
     */
    @Transactional(readOnly = true)
    public List<Project> findByStatus(ProjectStatus status) {
        return projectMapper.findByStatus(status);
    }

    /**
     * 業界IDでProjectを検索
     *
     * @param industryId 検索する業界ID
     * @return 該当するProjectのリスト
     */
    @Transactional(readOnly = true)
    public List<Project> findByIndustryId(Long industryId) {
        return projectMapper.findByIndustryId(industryId);
    }

    /**
     * プロジェクト種別でProjectを検索
     *
     * @param projectType 検索するプロジェクト種別
     * @return 該当するProjectのリスト
     */
    @Transactional(readOnly = true)
    public List<Project> findByProjectType(ProjectType projectType) {
        return projectMapper.findByProjectType(projectType);
    }

    /**
     * プロジェクトマネージャーIDでProjectを検索
     *
     * @param projectManagerId 検索するプロジェクトマネージャーID
     * @return 該当するProjectのリスト
     */
    @Transactional(readOnly = true)
    public List<Project> findByProjectManagerId(Long projectManagerId) {
        return projectMapper.findByProjectManagerId(projectManagerId);
    }

    /**
     * 技術リードIDでProjectを検索
     *
     * @param technicalLeadId 検索する技術リードID
     * @return 該当するProjectのリスト
     */
    @Transactional(readOnly = true)
    public List<Project> findByTechnicalLeadId(Long technicalLeadId) {
        return projectMapper.findByTechnicalLeadId(technicalLeadId);
    }

    /**
     * Projectと全てのリレーションシップを取得
     * Industry、ProjectManager、TechnicalLeadを含む
     * Phase 4でPhase、ProjectMember、ProjectTechnologyも含める予定
     *
     * @param id 検索するProject ID
     * @return Projectとそれに関連する全てのエンティティ、存在しない場合はnull
     */
    @Transactional(readOnly = true)
    public Project findProjectWithAllRelations(Long id) {
        return projectMapper.selectProjectWithAllRelations(id);
    }
}
