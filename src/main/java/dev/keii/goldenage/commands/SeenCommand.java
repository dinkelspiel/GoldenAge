package dev.keii.goldenage.commands;

import dev.keii.goldenage.GoldenAge;
import dev.keii.goldenage.dao.LoginDao;
import dev.keii.goldenage.dao.UserDao;
import dev.keii.goldenage.models.Login;
import dev.keii.goldenage.models.User;
import dev.keii.goldenage.utils.DateUtility;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.time.ZoneOffset;

@SuppressWarnings("unused")
public class SeenCommand implements CommandExecutor {
    private GoldenAge plugin;

    public SeenCommand(GoldenAge plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            sender.sendMessage("No argument is specified.");
            return false;
        }

        UserDao userDao = new UserDao(plugin.getDatabaseUtility());
        User user = userDao.getUserByUserName(args[0]);

        if (user == null) {
            sender.sendMessage("§eThis user has never been seen.");
            return true;
        }

        LoginDao loginDao = new LoginDao(plugin.getDatabaseUtility());
        Login login = loginDao.getLatestLoginByUser(user);

        if (login == null) {
            sender.sendMessage("§eThis user has never been seen.");
            return true;
        }


        String ago = DateUtility.getHumanReadableTimeSpan(login.getCreatedAt().toEpochSecond(ZoneOffset.UTC));
        String sb = "§eThis user was seen " +
                ago +
                (ago.equals("now") ? "." : " ago.");
        sender.sendMessage(sb);

        return true;
    }

}
