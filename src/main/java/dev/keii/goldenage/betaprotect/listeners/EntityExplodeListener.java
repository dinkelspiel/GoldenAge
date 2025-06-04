package dev.keii.goldenage.betaprotect.listeners;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;

import org.bukkit.block.Block;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityExplodeEvent;

import dev.keii.goldenage.GoldenAge;
import dev.keii.goldenage.betaprotect.BetaProtect;
import dev.keii.goldenage.betaprotect.dao.BlockTransactionDao;
import dev.keii.goldenage.betaprotect.models.BlockTransaction;
import dev.keii.goldenage.betaprotect.models.TransactionAction;
import dev.keii.goldenage.betaprotect.models.TransactionActor;
import dev.keii.goldenage.dao.WorldDao;
import dev.keii.goldenage.models.World;

public class EntityExplodeListener implements Listener {
    private final GoldenAge plugin;

    public EntityExplodeListener(BetaProtect betaProtect) {
        this.plugin = betaProtect.getPlugin();
    }

    @EventHandler
    public void onEntityExplode(EntityExplodeEvent event) throws Exception {
        List<Block> blocks = event.blockList();

        if (blocks.isEmpty())
            return;

        WorldDao worldDao = new WorldDao(plugin.getDatabaseUtility());
        // Get the world for the first block since blocks can't be broken across dimensions
        World world = worldDao.getWorldByUuid(blocks.get(0).getWorld().getUID());
        if (world == null) {
            throw new Exception("World does not exist");
        }

        BlockTransactionDao blockTransactionDao = new BlockTransactionDao(plugin.getDatabaseUtility());

        TransactionActor actor = TransactionActor.Unknown;
        if (event.getEntity() instanceof TNTPrimed)
            actor = TransactionActor.PrimedTNT;
        else if (event.getEntity() instanceof Creeper)
            actor = TransactionActor.Creeper;
        else if (event.getEntity() instanceof Fireball)
            actor = TransactionActor.Fireball;

        for (Block block : event.blockList()) {
            BlockTransaction blockTransaction = new BlockTransaction(actor, TransactionAction.Remove, null, block, world, LocalDateTime.now(ZoneOffset.UTC));
            blockTransactionDao.insertBlockTransaction(blockTransaction);
        }

    }
}
