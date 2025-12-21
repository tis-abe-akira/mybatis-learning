package com.example.mybatislearning.service;

import com.example.mybatislearning.entity.Phase;
import com.example.mybatislearning.entity.Project;
import com.example.mybatislearning.enums.PhaseStatus;
import com.example.mybatislearning.enums.PhaseType;
import com.example.mybatislearning.mapper.ProjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * PhaseServiceTest
 * PhaseServiceのビジネスロジックとトランザクション管理をテスト
 */
@SpringBootTest
@Transactional
class PhaseServiceTest {

    @Autowired
    private PhaseService phaseService;

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
    void testCreatePhase() {
        System.out.println("\n=== PhaseService Create Test ===");

        // 事前にProjectを作成
        Project project = createTestProject("サービステストプロジェクト1");

        // Create Phase
        Phase phase = new Phase();
        phase.setProjectId(project.getId());
        phase.setPhaseType(PhaseType.REQUIREMENTS);
        phase.setPlannedStartDate(LocalDate.of(2024, 1, 1));
        phase.setPlannedEndDate(LocalDate.of(2024, 1, 31));
        phase.setStatus(PhaseStatus.NOT_STARTED);
        phase.setDeliverables("要件定義書");

        phaseService.createPhase(phase);
        System.out.println("Created: " + phase);

        // 検証
        assertNotNull(phase.getId());
        assertTrue(phase.getId() > 0);

        System.out.println("=== PhaseService Create Test Completed ===\n");
    }

    /**
     * PhasefindById検索をテスト
     */
    @Test
    void testFindPhaseById() {
        System.out.println("\n=== PhaseService FindById Test ===");

        // 事前データ作成
        Project project = createTestProject("サービステストプロジェクト2");

        Phase phase = new Phase();
        phase.setProjectId(project.getId());
        phase.setPhaseType(PhaseType.DESIGN);
        phase.setPlannedStartDate(LocalDate.of(2024, 2, 1));
        phase.setPlannedEndDate(LocalDate.of(2024, 2, 29));
        phase.setStatus(PhaseStatus.IN_PROGRESS);
        phase.setDeliverables("設計書");

        phaseService.createPhase(phase);
        Long createdId = phase.getId();

        // Find by ID
        Phase found = phaseService.findById(createdId);
        System.out.println("Found: " + found);

        // 検証
        assertNotNull(found);
        assertEquals(PhaseType.DESIGN, found.getPhaseType());
        assertEquals(PhaseStatus.IN_PROGRESS, found.getStatus());

        System.out.println("=== PhaseService FindById Test Completed ===\n");
    }

    /**
     * PhasefindAll検索をテスト
     */
    @Test
    void testFindAllPhases() {
        System.out.println("\n=== PhaseService FindAll Test ===");

        // 事前データ作成
        Project project1 = createTestProject("サービステストプロジェクト3");
        Project project2 = createTestProject("サービステストプロジェクト4");

        Phase phase1 = new Phase();
        phase1.setProjectId(project1.getId());
        phase1.setPhaseType(PhaseType.IMPLEMENTATION);
        phase1.setPlannedStartDate(LocalDate.of(2024, 3, 1));
        phase1.setPlannedEndDate(LocalDate.of(2024, 5, 31));
        phase1.setStatus(PhaseStatus.NOT_STARTED);
        phase1.setDeliverables("ソースコード");

        Phase phase2 = new Phase();
        phase2.setProjectId(project2.getId());
        phase2.setPhaseType(PhaseType.TESTING);
        phase2.setPlannedStartDate(LocalDate.of(2024, 6, 1));
        phase2.setPlannedEndDate(LocalDate.of(2024, 6, 30));
        phase2.setStatus(PhaseStatus.NOT_STARTED);
        phase2.setDeliverables("テスト報告書");

        phaseService.createPhase(phase1);
        phaseService.createPhase(phase2);

        // Find All
        List<Phase> allPhases = phaseService.findAll();
        System.out.println("All Phases: " + allPhases.size() + " records");
        for (Phase p : allPhases) {
            System.out.println("  - " + p);
        }

        // 検証
        assertNotNull(allPhases);
        assertTrue(allPhases.size() >= 2);

        System.out.println("=== PhaseService FindAll Test Completed ===\n");
    }

    /**
     * PhasefindByProjectId検索をテスト
     */
    @Test
    void testFindPhasesByProjectId() {
        System.out.println("\n=== PhaseService FindByProjectId Test ===");

        // 事前データ作成
        Project project = createTestProject("サービステストプロジェクト5");

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

        phaseService.createPhase(phase1);
        phaseService.createPhase(phase2);

        // Find by ProjectId
        List<Phase> foundPhases = phaseService.findByProjectId(project.getId());
        System.out.println("Found by ProjectId: " + foundPhases.size() + " records");
        for (Phase p : foundPhases) {
            System.out.println("  - " + p);
        }

        // 検証
        assertNotNull(foundPhases);
        assertEquals(2, foundPhases.size());

        System.out.println("=== PhaseService FindByProjectId Test Completed ===\n");
    }

