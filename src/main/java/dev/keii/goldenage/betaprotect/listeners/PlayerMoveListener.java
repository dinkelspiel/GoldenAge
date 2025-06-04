package dev.keii.goldenage.betaprotect.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import dev.keii.goldenage.GoldenAge;
import dev.keii.goldenage.betaprotect.BetaProtect;
import dev.keii.goldenage.dao.UserDao;
import dev.keii.goldenage.models.User;

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
