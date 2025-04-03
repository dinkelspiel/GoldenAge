package dev.keii.goldenage.config.commands;

import org.bukkit.util.config.Configuration;

public class ListConfig {
    private final Configuration configuration;

    public ListConfig(Configuration configuration)
    {
        this.configuration = configuration;
    }

    public boolean isEnabled()
    {
        return this.configuration.getBoolean("commands.list.enabled", true);
    }

    public String getFormat() {
        return this.configuration.getString("commands.list.format", "§eOnline players (${online}/${max}): ${playerList}");
    }
}