    /**
     * Phaseの更新をテスト
     */
    @Test
    void testUpdatePhase() {
        System.out.println("\n=== PhaseService Update Test ===");

        // 事前データ作成
        Project project = createTestProject("サービステストプロジェクト6");

        Phase phase = new Phase();
        phase.setProjectId(project.getId());
        phase.setPhaseType(PhaseType.RELEASE);
        phase.setPlannedStartDate(LocalDate.of(2024, 7, 1));
        phase.setPlannedEndDate(LocalDate.of(2024, 7, 15));
        phase.setStatus(PhaseStatus.NOT_STARTED);
        phase.setDeliverables("リリース計画書");

        phaseService.createPhase(phase);
        Long createdId = phase.getId();

        // Update
        Phase toUpdate = phaseService.findById(createdId);
        toUpdate.setActualStartDate(LocalDate.of(2024, 7, 1));
        toUpdate.setActualEndDate(LocalDate.of(2024, 7, 12));
        toUpdate.setStatus(PhaseStatus.COMPLETED);
        toUpdate.setDeliverables("リリース計画書、リリース完了報告書");

        phaseService.updatePhase(toUpdate);

        // 更新後の確認
        Phase updated = phaseService.findById(createdId);
        System.out.println("Updated: " + updated);

        // 検証
        assertNotNull(updated);
        assertEquals(LocalDate.of(2024, 7, 12), updated.getActualEndDate());
        assertEquals(PhaseStatus.COMPLETED, updated.getStatus());

        System.out.println("=== PhaseService Update Test Completed ===\n");
    }

    /**
     * Phaseの更新時、actualEndDate < plannedEndDateの場合の警告ログをテスト
     */
    @Test
    void testUpdatePhaseWithEarlyCompletion() {
        System.out.println("\n=== PhaseService Update with Early Completion Test ===");

        // 事前データ作成
        Project project = createTestProject("サービステストプロジェクト7");

        Phase phase = new Phase();
        phase.setProjectId(project.getId());
        phase.setPhaseType(PhaseType.IMPLEMENTATION);
        phase.setPlannedStartDate(LocalDate.of(2024, 3, 1));
        phase.setPlannedEndDate(LocalDate.of(2024, 5, 31));  // 計画終了日: 5/31
        phase.setStatus(PhaseStatus.IN_PROGRESS);
        phase.setDeliverables("ソースコード");

        phaseService.createPhase(phase);
        Long createdId = phase.getId();

        // Update with early completion (actualEndDate < plannedEndDate)
        Phase toUpdate = phaseService.findById(createdId);
        toUpdate.setActualStartDate(LocalDate.of(2024, 3, 1));
        toUpdate.setActualEndDate(LocalDate.of(2024, 5, 15));  // 実績終了日: 5/15 (計画より早い)
        toUpdate.setStatus(PhaseStatus.COMPLETED);

        // この更新で警告ログが出力されるはず (実際のログ出力を手動で確認)
        phaseService.updatePhase(toUpdate);
        System.out.println("Updated with early completion (warning log should be output)");

        // 更新後の確認
        Phase updated = phaseService.findById(createdId);
        System.out.println("Updated: " + updated);

        // 検証
        assertNotNull(updated);
        assertEquals(LocalDate.of(2024, 5, 15), updated.getActualEndDate());
        assertTrue(updated.getActualEndDate().isBefore(updated.getPlannedEndDate()));

        System.out.println("=== PhaseService Update with Early Completion Test Completed ===\n");
    }

    /**
     * Phaseの削除をテスト
     */
    @Test
    void testDeletePhase() {
        System.out.println("\n=== PhaseService Delete Test ===");

        // 事前データ作成
        Project project = createTestProject("サービステストプロジェクト8");

        Phase phase = new Phase();
        phase.setProjectId(project.getId());
        phase.setPhaseType(PhaseType.TESTING);
        phase.setPlannedStartDate(LocalDate.of(2024, 8, 1));
        phase.setPlannedEndDate(LocalDate.of(2024, 8, 31));
        phase.setStatus(PhaseStatus.NOT_STARTED);
        phase.setDeliverables("テスト計画書");

        phaseService.createPhase(phase);
        Long createdId = phase.getId();

        // Delete
        phaseService.deletePhase(createdId);
        System.out.println("Deleted phase with ID: " + createdId);

        // 削除後の確認
        Phase deleted = phaseService.findById(createdId);
        System.out.println("After deletion: " + deleted);

        // 検証
        assertNull(deleted);

        System.out.println("=== PhaseService Delete Test Completed ===\n");
    }
}
