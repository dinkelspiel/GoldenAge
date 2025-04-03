package dev.keii.goldenage.config.commands;

import org.bukkit.util.config.Configuration;

public class SeenConfig {
    private final Configuration configuration;

    public SeenConfig(Configuration configuration) {
        this.configuration = configuration;
    }

    public boolean isEnabled() {
        return this.configuration.getBoolean("commands.seen.enabled", true);
    }

    public String getNoUser() {
        return this.configuration.getString("commands.seen.no-user", "§cThis user has never been seen.");
    }

    public String getSuccessDuration() {
        return this.configuration.getString("commands.seen.success-duration", "§e${player} was seen ${duration} ago.");
    }

    public String getSuccessNow() {
        return this.configuration.getString("commands.seen.success-now", "§e${player} was seen now.");
    }

    public String getSuccessOnline() {
        return this.configuration.getString("commands.seen.success-online", "§e${player} is online.");
    }
}