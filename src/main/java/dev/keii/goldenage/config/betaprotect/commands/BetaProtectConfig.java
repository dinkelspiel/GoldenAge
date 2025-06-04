package dev.keii.goldenage.config.betaprotect.commands;

import org.bukkit.util.config.Configuration;

import dev.keii.goldenage.config.betaprotect.commands.betaprotect.InspectConfig;
import lombok.Getter;

public class BetaProtectConfig {
    private final Configuration configuration;
    @Getter
    private InspectConfig inspect;

    public BetaProtectConfig(Configuration configuration) {
        this.configuration = configuration;
        this.inspect = new InspectConfig(configuration);
    }
}
