package dev.keii.goldenage.config;

//statistics:
//    enabled: true
//    remote: "http://localhost:8080"
//    pluginId: 1

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
        return this.configuration.getString("statistics.remote", "http://localhost:8080");
    }

    public int getServerId() {
        return this.configuration.getInt("statistics.serverId", -1);
    }

    public String getServerSecret() {
        return this.configuration.getString("statistics.serverSecret", "");
    }
}

