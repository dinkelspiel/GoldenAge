package dev.keii.goldenage.config;

import dev.keii.goldenage.config.commands.HistoryConfig;
import dev.keii.goldenage.config.commands.ListConfig;
import dev.keii.goldenage.config.commands.SeenConfig;
import lombok.Getter;
import org.bukkit.util.config.Configuration;

public class CommandsConfig {
    private final Configuration configuration;
    @Getter
    private ListConfig list;
    @Getter
    private SeenConfig seen;
    @Getter
    private HistoryConfig history;

    public CommandsConfig(Configuration configuration) {
        this.configuration = configuration;
        this.list = new ListConfig(configuration);
        this.seen = new SeenConfig(configuration);
        this.history = new HistoryConfig(configuration);
    }
}
