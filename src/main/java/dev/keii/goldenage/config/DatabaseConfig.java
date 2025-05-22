package dev.keii.goldenage.config;

import org.bukkit.util.config.Configuration;

public class DatabaseConfig {
    private final Configuration configuration;

    public DatabaseConfig(Configuration configuration)
    {
        this.configuration = configuration;
    }

    public boolean isAutoMigrate()
    {
        return this.configuration.getBoolean("database.auto-migrate", true);
    }
}
