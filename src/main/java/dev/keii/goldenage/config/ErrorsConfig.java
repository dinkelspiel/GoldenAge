package dev.keii.goldenage.config;

import org.bukkit.util.config.Configuration;

public class ErrorsConfig {
    private final Configuration configuration;

    public ErrorsConfig(Configuration configuration) {
        this.configuration = configuration;
    }

    public String getInsufficientArguments() {
        return this.configuration.getString("errors.insufficient-arguments", "§cNot enough arguments");
    }
}