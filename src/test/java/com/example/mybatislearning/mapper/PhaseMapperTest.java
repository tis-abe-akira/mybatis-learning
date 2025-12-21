package com.example.mybatislearning.mapper;

import com.example.mybatislearning.entity.Phase;
import com.example.mybatislearning.entity.Project;
import com.example.mybatislearning.enums.PhaseStatus;
import com.example.mybatislearning.enums.PhaseType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * PhaseMapperTest
 * PhaseMapperのCRUD操作をテスト
 */
@SpringBootTest
@Transactional
class PhaseMapperTest {

    @Autowired
    private PhaseMapper phaseMapper;

    @Autowired
    private ProjectMapper projectMapper;

    /**
     * テスト用のProjectを作成するヘルパーメソッド
     */
    private Project createTestProject(String name) {
        Project project = new Project();
        project.setProjectName(name);
        project.setOrganizationId(1L);
        projectMapper.insert(project);
        return project;
    }

    /**
     * Phaseの作成をテスト
     */
    @Test
    void testInsertPhase() {
        System.out.println("\n=== Phase Insert Test ===");

        // 事前にProjectを作成
        Project project = createTestProject("テストプロジェクト1");

        // Create Phase
        Phase phase = new Phase();
        phase.setProjectId(project.getId());
        phase.setPhaseType(PhaseType.REQUIREMENTS);
        phase.setPlannedStartDate(LocalDate.of(2024, 1, 1));
        phase.setPlannedEndDate(LocalDate.of(2024, 1, 31));
        phase.setStatus(PhaseStatus.NOT_STARTED);
        phase.setDeliverables("要件定義書、画面設計書");

        phaseMapper.insert(phase);
        System.out.println("Created: " + phase);

        // 検証: IDが自動採番されること
        assertNotNull(phase.getId());
        assertTrue(phase.getId() > 0);

        System.out.println("=== Phase Insert Test Completed ===\n");
    }

    /**
     * PhaseselectById検索をテスト
     */
    @Test
    void testSelectPhaseById() {
        System.out.println("\n=== Phase SelectById Test ===");

        // 事前データ作成
        Project project = createTestProject("テストプロジェクト2");

        Phase phase = new Phase();
        phase.setProjectId(project.getId());
        phase.setPhaseType(PhaseType.DESIGN);
        phase.setPlannedStartDate(LocalDate.of(2024, 2, 1));
        phase.setPlannedEndDate(LocalDate.of(2024, 2, 29));
        phase.setActualStartDate(LocalDate.of(2024, 2, 1));
        phase.setStatus(PhaseStatus.IN_PROGRESS);
        phase.setDeliverables("基本設計書、詳細設計書");

        phaseMapper.insert(phase);
        Long insertedId = phase.getId();

        // Read
        Phase retrieved = phaseMapper.selectById(insertedId);
        System.out.println("Retrieved: " + retrieved);

        // 検証
        assertNotNull(retrieved);
        assertEquals(project.getId(), retrieved.getProjectId());
        assertEquals(PhaseType.DESIGN, retrieved.getPhaseType());
        assertEquals(LocalDate.of(2024, 2, 1), retrieved.getPlannedStartDate());
        assertEquals(LocalDate.of(2024, 2, 29), retrieved.getPlannedEndDate());
        assertEquals(LocalDate.of(2024, 2, 1), retrieved.getActualStartDate());
        assertNull(retrieved.getActualEndDate());
        assertEquals(PhaseStatus.IN_PROGRESS, retrieved.getStatus());
        assertEquals("基本設計書、詳細設計書", retrieved.getDeliverables());
        assertNotNull(retrieved.getCreatedAt());
        assertNotNull(retrieved.getUpdatedAt());

        System.out.println("=== Phase SelectById Test Completed ===\n");
    }

    /**
     * PhaseselectAll検索をテスト
     */
    @Test
    void testSelectAllPhases() {
        System.out.println("\n=== Phase SelectAll Test ===");

        // 事前データ作成
        Project project1 = createTestProject("テストプロジェクト3");
        Project project2 = createTestProject("テストプロジェクト4");

        Phase phase1 = new Phase();
        phase1.setProjectId(project1.getId());
        phase1.setPhaseType(PhaseType.IMPLEMENTATION);
        phase1.setPlannedStartDate(LocalDate.of(2024, 3, 1));
        phase1.setPlannedEndDate(LocalDate.of(2024, 5, 31));
        phase1.setStatus(PhaseStatus.NOT_STARTED);
        phase1.setDeliverables("ソースコード、単体テスト仕様書");

        Phase phase2 = new Phase();
        phase2.setProjectId(project2.getId());
        phase2.setPhaseType(PhaseType.TESTING);
        phase2.setPlannedStartDate(LocalDate.of(2024, 6, 1));
        phase2.setPlannedEndDate(LocalDate.of(2024, 6, 30));
        phase2.setStatus(PhaseStatus.NOT_STARTED);
        phase2.setDeliverables("テスト結果報告書");

        phaseMapper.insert(phase1);
        phaseMapper.insert(phase2);

        // Read All
        List<Phase> allPhases = phaseMapper.selectAll();
        System.out.println("All Phases: " + allPhases.size() + " records");
        for (Phase p : allPhases) {
            System.out.println("  - " + p);
        }

        // 検証
        assertNotNull(allPhases);
        assertTrue(allPhases.size() >= 2);

        System.out.println("=== Phase SelectAll Test Completed ===\n");
    }

