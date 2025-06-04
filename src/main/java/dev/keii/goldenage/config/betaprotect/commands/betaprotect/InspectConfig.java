package dev.keii.goldenage.config.betaprotect.commands.betaprotect;

import org.bukkit.util.config.Configuration;

public class InspectConfig {
    private final Configuration configuration;

    public InspectConfig(Configuration configuration) {
        this.configuration = configuration;
    }

    public String getOnMessage() {
        return this.configuration.getString("betaprotect.commands.betaprotect.inspect.on-message", "§eTurned on inspect mode");
    }

    public String getOffMessage() {
        return this.configuration.getString("betaprotect.commands.betaprotect.inspect.off-message", "§eTurned off inspect mode");
    }

    public String getBlockTitle() {
        return this.configuration.getString("betaprotect.commands.betaprotect.inspect.block-title", "§f----- §eBetaProtect §f----- §e(x%x/y%y/z%z)");
    }

    public String getBlockEntry() {
        return this.configuration.getString("betaprotect.commands.betaprotect.inspect.block-entry", "§e%d §f- §e%a §f%r §e#%i (%b)");
    }
}
