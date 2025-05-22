package com.example.findy._testutils;

import com.example.findy._core.config.JpaConfig;
import com.example.findy._core.config.PasswordConfig;
import com.example.findy._core.config.QueryDSLConfig;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@DataJpaTest
@ExtendWith(DatabaseCleanerExtension.class)
@Import({JpaConfig.class, QueryDSLConfig.class, PasswordConfig.class, DatabaseCleaner.class})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class RepositoryTest extends TestContainer {

    @PersistenceContext
    private EntityManager entityManager;

    @MockitoBean
    public final String    cdnDomain = "https://cdn.domain.com/";

    @BeforeEach
    public void init() {
    }

    private void execute(String sql) {
        String[] statements = sql.split(";");

        for (String statement : statements) {
            if (!statement.trim().isEmpty()) {
                entityManager.createNativeQuery(statement.trim()).executeUpdate();
            }
        }
    }
}
