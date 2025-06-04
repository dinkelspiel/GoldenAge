package dev.keii.goldenage.betaprotect.listeners;

import dev.keii.goldenage.GoldenAge;
import dev.keii.goldenage.betaprotect.BetaProtect;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ChestOpenedEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class ChestListener implements Listener {
    private final GoldenAge plugin;
    private final BetaProtect betaProtect;

    public ChestListener(BetaProtect betaProtect) {
        this.plugin = betaProtect.getPlugin();
        this.betaProtect = betaProtect;
        GoldenAge.getLogger().info("Testa");
    }

    @EventHandler
    public void onChestOpen(ChestOpenedEvent event) {
        GoldenAge.getLogger().info("Test1");
    }


    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        GoldenAge.getLogger().info("Test2");
    }
}
