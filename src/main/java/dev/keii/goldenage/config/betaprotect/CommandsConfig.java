package dev.keii.goldenage.config.betaprotect;

import dev.keii.goldenage.config.betaprotect.commands.BetaProtectConfig;
import lombok.Getter;
import org.bukkit.util.config.Configuration;

public class CommandsConfig {
    private final Configuration configuration;
    @Getter
    private BetaProtectConfig betaProtect;

    public CommandsConfig(Configuration configuration) {
        this.configuration = configuration;
        this.betaProtect = new BetaProtectConfig(configuration);
    }
}
