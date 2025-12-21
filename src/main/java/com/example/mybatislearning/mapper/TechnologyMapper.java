package com.example.mybatislearning.mapper;

import com.example.mybatislearning.entity.Technology;
import com.example.mybatislearning.enums.TechnologyCategory;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * TechnologyMapper（XML）インターフェース
 * XMLマッパーファイルでSQL定義を行うCRUD操作
 */
@Mapper
public interface TechnologyMapper {

    /**
     * 新しいTechnologyを挿入
     *
     * @param technology 挿入するTechnology（IDは自動採番）
     */
    void insert(Technology technology);

    /**
     * IDでTechnologyを検索
     *
     * @param id 検索するTechnology ID
     * @return 該当するTechnology、存在しない場合はnull
     */
    Technology selectById(Long id);

    /**
     * すべてのTechnologyを取得
     *
     * @return Technologyのリスト
     */
    List<Technology> selectAll();

    /**
     * カテゴリでTechnologyを検索
     *
     * @param category 検索する技術カテゴリ
     * @return 該当するTechnologyのリスト
     */
    List<Technology> findByCategory(TechnologyCategory category);

    /**
     * Technologyを更新
     *
     * @param technology 更新するTechnology
     */
    void update(Technology technology);

    /**
     * IDでTechnologyを削除
     *
     * @param id 削除するTechnology ID
     */
    void deleteById(Long id);
}
