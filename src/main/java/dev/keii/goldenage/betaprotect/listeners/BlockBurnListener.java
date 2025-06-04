package dev.keii.goldenage.betaprotect.listeners;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBurnEvent;

import dev.keii.goldenage.GoldenAge;
import dev.keii.goldenage.betaprotect.BetaProtect;
import dev.keii.goldenage.betaprotect.dao.BlockTransactionDao;
import dev.keii.goldenage.betaprotect.models.BlockTransaction;
import dev.keii.goldenage.betaprotect.models.TransactionAction;
import dev.keii.goldenage.betaprotect.models.TransactionActor;
import dev.keii.goldenage.dao.WorldDao;
import dev.keii.goldenage.models.World;

public class BlockBurnListener implements Listener {
    private final GoldenAge plugin;

    public BlockBurnListener(BetaProtect betaProtect) {
        this.plugin = betaProtect.getPlugin();
    }

    @EventHandler
    public void onBlockBurn(BlockBurnEvent event) throws Exception {
        WorldDao worldDao = new WorldDao(plugin.getDatabaseUtility());
        World world = worldDao.getWorldByUuid(event.getBlock().getWorld().getUID());
        if (world == null) {
            throw new Exception("World does not exist");
        }

        BlockTransaction blockTransaction = new BlockTransaction(TransactionActor.Fire, TransactionAction.Remove, null, event.getBlock(), world, LocalDateTime.now(ZoneOffset.UTC));
        BlockTransactionDao blockTransactionDao = new BlockTransactionDao(plugin.getDatabaseUtility());
        blockTransactionDao.insertBlockTransaction(blockTransaction);
    }
}
