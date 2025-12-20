package com.example.mybatislearning.service;

import com.example.mybatislearning.entity.Project;
import com.example.mybatislearning.mapper.ProjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * ProjectService
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
     *
     * @param project 作成するProject
     */
    @Transactional
    public void createProject(Project project) {
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
     *
     * @param project 更新するProject
     */
    @Transactional
    public void updateProject(Project project) {
        projectMapper.update(project);
    }

    /**
     * Projectを削除
     *
     * @param id 削除するProject ID
     */
    @Transactional
    public void deleteProject(Long id) {
        projectMapper.deleteById(id);
    }
}
