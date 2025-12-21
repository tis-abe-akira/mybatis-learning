package com.example.mybatislearning.service;

import com.example.mybatislearning.entity.Industry;
import com.example.mybatislearning.mapper.IndustryMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * IndustryService
 * Industry管理のビジネスロジックとトランザクション管理
 */
@Service
public class IndustryService {

    private final IndustryMapper industryMapper;

    /**
     * コンストラクタインジェクション
     *
     * @param industryMapper IndustryMapper
     */
    @Autowired
    public IndustryService(IndustryMapper industryMapper) {
        this.industryMapper = industryMapper;
    }

    /**
     * 新しいIndustryを作成
     *
     * @param industry 作成するIndustry
     */
    @Transactional
    public void createIndustry(Industry industry) {
        industryMapper.insert(industry);
    }

    /**
     * IDでIndustryを取得
     *
     * @param id Industry ID
     * @return 該当するIndustry、存在しない場合はnull
     */
    @Transactional(readOnly = true)
    public Industry findById(Long id) {
        return industryMapper.selectById(id);
    }

    /**
     * すべてのIndustryを取得
     *
     * @return Industryのリスト
     */
    @Transactional(readOnly = true)
    public List<Industry> findAll() {
        return industryMapper.selectAll();
    }

    /**
     * 業界名でIndustryを検索
     *
     * @param name 業界名
     * @return 該当するIndustry、存在しない場合はnull
     */
    @Transactional(readOnly = true)
    public Industry findByName(String name) {
        return industryMapper.findByName(name);
    }

    /**
     * Industryを更新
     *
     * @param industry 更新するIndustry
     */
    @Transactional
    public void updateIndustry(Industry industry) {
        industryMapper.update(industry);
    }

    /**
     * Industryを削除
     *
     * @param id 削除するIndustry ID
     */
    @Transactional
    public void deleteIndustry(Long id) {
        industryMapper.deleteById(id);
    }

    // 注: aggregateProjectCountByIndustryメソッドは
    // Projectエンティティ拡張後に追加予定
}
