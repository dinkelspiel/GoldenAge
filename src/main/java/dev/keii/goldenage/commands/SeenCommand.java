package dev.keii.goldenage.commands;

import dev.keii.goldenage.GoldenAge;
import dev.keii.goldenage.dao.LoginDao;
import dev.keii.goldenage.dao.UserDao;
import dev.keii.goldenage.models.Login;
import dev.keii.goldenage.models.User;
import dev.keii.goldenage.utils.DateUtility;
import dev.keii.goldenage.utils.StringSubstitutor;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.time.ZoneOffset;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("unused")
public class SeenCommand implements CommandExecutor {
    private GoldenAge plugin;

    public SeenCommand(GoldenAge plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            sender.sendMessage(plugin.getConfig().getErrors().getInsufficientArguments());
            return false;
        }

        UserDao userDao = new UserDao(plugin.getDatabaseUtility());
        User user = userDao.getUserByUserName(args[0]);

        if (user == null) {
            sender.sendMessage(plugin.getConfig().getCommands().getSeen().getNoUser());
            return true;
        }

        LoginDao loginDao = new LoginDao(plugin.getDatabaseUtility());
        Login login = loginDao.getLatestLoginByUser(user);

        if (login == null) {
            sender.sendMessage(plugin.getConfig().getCommands().getSeen().getNoUser());
            return true;
        }

        Map<String, Object> params = new HashMap<>();
        params.put("player", args[0]);

        if (Arrays.stream(Bukkit.getOnlinePlayers()).anyMatch((Player player) -> player.getDisplayName().equalsIgnoreCase(args[0]) ))
        {
            StringSubstitutor substitutor = new StringSubstitutor(params);
            sender.sendMessage(substitutor.replace(plugin.getConfig().getCommands().getSeen().getSuccessOnline()));
            return true;
        }

        String ago = DateUtility.getHumanReadableTimeSpan(login.getCreatedAt().toEpochSecond(ZoneOffset.UTC));

        if (ago.equals("now")) {
            StringSubstitutor substitutor = new StringSubstitutor(params);
            sender.sendMessage(substitutor.replace(plugin.getConfig().getCommands().getSeen().getSuccessNow()));
        } else {
            params.put("duration", ago);

            StringSubstitutor substitutor = new StringSubstitutor(params);
            sender.sendMessage(substitutor.replace(plugin.getConfig().getCommands().getSeen().getSuccessDuration()));
        }

        return true;
    }

}
