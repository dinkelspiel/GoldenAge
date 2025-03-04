package dev.keii.goldenage;

import dev.keii.goldenage.commands.ListCommand;
import dev.keii.goldenage.listeners.PlayerJoinListener;
import dev.keii.goldenage.utils.DatabaseUtility;
import dev.keii.goldenage.utils.Logger;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class GoldenAge extends JavaPlugin {
    @Getter
    private DatabaseUtility databaseUtility;

    @Getter
    private static Logger logger;

    @Override
    public void onEnable() {
        GoldenAge.logger = new Logger("[GoldenAge]");

        this.databaseUtility = new DatabaseUtility(this, "jdbc:sqlite:./plugins/GoldenAge/database.db");
        this.databaseUtility.openConnection();

        GoldenAge.getLogger().info("GoldenAge has been enabled!");
        this.getCommand("list").setExecutor(new ListCommand());
        Bukkit.getPluginManager().registerEvents(new PlayerJoinListener(this), this);
    }

    @Override
    public void onDisable() {
        this.databaseUtility.closeConnection();

        getLogger().info("GoldenAge has been disabled!");
    }
}
