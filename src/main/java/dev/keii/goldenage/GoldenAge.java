package dev.keii.goldenage;

import dev.keii.goldenage.commands.ListCommand;
import dev.keii.goldenage.commands.SeenCommand;
import dev.keii.goldenage.listeners.PlayerJoinListener;
import dev.keii.goldenage.utils.DatabaseUtility;
import dev.keii.goldenage.utils.Logger;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public class GoldenAge extends JavaPlugin {
    @Getter
    private DatabaseUtility databaseUtility;

    @Getter
    private static Logger logger;

    @Override
    public void onEnable() {
        GoldenAge.logger = new Logger("[GoldenAge]");

        // Create plugin folder
        File databaseFile = new File("plugins/GoldenAge/database.db");
        databaseFile.getParentFile().mkdirs();

        this.databaseUtility = new DatabaseUtility(this, "jdbc:sqlite:" + databaseFile.getAbsolutePath());
        this.databaseUtility.openConnection();

        this.getCommand("list").setExecutor(new ListCommand());
        this.getCommand("seen").setExecutor(new SeenCommand());

        this.getServer().getPluginManager().registerEvents(new PlayerJoinListener(this), this);

        GoldenAge.getLogger().info("GoldenAge has been enabled!");
    }

    @Override
    public void onDisable() {
        if(this.databaseUtility != null)
            this.databaseUtility.closeConnection();

        getLogger().info("GoldenAge has been disabled!");
    }
}
