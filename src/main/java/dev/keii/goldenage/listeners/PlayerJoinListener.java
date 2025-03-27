package dev.keii.goldenage.listeners;

import dev.keii.goldenage.GoldenAge;
import dev.keii.goldenage.utils.PlayerUtility;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerListener;
import org.bukkit.entity.Player;

public class PlayerJoinListener implements Listener {

    private final GoldenAge plugin;

    public PlayerJoinListener(GoldenAge plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    // Handle player join
    public void onPlayerJoin(PlayerJoinEvent event) {
        Bukkit.broadcastMessage("test");
    }
}
