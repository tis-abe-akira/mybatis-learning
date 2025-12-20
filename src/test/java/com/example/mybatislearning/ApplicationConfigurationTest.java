package com.example.mybatislearning;

import org.apache.ibatis.session.SqlSessionFactory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;

import javax.sql.DataSource;
import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ApplicationConfigurationTest {

    @Autowired
    private Environment environment;

    @Autowired
    private DataSource dataSource;

    @Autowired
    private SqlSessionFactory sqlSessionFactory;

    @Test
    void contextLoads() {
        assertNotNull(environment);
        assertNotNull(dataSource);
        assertNotNull(sqlSessionFactory);
    }

    @Test
    void h2DatabaseConfigurationIsCorrect() throws Exception {
        // H2データソース設定を確認
        assertEquals("jdbc:h2:mem:testdb", environment.getProperty("spring.datasource.url"));
        assertEquals("org.h2.Driver", environment.getProperty("spring.datasource.driver-class-name"));

        // H2コンソールが有効化されていることを確認
        assertEquals("true", environment.getProperty("spring.h2.console.enabled"));
        assertEquals("/h2-console", environment.getProperty("spring.h2.console.path"));

        // データベース接続を確認
        try (Connection connection = dataSource.getConnection()) {
            assertNotNull(connection);
            assertFalse(connection.isClosed());
        }
    }

    @Test
    void mybatisConfigurationIsCorrect() {
        // MyBatis設定を確認
        assertEquals("classpath:mappers/*.xml", environment.getProperty("mybatis.mapper-locations"));
        assertEquals("true", environment.getProperty("mybatis.configuration.map-underscore-to-camel-case"));
        assertEquals("org.apache.ibatis.logging.stdout.StdOutImpl", environment.getProperty("mybatis.configuration.log-impl"));

        // SqlSessionFactoryが正しく設定されていることを確認
        org.apache.ibatis.session.Configuration configuration = sqlSessionFactory.getConfiguration();
        assertTrue(configuration.isMapUnderscoreToCamelCase());
    }

    @Test
    void sqlInitModeIsConfigured() {
        // SQL初期化モードが設定されていることを確認
        assertEquals("always", environment.getProperty("spring.sql.init.mode"));
    }
}
