package com.example.findy._testutils;

import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

public class DatabaseCleanerExtension implements AfterAllCallback {

    @Override
    public void afterAll(ExtensionContext context) {
        DatabaseCleaner cleaner = SpringExtension.getApplicationContext(context).getBean(DatabaseCleaner.class);
        cleaner.truncate();
    }

}
