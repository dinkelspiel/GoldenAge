package dev.keii.goldenage.config;

import dev.keii.goldenage.config.betaprotect.CommandsConfig;
import dev.keii.goldenage.config.commands.HistoryConfig;
import dev.keii.goldenage.config.commands.ListConfig;
import dev.keii.goldenage.config.commands.SeenConfig;
import lombok.Getter;
import org.bukkit.util.config.Configuration;

public class BetaProtectConfig {
    private final Configuration configuration;
    @Getter
    private CommandsConfig commands;

    public BetaProtectConfig(Configuration configuration) {
        this.configuration = configuration;
        this.commands = new CommandsConfig(configuration);
    }

    public boolean isEnabled() {
        return this.configuration.getBoolean("betaprotect.enabled", true);
    }
}
