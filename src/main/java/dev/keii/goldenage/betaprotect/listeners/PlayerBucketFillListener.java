package dev.keii.goldenage.betaprotect.listeners;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerBucketFillEvent;

import dev.keii.goldenage.GoldenAge;
import dev.keii.goldenage.betaprotect.BetaProtect;
import dev.keii.goldenage.betaprotect.dao.BlockTransactionDao;
import dev.keii.goldenage.betaprotect.models.BlockTransaction;
import dev.keii.goldenage.betaprotect.models.TransactionAction;
import dev.keii.goldenage.betaprotect.models.TransactionActor;
import dev.keii.goldenage.dao.UserDao;
import dev.keii.goldenage.dao.WorldDao;
import dev.keii.goldenage.models.User;
import dev.keii.goldenage.models.World;

public class PlayerBucketFillListener implements Listener {
    private final GoldenAge plugin;

    public PlayerBucketFillListener(BetaProtect betaProtect) {
        this.plugin = betaProtect.getPlugin();
    }

    @EventHandler
    public void onPlayerBucketFill(PlayerBucketFillEvent event) throws Exception {
        Block block = event.getBlockClicked();

        UserDao userDao = new UserDao(plugin.getDatabaseUtility());
        User user = userDao.getUserByUuid(event.getPlayer().getUniqueId());
        if (user == null) {
            throw new Exception("Player does not exist");
        }

        WorldDao worldDao = new WorldDao(plugin.getDatabaseUtility());
        World world = worldDao.getWorldByUuid(block.getWorld().getUID());
        if (world == null) {
            throw new Exception("World does not exist");
        }

        BlockTransactionDao blockTransactionDao = new BlockTransactionDao(plugin.getDatabaseUtility());
        BlockTransaction blockTransaction = new BlockTransaction(TransactionActor.Player, TransactionAction.Remove, user, block, world, LocalDateTime.now(ZoneOffset.UTC));
        blockTransactionDao.insertBlockTransaction(blockTransaction);
    }
}
