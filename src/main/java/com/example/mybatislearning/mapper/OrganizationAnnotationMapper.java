package com.example.mybatislearning.mapper;

import com.example.mybatislearning.entity.Organization;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * OrganizationAnnotationMapper（アノテーション）インターフェース
 * XMLファイル不要のアノテーションベースSQLマッピング
 */
@Mapper
public interface OrganizationAnnotationMapper {

    /**
     * 新しいOrganizationを挿入
     * @Optionsアノテーションで生成されたキーを取得
     *
     * @param organization 挿入するOrganization（IDは自動採番）
     */
    @Insert("INSERT INTO organizations (name, description) VALUES (#{name}, #{description})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insert(Organization organization);

    /**
     * IDでOrganizationを検索
     *
     * @param id 検索するOrganization ID
     * @return 該当するOrganization、存在しない場合はnull
     */
    @Select("SELECT * FROM organizations WHERE id = #{id}")
    Organization selectById(Long id);

    /**
     * すべてのOrganizationを取得
     *
     * @return Organizationのリスト
     */
    @Select("SELECT * FROM organizations")
    List<Organization> selectAll();

    /**
     * Organizationを更新
     *
     * @param organization 更新するOrganization
     */
    @Update("UPDATE organizations SET name = #{name}, description = #{description} WHERE id = #{id}")
    void update(Organization organization);

    /**
     * IDでOrganizationを削除
     *
     * @param id 削除するOrganization ID
     */
    @Delete("DELETE FROM organizations WHERE id = #{id}")
    void deleteById(Long id);
}
