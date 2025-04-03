package dev.keii.goldenage.config;

import lombok.Getter;
import lombok.Setter;

public class DatabaseConfig {
    @Getter
    @Setter
    private boolean autoMigrate;
}
