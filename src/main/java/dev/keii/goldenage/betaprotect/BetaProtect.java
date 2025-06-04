package dev.keii.goldenage.betaprotect;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import org.bukkit.block.Block;
import org.bukkit.plugin.PluginManager;

import dev.keii.goldenage.GoldenAge;
import dev.keii.goldenage.betaprotect.commands.BetaProtectCommand;
import dev.keii.goldenage.betaprotect.listeners.BlockBreakListener;
import dev.keii.goldenage.betaprotect.listeners.BlockBurnListener;
import dev.keii.goldenage.betaprotect.listeners.BlockPlaceListener;
import dev.keii.goldenage.betaprotect.listeners.EntityExplodeListener;
import dev.keii.goldenage.betaprotect.listeners.PlayerBucketEmptyListener;
import dev.keii.goldenage.betaprotect.listeners.PlayerBucketFillListener;
import dev.keii.goldenage.betaprotect.listeners.PlayerInteractListener;
import lombok.Getter;

public class BetaProtect {
    @Getter
    private GoldenAge plugin;
    // UserId, Interacted Container
    public HashMap<Integer, Block> usersInContainers;

    public Set<Integer> usersInInteractMode;

    public BetaProtect(GoldenAge plugin) {
        this.plugin = plugin;
        this.usersInContainers = new HashMap<>();
        this.usersInInteractMode = new HashSet<Integer>();
    }

    public void registerCommands() {
        this.plugin.getCommand("betaprotect").setExecutor(new BetaProtectCommand(this));
    }

    public void registerListeners() {
        PluginManager pm = this.plugin.getServer().getPluginManager();

        // Block Transactions
        pm.registerEvents(new BlockBreakListener(this), this.plugin);
        pm.registerEvents(new BlockBurnListener(this), this.plugin);
        pm.registerEvents(new BlockPlaceListener(this), this.plugin);
        pm.registerEvents(new EntityExplodeListener(this), this.plugin);
        pm.registerEvents(new PlayerBucketEmptyListener(this), this.plugin);
        pm.registerEvents(new PlayerBucketFillListener(this), this.plugin);
        pm.registerEvents(new PlayerInteractListener(this), this.plugin);

        // Container Transactions
        // -- Container Transactions aren't implemented yet because of limitations in the server software
        // pm.registerEvents(new PlayerMoveListener(this), this.plugin);
        // pm.registerEvents(new ChestListener(this), this.plugin);
        // pm.registerEvents(new BlockDispenseListener(this), this.plugin);

    }
}
