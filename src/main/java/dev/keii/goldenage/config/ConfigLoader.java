package dev.keii.goldenage.config;

import org.bukkit.util.config.Configuration;

import java.io.File;
import java.io.IOException;

public class ConfigLoader {
    public static Config loadConfig(String filePath) throws IOException {
        Configuration config = new Configuration(new File(filePath));
        config.load();
        return new Config(config);
    }
}