# MyBatis Learning Guide

MyBatisの段階的な学習ガイド。各章は対応するspec（実装仕様）を持ち、実装コードがリファレンスとして機能します。

## 学習の進め方

1. 各章の**仕様**（`specs/`）で実装内容を確認
2. **learning-notes**で学習ポイントを理解
3. **実装コード**を読んで動作確認
4. **テスト実行**で動作を体験

---

## 第1章: 基本編 ✅ 完了

**仕様**: [.kiro/specs/mybatis-basic-crud/](../specs/mybatis-basic-crud/)
**学習ノート**: [learning-notes.md](../specs/mybatis-basic-crud/learning-notes.md)

### 学習内容
- 環境準備（Spring Boot 3.x + MyBatis 3.5.16 + H2）
- XMLマッパーによるCRUD操作
- アノテーションマッパーによるCRUD操作
- リレーションシップマッピング（association）
- トランザクション管理（@Transactional）

### 実装済みコンポーネント
- ✅ `OrganizationMapper`（XML）- 基本的なCRUD
- ✅ `ProjectMapper`（XML）- JOIN、association
- ✅ `OrganizationAnnotationMapper` - アノテーション方式
- ✅ `OrganizationService` / `ProjectService` - サービス層
- ✅ `MyBatisApplicationTests` - 統合テスト

### 習得スキル
- XMLマッパーとアノテーションマッパーの使い分け
- resultMapの定義
- associationによるリレーションシップマッピング
- パラメータバインディング（`#{}`）
- 自動採番（useGeneratedKeys）

---

## 第2章: 実践編 - 動的SQL 📝 計画中

**予定内容**:
- 検索条件の動的生成（`<if>`, `<choose>`, `<where>`）
- 一括操作（`<foreach>`）
- SQLプロバイダー（@SelectProvider）
- 動的SQLのベストプラクティス

**実装予定**:
- 複数条件検索（名前、説明、日付範囲など）
- 一括INSERT/UPDATE
- 条件によってORDER BYを変更

---

## 第3章: 実践編 - パフォーマンス最適化 📝 計画中

**予定内容**:
- N+1問題の理解と回避
- キャッシング（1次、2次）
- Lazy Loading vs Eager Loading
- バッチ処理

---

## 第4章: 実践編 - 高度なマッピング 📝 計画中

**予定内容**:
- collection（一対多）
- 複雑なネストしたresultMap
- discriminator（継承マッピング）
- カスタムTypeHandler

---

## 参考資料

- [MyBatis公式ドキュメント](https://mybatis.org/mybatis-3/)
- プロジェクト構成: [structure.md](./structure.md)
- 技術スタック: [tech.md](./tech.md)

---

## 次のステップ

現在、**第1章（基本編）が完了**しています。

次の学習を始めるには：
```bash
# 第2章の仕様を作成
/kiro:spec-init "MyBatis動的SQL - 検索条件の動的生成と一括操作"
```

実装と学習を同時に進めることで、理解が深まります！
