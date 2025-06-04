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

    public String getInternalServerError() {
        return this.configuration.getString("errors.internal-server-error", "§cInternal server error");
    }

    public String getInvalidArgument(String argument) {
        return this.configuration.getString("errors.invalid-argument", "§cInvalid argument provided: \"%s\"").replace("%s", argument);
    }
}