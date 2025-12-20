# Technology Stack

## Architecture

学習用プロジェクトとして、シンプルなスタンドアローンJavaアプリケーション構成。データアクセス層にMyBatisを使用し、インメモリデータベース(H2)で完結する構成。

## Core Technologies

- **Language**: Java 21
- **Build Tool**: Maven
- **ORM/SQL Mapper**: MyBatis 3.5.16
- **Database**: H2 Database 2.2.224 (インメモリ)

## Key Libraries

- **MyBatis 3.5.16**: SQLマッパーフレームワーク - XML/アノテーションベースのSQLマッピング
- **H2 Database**: 組み込みデータベース - 外部DB不要で学習環境を構築

## Development Standards

### Code Quality
- Java 21の機能を活用
- UTF-8エンコーディング統一

### Testing
- 学習プロジェクトとして、実装しながらの動作確認を重視

## Development Environment

### Required Tools
- JDK 21以上
- Maven 3.x

### Common Commands
```bash
# Compile: mvn compile
# Run: mvn exec:java -Dexec.mainClass="<MainClass>"
# Clean: mvn clean
```

## Key Technical Decisions

- **H2データベース選択理由**: インメモリで動作し、外部データベース設定不要で学習に集中できる
- **MyBatis 3.5.16**: 最新の安定版を使用し、現代的な機能を学習
- **Java 21**: 最新のLTS版でモダンなJava機能を活用

---
_学習フォーカスのため、シンプルな技術スタックを維持_
