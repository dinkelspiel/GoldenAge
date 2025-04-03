package dev.keii.goldenage.listeners;

import dev.keii.goldenage.GoldenAge;
import dev.keii.goldenage.dao.LoginDao;
import dev.keii.goldenage.dao.UserDao;
import dev.keii.goldenage.dao.UserNameDao;
import dev.keii.goldenage.models.Login;
import dev.keii.goldenage.models.User;
import dev.keii.goldenage.models.UserName;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

public class PlayerJoinListener implements Listener {

    private final GoldenAge plugin;

    public PlayerJoinListener(GoldenAge plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        UserDao userDao = new UserDao(plugin.getDatabaseUtility());
        User user = userDao.getUserByUuid(event.getPlayer().getUniqueId());
        if (user == null) {
            user = new User(event.getPlayer().getUniqueId(), LocalDateTime.now(ZoneOffset.UTC), null);
            user = userDao.insertUser(user);
            Bukkit.getLogger().info("Created user in db for " + event.getPlayer().getDisplayName());
        }
        UserNameDao userNameDao = new UserNameDao(plugin.getDatabaseUtility());
        UserName userName = userNameDao.getUserNameByName(event.getPlayer().getDisplayName());
        if (userName == null) {
            userName = new UserName(user, event.getPlayer().getDisplayName(), LocalDateTime.now(ZoneOffset.UTC));
            userNameDao.insertUserName(userName);
        }

        Login login = new Login(user, LocalDateTime.now(ZoneOffset.UTC));
        LoginDao loginDao = new LoginDao(plugin.getDatabaseUtility());
        loginDao.insertLogin(login);
    }
}
