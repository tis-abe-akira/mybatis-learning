package com.example.mybatislearning.mapper;

import com.example.mybatislearning.entity.Phase;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * PhaseMapper
 * Phase（工程計画・実績）のCRUD操作を行うMapperインターフェース
 */
@Mapper
public interface PhaseMapper {

    /**
     * Phaseを新規登録
     * @param phase 登録するPhaseエンティティ
     */
    void insert(Phase phase);

    /**
     * IDでPhaseを検索
     * @param id PhaseのID
     * @return 検索されたPhase（存在しない場合はnull）
     */
    Phase selectById(Long id);

    /**
     * すべてのPhaseを検索
     * @return すべてのPhaseのリスト
     */
    List<Phase> selectAll();

    /**
     * プロジェクトIDでPhaseを検索
     * @param projectId プロジェクトID
     * @return 該当するPhaseのリスト
     */
    List<Phase> findByProjectId(Long projectId);

    /**
     * Phaseを更新
     * @param phase 更新するPhaseエンティティ
     */
    void update(Phase phase);

    /**
     * IDでPhaseを削除
     * @param id 削除するPhaseのID
     */
    void deleteById(Long id);
}
