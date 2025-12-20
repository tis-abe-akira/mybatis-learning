package com.example.mybatislearning.mapper;

import com.example.mybatislearning.entity.Project;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * ProjectMapper（XML）インターフェース
 * XMLマッパーファイルでSQL定義を行うCRUD操作とリレーションシップマッピング
 */
@Mapper
public interface ProjectMapper {

    /**
     * 新しいProjectを挿入
     *
     * @param project 挿入するProject（IDは自動採番）
     */
    void insert(Project project);

    /**
     * IDでProjectを検索
     *
     * @param id 検索するProject ID
     * @return 該当するProject、存在しない場合はnull
     */
    Project selectById(Long id);

    /**
     * すべてのProjectを取得
     *
     * @return Projectのリスト
     */
    List<Project> selectAll();

    /**
     * 組織IDでProjectを検索
     *
     * @param organizationId 検索する組織ID
     * @return 該当する組織に属するProjectのリスト
     */
    List<Project> selectByOrganizationId(Long organizationId);

    /**
     * ProjectとOrganizationの結合データを取得
     *
     * @param id 検索するProject ID
     * @return Projectとそれに関連するOrganization、存在しない場合はnull
     */
    Project selectProjectWithOrganization(Long id);

    /**
     * Projectを更新
     *
     * @param project 更新するProject
     */
    void update(Project project);

    /**
     * IDでProjectを削除
     *
     * @param id 削除するProject ID
     */
    void deleteById(Long id);
}
