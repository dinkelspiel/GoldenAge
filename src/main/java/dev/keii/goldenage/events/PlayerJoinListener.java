package dev.keii.goldenage.events;

import dev.keii.goldenage.GoldenAge;
import org.bukkit.Bukkit;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerListener;
import org.bukkit.entity.Player;

public class PlayerJoinListener extends PlayerListener {

    private final GoldenAge plugin;

    public PlayerJoinListener(GoldenAge plugin) {
        this.plugin = plugin;
    }

    // Handle player join
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        player.sendMessage("Welcome to the server, " + player.getName() + "!");
        Bukkit.getLogger().info(player.getName() + " has joined the server.");
    }
}