    /**
     * PhasefindByProjectId検索をテスト
     */
    @Test
    void testFindPhasesByProjectId() {
        System.out.println("\n=== Phase FindByProjectId Test ===");

        // 事前データ作成
        Project project = createTestProject("テストプロジェクト5");

        Phase phase1 = new Phase();
        phase1.setProjectId(project.getId());
        phase1.setPhaseType(PhaseType.REQUIREMENTS);
        phase1.setPlannedStartDate(LocalDate.of(2024, 1, 1));
        phase1.setPlannedEndDate(LocalDate.of(2024, 1, 31));
        phase1.setStatus(PhaseStatus.COMPLETED);
        phase1.setDeliverables("要件定義書");

        Phase phase2 = new Phase();
        phase2.setProjectId(project.getId());
        phase2.setPhaseType(PhaseType.DESIGN);
        phase2.setPlannedStartDate(LocalDate.of(2024, 2, 1));
        phase2.setPlannedEndDate(LocalDate.of(2024, 2, 28));
        phase2.setStatus(PhaseStatus.IN_PROGRESS);
        phase2.setDeliverables("設計書");

        phaseMapper.insert(phase1);
        phaseMapper.insert(phase2);

        // Find by ProjectId
        List<Phase> foundPhases = phaseMapper.findByProjectId(project.getId());
        System.out.println("Found by ProjectId: " + foundPhases.size() + " records");
        for (Phase p : foundPhases) {
            System.out.println("  - " + p);
        }

        // 検証
        assertNotNull(foundPhases);
        assertEquals(2, foundPhases.size());
        assertTrue(foundPhases.stream().allMatch(p -> p.getProjectId().equals(project.getId())));

        // 存在しないprojectIdで検索
        List<Phase> notFound = phaseMapper.findByProjectId(99999L);
        System.out.println("Not Found by ProjectId: " + notFound.size() + " records");
        assertEquals(0, notFound.size());

        System.out.println("=== Phase FindByProjectId Test Completed ===\n");
    }

    /**
     * Phaseの更新をテスト
     */
    @Test
    void testUpdatePhase() {
        System.out.println("\n=== Phase Update Test ===");

        // 事前データ作成
        Project project = createTestProject("テストプロジェクト6");

        Phase phase = new Phase();
        phase.setProjectId(project.getId());
        phase.setPhaseType(PhaseType.RELEASE);
        phase.setPlannedStartDate(LocalDate.of(2024, 7, 1));
        phase.setPlannedEndDate(LocalDate.of(2024, 7, 15));
        phase.setStatus(PhaseStatus.NOT_STARTED);
        phase.setDeliverables("リリース計画書");

        phaseMapper.insert(phase);
        Long insertedId = phase.getId();

        // Update
        Phase toUpdate = phaseMapper.selectById(insertedId);
        toUpdate.setActualStartDate(LocalDate.of(2024, 7, 1));
        toUpdate.setActualEndDate(LocalDate.of(2024, 7, 12));
        toUpdate.setStatus(PhaseStatus.COMPLETED);
        toUpdate.setDeliverables("リリース計画書、リリース完了報告書");

        phaseMapper.update(toUpdate);

        // 更新後の確認
        Phase updated = phaseMapper.selectById(insertedId);
        System.out.println("Updated: " + updated);

        // 検証
        assertNotNull(updated);
        assertEquals(LocalDate.of(2024, 7, 1), updated.getActualStartDate());
        assertEquals(LocalDate.of(2024, 7, 12), updated.getActualEndDate());
        assertEquals(PhaseStatus.COMPLETED, updated.getStatus());
        assertEquals("リリース計画書、リリース完了報告書", updated.getDeliverables());

        System.out.println("=== Phase Update Test Completed ===\n");
    }

    /**
     * Phaseの削除をテスト
     */
    @Test
    void testDeletePhase() {
        System.out.println("\n=== Phase Delete Test ===");

        // 事前データ作成
        Project project = createTestProject("テストプロジェクト7");

        Phase phase = new Phase();
        phase.setProjectId(project.getId());
        phase.setPhaseType(PhaseType.TESTING);
        phase.setPlannedStartDate(LocalDate.of(2024, 8, 1));
        phase.setPlannedEndDate(LocalDate.of(2024, 8, 31));
        phase.setStatus(PhaseStatus.NOT_STARTED);
        phase.setDeliverables("テスト計画書");

        phaseMapper.insert(phase);
        Long insertedId = phase.getId();

        // Delete
        phaseMapper.deleteById(insertedId);
        System.out.println("Deleted phase with ID: " + insertedId);

        // 削除後の確認
        Phase deleted = phaseMapper.selectById(insertedId);
        System.out.println("After deletion: " + deleted);

        // 検証
        assertNull(deleted);

        System.out.println("=== Phase Delete Test Completed ===\n");
    }
}
