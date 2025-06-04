package dev.keii.goldenage.betaprotect.listeners;

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
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerMoveEvent;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

public class PlayerMoveListener implements Listener {
    private final GoldenAge plugin;
    private final BetaProtect betaProtect;

    public PlayerMoveListener(BetaProtect betaProtect) {
        this.plugin = betaProtect.getPlugin();
        this.betaProtect = betaProtect;
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) throws Exception {
        UserDao userDao = new UserDao(plugin.getDatabaseUtility());
        User user = userDao.getUserByUuid(event.getPlayer().getUniqueId());
        if (user == null) {
            throw new Exception("Player does not exist");
        }

        betaProtect.usersInContainers.remove(user.getId());
    }
}
