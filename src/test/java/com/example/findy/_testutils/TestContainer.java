package com.example.findy._testutils;

import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
@SuppressWarnings("resource")
public abstract class TestContainer {

    static final String              MARIA_DB_CONTAINER_IMAGE = "mariadb:11.6";
    static final MySQLContainer<?> MARIA_DB_CONTAINER;

    static {
        MARIA_DB_CONTAINER = new MySQLContainer<>(MARIA_DB_CONTAINER_IMAGE).withReuse(true);
        MARIA_DB_CONTAINER.start();
    }

    @DynamicPropertySource
    static void registerProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", MARIA_DB_CONTAINER::getJdbcUrl);
        registry.add("spring.datasource.username", MARIA_DB_CONTAINER::getUsername);
        registry.add("spring.datasource.password", MARIA_DB_CONTAINER::getPassword);
    }
}
