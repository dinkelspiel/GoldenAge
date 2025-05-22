package dev.keii.goldenage.config;

import javax.annotation.Nullable;

import org.bukkit.util.config.Configuration;

public class StatisticsConfig {
    private final Configuration configuration;

    public StatisticsConfig(Configuration configuration) {
        this.configuration = configuration;
    }

    public boolean getEnabled() {
        return this.configuration.getBoolean("statistics.enabled", true);
    }

    public String getRemote() {
        return this.configuration.getString("statistics.remote", "https://goldenage.keii.dev");
    }

    public @Nullable Integer getServerId() {
        int serverId = this.configuration.getInt("statistics.serverId", -1);
        if (serverId == -1) {
            return null;
        } else {
            return serverId;
        }
    }

    public @Nullable String getServerSecret() {
        String serverSecret = this.configuration.getString("statistics.serverSecret", "");
        if (serverSecret.equals("")) {
            return null;
        } else {
            return serverSecret;
        }
    }
}
