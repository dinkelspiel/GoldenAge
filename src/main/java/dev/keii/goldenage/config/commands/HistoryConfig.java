package dev.keii.goldenage.config.commands;

import org.bukkit.util.config.Configuration;

public class HistoryConfig {
    private final Configuration configuration;

    public HistoryConfig(Configuration configuration) {
        this.configuration = configuration;
    }

    public boolean isEnabled() {
        return this.configuration.getBoolean("commands.history.enabled", true);
    }

    public String getPlayerTitle() {
        return this.configuration.getString("commands.history.player-title", "§eLogin history for ${player}");
    }

    public String getPlayerRow() {
        return this.configuration.getString("commands.history.player-row", "§e${date} - ${duration}");
    }

    public String getGlobalTitle() {
        return this.configuration.getString("commands.history.global-title", "§eLogin history for everyone");
    }

    public String getGlobalRow() {
        return this.configuration.getString("commands.history.global-row", "§e${player} logged in ${date} - ${duration}");
    }

    public String getNoUser() {
        return this.configuration.getString("commands.history.no-user", "§cUser was not found");
    }

    public int getLimit() {
        return this.configuration.getInt("commands.history.limit", 10);
    }
}
