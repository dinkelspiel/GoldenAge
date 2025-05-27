package dev.keii.goldenage.betaprotect;

import dev.keii.goldenage.betaprotect.listeners.BlockBurnListener;
import dev.keii.goldenage.betaprotect.listeners.BlockPlaceListener;
import dev.keii.goldenage.betaprotect.listeners.EntityExplodeListener;
import org.bukkit.plugin.PluginManager;

import dev.keii.goldenage.GoldenAge;
import dev.keii.goldenage.betaprotect.listeners.BlockBreakListener;
import dev.keii.goldenage.listeners.PlayerJoinListener;
import dev.keii.goldenage.migration.Migrator;
import dev.keii.goldenage.utils.DatabaseUtility;

public class BetaProtect {
    GoldenAge plugin;

    public BetaProtect(GoldenAge plugin) {
        this.plugin = plugin;
    }

    public void registerListeners() {
        PluginManager pm = this.plugin.getServer().getPluginManager();
        pm.registerEvents(new BlockBreakListener(this.plugin), this.plugin);
        pm.registerEvents(new BlockBurnListener(this.plugin), this.plugin);
        pm.registerEvents(new BlockPlaceListener(this.plugin), this.plugin);
        pm.registerEvents(new EntityExplodeListener(this.plugin), this.plugin);
    }
}
