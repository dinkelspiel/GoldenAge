package dev.keii.goldenage.statistics.listeners;

import dev.keii.goldenage.statistics.Statistics;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener implements Listener {

    private final Statistics statistics;

    public PlayerJoinListener(Statistics statistics) {
        this.statistics = statistics;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        boolean addedPlayerToSet = statistics.uniquePlayersPerSchedule.add(event.getPlayer().getUniqueId().toString());
        if (addedPlayerToSet) {
            Bukkit.getLogger().info("[GoldenAge] Registered player in unique players");
        }
    }
}
