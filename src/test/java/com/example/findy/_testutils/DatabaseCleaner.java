package com.example.findy._testutils;

import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

public class DatabaseCleaner {

    private static final String FOREIGN_KEY_CHECK_FORMAT = "SET FOREIGN_KEY_CHECKS = %d";
    private static final String TRUNCATE_FORMAT          = "TRUNCATE TABLE `%s`";

    @PersistenceContext
    private       EntityManager entityManager;
    private final List<String>  tableNames = new ArrayList<>();

    @PostConstruct
    @SuppressWarnings("unchecked")
    private void findDatabaseTableNames() {
        List<String> names = entityManager.createNativeQuery("SHOW TABLES").getResultList();
        tableNames.addAll(names);
    }

    @Transactional
    public void truncate() {
        entityManager.createNativeQuery(String.format(FOREIGN_KEY_CHECK_FORMAT, 0)).executeUpdate();
        for (String tableName : tableNames) {
            entityManager.createNativeQuery(String.format(TRUNCATE_FORMAT, tableName)).executeUpdate();
        }
        entityManager.createNativeQuery(String.format(FOREIGN_KEY_CHECK_FORMAT, 1)).executeUpdate();
    }
}
