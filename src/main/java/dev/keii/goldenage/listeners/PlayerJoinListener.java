package dev.keii.goldenage.listeners;

import dev.keii.goldenage.GoldenAge;
import dev.keii.goldenage.utils.PlayerUtility;
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
        PlayerUtility playerUtility = new PlayerUtility();
        player.sendMessage(playerUtility.getPlayersListString());
    }
}
