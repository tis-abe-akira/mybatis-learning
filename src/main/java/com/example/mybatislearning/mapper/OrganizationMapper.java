package com.example.mybatislearning.mapper;

import com.example.mybatislearning.entity.Organization;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * OrganizationMapper（XML）インターフェース
 * XMLマッパーファイルでSQL定義を行うCRUD操作
 */
@Mapper
public interface OrganizationMapper {

    /**
     * 新しいOrganizationを挿入
     *
     * @param organization 挿入するOrganization（IDは自動採番）
     */
    void insert(Organization organization);

    /**
     * IDでOrganizationを検索
     *
     * @param id 検索するOrganization ID
     * @return 該当するOrganization、存在しない場合はnull
     */
    Organization selectById(Long id);

    /**
     * すべてのOrganizationを取得
     *
     * @return Organizationのリスト
     */
    List<Organization> selectAll();

    /**
     * Organizationを更新
     *
     * @param organization 更新するOrganization
     */
    void update(Organization organization);

    /**
     * IDでOrganizationを削除
     *
     * @param id 削除するOrganization ID
     */
    void deleteById(Long id);
}
