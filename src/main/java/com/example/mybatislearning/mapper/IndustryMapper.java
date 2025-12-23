package com.example.mybatislearning.mapper;

import com.example.mybatislearning.entity.Industry;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * IndustryMapper（XML）インターフェース
 * XMLマッパーファイルでSQL定義を行うCRUD操作
 */
@Mapper
public interface IndustryMapper {

    /**
     * 新しいIndustryを挿入
     *
     * @param industry 挿入するIndustry（IDは自動採番）
     */
    void insert(Industry industry);

    /**
     * IDでIndustryを検索
     *
     * @param id 検索するIndustry ID
     * @return 該当するIndustry、存在しない場合はnull
     */
    Industry selectById(Long id);

    /**
     * すべてのIndustryを取得
     *
     * @return Industryのリスト
     */
    List<Industry> selectAll();

    /**
     * 業界名でIndustryを検索
     *
     * @param name 検索する業界名
     * @return 該当するIndustry、存在しない場合はnull
     */
    Industry findByName(String name);

    /**
     * Industryを更新
     *
     * @param industry 更新するIndustry
     */
    void update(Industry industry);

    /**
     * IDでIndustryを削除
     *
     * @param id 削除するIndustry ID
     */
    void deleteById(Long id);
}
