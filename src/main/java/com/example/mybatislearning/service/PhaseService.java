package com.example.mybatislearning.service;

import com.example.mybatislearning.entity.Phase;
import com.example.mybatislearning.mapper.PhaseMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * PhaseService
 * Phase（工程計画・実績）管理のビジネスロジックとトランザクション管理
 */
@Service
public class PhaseService {

    private static final Logger logger = LoggerFactory.getLogger(PhaseService.class);

    private final PhaseMapper phaseMapper;

    /**
     * コンストラクタインジェクション
     *
     * @param phaseMapper PhaseMapper
     */
    @Autowired
    public PhaseService(PhaseMapper phaseMapper) {
        this.phaseMapper = phaseMapper;
    }

    /**
     * 新しいPhaseを作成
     *
     * @param phase 作成するPhase
     */
    @Transactional
    public void createPhase(Phase phase) {
        phaseMapper.insert(phase);
    }

    /**
     * IDでPhaseを取得
     *
     * @param id Phase ID
     * @return 該当するPhase、存在しない場合はnull
     */
    @Transactional(readOnly = true)
    public Phase findById(Long id) {
        return phaseMapper.selectById(id);
    }

    /**
     * すべてのPhaseを取得
     *
     * @return Phaseのリスト
     */
    @Transactional(readOnly = true)
    public List<Phase> findAll() {
        return phaseMapper.selectAll();
    }

    /**
     * プロジェクトIDでPhaseを検索
     *
     * @param projectId プロジェクトID
     * @return 該当するPhaseのリスト
     */
    @Transactional(readOnly = true)
    public List<Phase> findByProjectId(Long projectId) {
        return phaseMapper.findByProjectId(projectId);
    }

    /**
     * Phaseを更新
     * actualEndDate < plannedEndDateの場合、警告ログを出力
     *
     * @param phase 更新するPhase
     */
    @Transactional
    public void updatePhase(Phase phase) {
        // actualEndDateがplannedEndDateより早い場合、警告ログを出力
        if (phase.getActualEndDate() != null && phase.getPlannedEndDate() != null) {
            if (phase.getActualEndDate().isBefore(phase.getPlannedEndDate())) {
                logger.warn("Phase {} completed earlier than planned. Planned: {}, Actual: {}",
                        phase.getId(),
                        phase.getPlannedEndDate(),
                        phase.getActualEndDate());
            }
        }

        phaseMapper.update(phase);
    }

    /**
     * Phaseを削除
     *
     * @param id 削除するPhase ID
     */
    @Transactional
    public void deletePhase(Long id) {
        phaseMapper.deleteById(id);
    }
}
