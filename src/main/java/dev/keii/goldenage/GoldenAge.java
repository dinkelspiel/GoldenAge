package dev.keii.goldenage;

import dev.keii.goldenage.betaprotect.BetaProtect;
import dev.keii.goldenage.commands.DatabaseCommand;
import dev.keii.goldenage.commands.HistoryCommand;
import dev.keii.goldenage.commands.ListCommand;
import dev.keii.goldenage.commands.SeenCommand;
import dev.keii.goldenage.config.Config;
import dev.keii.goldenage.config.ConfigLoader;
import dev.keii.goldenage.config.Env;
import dev.keii.goldenage.dao.WorldDao;
import dev.keii.goldenage.listeners.PlayerJoinListener;
import dev.keii.goldenage.migration.Migrator;
import dev.keii.goldenage.statistics.Statistics;
import dev.keii.goldenage.utils.DatabaseUtility;
import dev.keii.goldenage.utils.Logger;
import lombok.Getter;
import lombok.SneakyThrows;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.plugin.PluginManager;
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

    @Getter
    private Statistics statistics;

    @SneakyThrows
    public boolean setupConfig() {
        File configFile = new File("plugins/GoldenAge/config.yml");

        try {
            if (configFile.exists()) {
                return true;
            }

            GoldenAge.getLogger().info("Creating config...");

            InputStream configInputStream = getClassLoader().getResourceAsStream("config.yml");
            FileWriter configFileWriter = new FileWriter("plugins/GoldenAge/config.yml");

            if (configInputStream != null) {
                try (BufferedReader reader = new BufferedReader(
                        new InputStreamReader(configInputStream, StandardCharsets.UTF_8))) {
                    StringBuilder content = new StringBuilder();
                    String line;
                    GoldenAge.getLogger().info("Writing defaults to config...");
                    while ((line = reader.readLine()) != null) {
                        content.append(line).append(System.lineSeparator());
                    }
                    configFileWriter.write(content.toString());
                    configFileWriter.close();
                }

                configInputStream.close();
                GoldenAge.getLogger().info("Config created!");
            }
            return true;
        } catch (IOException e) {
            GoldenAge.getLogger().severe("Failed to create config!");
            GoldenAge.getLogger().severe(e.getMessage());
            GoldenAge.getLogger().severe(e.toString());
            Bukkit.getPluginManager().disablePlugin(this);
            throw e;
            // return false;
        }
    }

    @Override
    public void onEnable() {
        GoldenAge.logger = new Logger("[GoldenAge]");
        GoldenAge.getLogger().info("Enabling GoldenAge...");

        if (!getDataFolder().exists()) {
            getDataFolder().mkdirs();
        }

        // Return out of onEnable if setting up config failed
        if (!setupConfig()) {
            return;
        }

        try {
            GoldenAge.getLogger().info("Loading config.");
            this.config = ConfigLoader.loadConfig("plugins/GoldenAge/config.yml");
        } catch (IOException e) {
            GoldenAge.getLogger().severe("Failed to read config!");
            GoldenAge.getLogger().severe(e.getMessage());
            Bukkit.getPluginManager().disablePlugin(this);
            return;
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

        if (config.getDatabase().isAutoMigrate()) {
            Migrator migrator = new Migrator(this);
            try {
                GoldenAge.getLogger().info("Migrating database...");
                migrator.migrate();
                GoldenAge.getLogger().info("Finished migrating database.");
            } catch (SQLException e) {
                GoldenAge.getLogger().severe("Failed to migrate db!");
                GoldenAge.getLogger().severe(e.getMessage());
                Bukkit.getPluginManager().disablePlugin(this);
                return;
            }
        }

        WorldDao worldDao = new WorldDao(this.getDatabaseUtility());
        for (World world : Bukkit.getWorlds()) {
            dev.keii.goldenage.models.World existingWorld = worldDao.getWorldByUuid(world.getUID());
            if (existingWorld != null)
                continue;

            // Create world in database if it doesn't exist
            dev.keii.goldenage.models.World dbWorld = new dev.keii.goldenage.models.World(world.getName(), world.getUID());
            worldDao.insertWorld(dbWorld);
            GoldenAge.getLogger().info("Created world '" + world.getName() + "' in database");
        }

        if (config.getCommands().getList().isEnabled())
            this.getCommand("list").setExecutor(new ListCommand(this));
        if (config.getCommands().getSeen().isEnabled())
            this.getCommand("seen").setExecutor(new SeenCommand(this));
        if (config.getCommands().getHistory().isEnabled())
            this.getCommand("history").setExecutor(new HistoryCommand(this));
        if (config.getEnv().equals(Env.Development))
            this.getCommand("db").setExecutor(new DatabaseCommand(this));

        PluginManager pm = this.getServer().getPluginManager();
        pm.registerEvents(new PlayerJoinListener(this), this);

        if (config.getStatistics().isEnabled()) {
            this.statistics = new Statistics(this, config.getStatistics().getRemote(), config.getStatistics().getServerId(), config.getStatistics().getServerSecret());
            statistics.beginScheduler();
            GoldenAge.getLogger().info("Statistics has been enabled!");
        }

        if (config.getBetaProtect().isEnabled()) {
            BetaProtect betaProtect = new BetaProtect(this);
            betaProtect.registerCommands();
            betaProtect.registerListeners();
            GoldenAge.getLogger().info("BetaProtect has been enabled!");
        }

        GoldenAge.getLogger().info("GoldenAge has been enabled!");
    }

    @Override
    public void onDisable() {
        if (this.databaseUtility != null)
            this.databaseUtility.closeConnection();

        getLogger().info("GoldenAge has been disabled!");
    }
}
