package dev.keii.goldenage.listeners;

import dev.keii.goldenage.GoldenAge;
import dev.keii.goldenage.dao.UserDao;
import dev.keii.goldenage.models.User;
import dev.keii.goldenage.utils.PlayerUtility;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerListener;
import org.bukkit.entity.Player;

import java.time.LocalDateTime;

public class PlayerJoinListener implements Listener {

    private final GoldenAge plugin;

    public PlayerJoinListener(GoldenAge plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
//        UserDao userDao = new UserDao(plugin.getDatabaseUtility());
//        User user = userDao.getUserByUuid(event.getPlayer().getUniqueId());
//        if(user == null)
//        {
//            user = new User(event.getPlayer().getUniqueId(), LocalDateTime.now(), null);
//            userDao.insertUser(user);
//            Bukkit.getLogger().info("Created user in db for " + event.getPlayer().getDisplayName());
//        }
    }
}
