package dev.keii.goldenage.config;

import dev.keii.goldenage.config.commands.HistoryConfig;
import dev.keii.goldenage.config.commands.ListConfig;
import dev.keii.goldenage.config.commands.SeenConfig;
import lombok.Getter;
import org.bukkit.util.config.Configuration;

public class FormattersConfig {
    private final Configuration configuration;

    public FormattersConfig(Configuration configuration) {
        this.configuration = configuration;
    }

    public String getDate()
    {
        return this.configuration.getString("formatters.date", "yyyy-MM-dd HH:mm");
    }
}
