package dev.keii.goldenage;

import dev.keii.goldenage.commands.DatabaseCommand;
import dev.keii.goldenage.commands.ListCommand;
import dev.keii.goldenage.commands.SeenCommand;
import dev.keii.goldenage.config.Config;
import dev.keii.goldenage.config.ConfigLoader;
import dev.keii.goldenage.listeners.PlayerJoinListener;
import dev.keii.goldenage.migration.Migrator;
import dev.keii.goldenage.utils.DatabaseUtility;
import dev.keii.goldenage.utils.Logger;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;

public class GoldenAge extends JavaPlugin {
    @Getter
    private DatabaseUtility databaseUtility;

    @Getter
    private Migrator migrator;

    @Getter
    private static Logger logger;

    @Getter
    private Config config;

    public void setupConfig() {
        File configFile = new File(getDataFolder(), "plugins/GoldenAge/config.yml");

        try {
            InputStream configInputStream = getClassLoader().getResourceAsStream("config.yml");
            FileWriter configFileWriter = new FileWriter("plugins/GoldenAge/config.yml");

            if (configInputStream != null && configFile.createNewFile()) {
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(configInputStream, StandardCharsets.UTF_8))) {
                    StringBuilder content = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        content.append(line).append(System.lineSeparator());
                    }
                    configFileWriter.write(content.toString());
                    configFileWriter.close();
                }

                configInputStream.close();
            }
        } catch (IOException e) {
            GoldenAge.getLogger().severe("Failed to create config!");
            GoldenAge.getLogger().severe(e.getMessage());
            Bukkit.getPluginManager().disablePlugin(this);
        }
    }

    @Override
    public void onEnable() {
        GoldenAge.logger = new Logger("[GoldenAge]");

        if (!getDataFolder().exists()) {
            getDataFolder().mkdirs();
        }

        setupConfig();

        try {
            this.config = ConfigLoader.loadConfig("plugins/GoldenAge/config.yml");
        } catch (IOException e) {
            GoldenAge.getLogger().severe("Failed to read config!");
            GoldenAge.getLogger().severe(e.getMessage());
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }

        if (config.getDatabase().isAutoMigrate()) {
            Migrator migrator = new Migrator(this);
            try {
                migrator.migrate();
            } catch (SQLException e) {
                GoldenAge.getLogger().severe("Failed to migrate db!");
                GoldenAge.getLogger().severe(e.getMessage());
                Bukkit.getPluginManager().disablePlugin(this);
                return;
            }
        }

        File databaseFile = new File("plugins/GoldenAge/database.db");

        this.databaseUtility = new DatabaseUtility(this, "jdbc:sqlite:" + databaseFile.getAbsolutePath());
        this.databaseUtility.openConnection();

        this.migrator = new Migrator(this);
        try {
            this.migrator.setupMigrationsTable();
        } catch (SQLException e) {
            GoldenAge.getLogger().severe("Failed to create migrations table!");
            GoldenAge.getLogger().severe(e.getMessage());
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }

        this.getCommand("list").setExecutor(new ListCommand());
        this.getCommand("seen").setExecutor(new SeenCommand(this));
        this.getCommand("db").setExecutor(new DatabaseCommand(this));

        this.getServer().getPluginManager().registerEvents(new PlayerJoinListener(this), this);

        GoldenAge.getLogger().info("GoldenAge has been enabled!");
    }

    @Override
    public void onDisable() {
        if (this.databaseUtility != null)
            this.databaseUtility.closeConnection();

        getLogger().info("GoldenAge has been disabled!");
    }
}
