package dev.keii.goldenage.commands;

import dev.keii.goldenage.GoldenAge;
import dev.keii.goldenage.dao.LoginDao;
import dev.keii.goldenage.dao.UserDao;
import dev.keii.goldenage.dao.UserNameDao;
import dev.keii.goldenage.models.Login;
import dev.keii.goldenage.models.User;
import dev.keii.goldenage.models.UserName;
import dev.keii.goldenage.utils.DateUtility;
import dev.keii.goldenage.utils.PlayerUtility;
import dev.keii.goldenage.utils.StringSubstitutor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("unused")
public class HistoryCommand implements CommandExecutor {
    private GoldenAge plugin;

    public HistoryCommand(GoldenAge plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        LoginDao loginDao = new LoginDao(this.plugin.getDatabaseUtility());
        UserNameDao userNameDao = new UserNameDao(this.plugin.getDatabaseUtility());
        UserDao userDao = new UserDao(this.plugin.getDatabaseUtility());

        if(args.length == 0)
        {
            List<Login> logins = loginDao.getLogins(10);
            sender.sendMessage(plugin.getConfig().getCommands().getHistory().getGlobalTitle());
            for(Login login : logins)
            {
                User user = userDao.getUserById(login.getUserId());
                UserName userName = userNameDao.getUserNameByUser(user);
                Map<String, Object> params = new HashMap<>();
                assert userName != null;
                params.put("player", userName.getName());

                DateTimeFormatter formatter = DateTimeFormatter.ofPattern(plugin.getConfig().getFormatters().getDate());
                params.put("date", formatter.format(login.getCreatedAt()));

                params.put("duration", DateUtility.getHumanReadableTimeSpan(login.getCreatedAt().toEpochSecond(ZoneOffset.UTC)));

                StringSubstitutor substitutor = new StringSubstitutor(params);
                sender.sendMessage(substitutor.replace(plugin.getConfig().getCommands().getHistory().getGlobalRow()));
            }
            return true;
        }

        User user = userDao.getUserByUserName(args[0]);
        if(user == null)
        {
            sender.sendMessage(plugin.getConfig().getCommands().getHistory().getNoUser());
            return false;
        }

        List<Login> logins = loginDao.getLoginsByUser(user, 10);

        UserName userName = userNameDao.getUserNameByUser(user);
        Map<String, Object> params = new HashMap<>();
        assert userName != null;
        params.put("player", userName.getName());

        StringSubstitutor substitutor = new StringSubstitutor(params);
        sender.sendMessage(substitutor.replace(plugin.getConfig().getCommands().getHistory().getPlayerTitle()));

        for(Login login : logins)
        {
            userName = userNameDao.getUserNameByUser(user);
            params = new HashMap<>();
            assert userName != null;
            params.put("player", userName.getName());

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(plugin.getConfig().getFormatters().getDate());
            params.put("date", formatter.format(login.getCreatedAt()));

            params.put("duration", DateUtility.getHumanReadableTimeSpan(login.getCreatedAt().toEpochSecond(ZoneOffset.UTC)));

            substitutor = new StringSubstitutor(params);
            sender.sendMessage(substitutor.replace(plugin.getConfig().getCommands().getHistory().getPlayerRow()));
        }

        return true;
    }

}
