package dev.keii.goldenage.config;

import dev.keii.goldenage.GoldenAge;
import lombok.Getter;
import org.bukkit.util.config.Configuration;

public class Config {
    @Getter
    private DatabaseConfig database;

    @Getter
    private CommandsConfig commands;

    @Getter
    private ErrorsConfig errors;

    @Getter
    private FormattersConfig formatters;

    @Getter
    private StatisticsConfig statistics;

    private final Configuration configuration;

    public Config(Configuration configuration) {
        this.configuration = configuration;
        this.database = new DatabaseConfig(configuration);
        this.commands = new CommandsConfig(configuration);
        this.errors = new ErrorsConfig(configuration);
        this.formatters = new FormattersConfig(configuration);
        this.statistics = new StatisticsConfig(configuration);
    }

    public Env getEnv() {
        String envString = configuration.getString("env", "development");

        switch (envString.toLowerCase()) {
            case "development":
                return Env.Development;
            case "production":
                return Env.Production;
            // Env is defaulted to production instead of throwing so the plugin still runs
            default:
                GoldenAge.getLogger().warning("Invalid env in config. Accepted values are 'development' and 'production'.");
                return Env.Production;
        }
    }
}
