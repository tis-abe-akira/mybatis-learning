package com.example.mybatislearning;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * パッケージ構成が正しく作成されていることを検証するテスト
 */
class PackageStructureTest {

    private static final String BASE_PATH = "src/main/java/com/example/mybatislearning";
    private static final String RESOURCES_PATH = "src/main/resources";
    private static final String TEST_PATH = "src/test/java/com/example/mybatislearning";

    @Test
    void entityPackageExists() {
        Path entityPath = Paths.get(BASE_PATH, "entity");
        assertTrue(Files.exists(entityPath) && Files.isDirectory(entityPath),
                "entity パッケージが存在しません: " + entityPath);
    }

    @Test
    void mapperPackageExists() {
        Path mapperPath = Paths.get(BASE_PATH, "mapper");
        assertTrue(Files.exists(mapperPath) && Files.isDirectory(mapperPath),
                "mapper パッケージが存在しません: " + mapperPath);
    }

    @Test
    void servicePackageExists() {
        Path servicePath = Paths.get(BASE_PATH, "service");
        assertTrue(Files.exists(servicePath) && Files.isDirectory(servicePath),
                "service パッケージが存在しません: " + servicePath);
    }

    @Test
    void mappersDirectoryExists() {
        Path mappersPath = Paths.get(RESOURCES_PATH, "mappers");
        assertTrue(Files.exists(mappersPath) && Files.isDirectory(mappersPath),
                "mappers ディレクトリが存在しません: " + mappersPath);
    }

    @Test
    void testPackageExists() {
        Path testPath = Paths.get(TEST_PATH);
        assertTrue(Files.exists(testPath) && Files.isDirectory(testPath),
                "テストパッケージが存在しません: " + testPath);
    }
}
