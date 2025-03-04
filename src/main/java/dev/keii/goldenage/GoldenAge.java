package dev.keii.goldenage;

import dev.keii.goldenage.commands.ListCommand;
import dev.keii.goldenage.events.PlayerJoinListener;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class GoldenAge extends JavaPlugin {

    @Override
    public void onEnable() {
        Bukkit.getLogger().info("GoldenAge has been enabled!");
        this.getCommand("list").setExecutor(new ListCommand());
        Bukkit.getPluginManager().registerEvents(new PlayerJoinListener(this), this);
    }

    @Override
    public void onDisable() {
        Bukkit.getLogger().info("GoldenAge has been disabled!");
    }
}
