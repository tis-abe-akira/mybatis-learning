package com.example.mybatislearning.service;

import com.example.mybatislearning.entity.Technology;
import com.example.mybatislearning.enums.TechnologyCategory;
import com.example.mybatislearning.mapper.TechnologyMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * TechnologyService
 * Technology管理のビジネスロジックとトランザクション管理
 */
@Service
public class TechnologyService {

    private final TechnologyMapper technologyMapper;

    /**
     * コンストラクタインジェクション
     *
     * @param technologyMapper TechnologyMapper
     */
    @Autowired
    public TechnologyService(TechnologyMapper technologyMapper) {
        this.technologyMapper = technologyMapper;
    }

    /**
     * 新しいTechnologyを作成
     *
     * @param technology 作成するTechnology
     */
    @Transactional
    public void createTechnology(Technology technology) {
        technologyMapper.insert(technology);
    }

    /**
     * IDでTechnologyを取得
     *
     * @param id Technology ID
     * @return 該当するTechnology、存在しない場合はnull
     */
    @Transactional(readOnly = true)
    public Technology findById(Long id) {
        return technologyMapper.selectById(id);
    }

    /**
     * すべてのTechnologyを取得
     *
     * @return Technologyのリスト
     */
    @Transactional(readOnly = true)
    public List<Technology> findAll() {
        return technologyMapper.selectAll();
    }

    /**
     * カテゴリでTechnologyを検索
     *
     * @param category 技術カテゴリ
     * @return 該当するTechnologyのリスト
     */
    @Transactional(readOnly = true)
    public List<Technology> findByCategory(TechnologyCategory category) {
        return technologyMapper.findByCategory(category);
    }

    /**
     * Technologyを更新
     *
     * @param technology 更新するTechnology
     */
    @Transactional
    public void updateTechnology(Technology technology) {
        technologyMapper.update(technology);
    }

    /**
     * Technologyを削除
     *
     * @param id 削除するTechnology ID
     */
    @Transactional
    public void deleteTechnology(Long id) {
        technologyMapper.deleteById(id);
    }

    // 注: aggregateProjectCountByTechnologyメソッドは
    // ProjectTechnologyエンティティ実装後に追加予定
}
