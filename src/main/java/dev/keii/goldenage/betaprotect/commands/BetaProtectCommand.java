package dev.keii.goldenage.betaprotect.commands;

import dev.keii.goldenage.GoldenAge;
import dev.keii.goldenage.betaprotect.BetaProtect;
import dev.keii.goldenage.config.Env;
import dev.keii.goldenage.dao.UserDao;
import dev.keii.goldenage.migration.Migrator;
import dev.keii.goldenage.models.User;
import dev.keii.goldenage.utils.DatabaseUtility;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.SQLException;

@SuppressWarnings("unused")
public class BetaProtectCommand implements CommandExecutor {
    BetaProtect betaProtect;
    GoldenAge plugin;

    public BetaProtectCommand(BetaProtect betaProtect) {
        this.betaProtect = betaProtect;
        this.plugin = betaProtect.getPlugin();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            sender.sendMessage(this.plugin.getConfig().getErrors().getInsufficientArguments());
            return false;
        }

        if (!(sender instanceof Player)) {
            //message
            return true;
        }

        Player player = (Player) sender;

        UserDao userDao = new UserDao(plugin.getDatabaseUtility());
        User user = userDao.getUserByUuid(player.getUniqueId());
        if (user == null) {
            GoldenAge.getLogger().severe("Player who ran BetaProtect command didn't exist in database.");
            sender.sendMessage(this.plugin.getConfig().getErrors().getInternalServerError());
            return true;
        }

        if (args[0].equalsIgnoreCase("inspect") || args[0].equalsIgnoreCase("i")) {
            if (!this.betaProtect.usersInInteractMode.contains(user.getId())) {
                this.betaProtect.usersInInteractMode.add(user.getId());
                sender.sendMessage(this.plugin.getConfig().getBetaProtect().getCommands().getBetaProtect().getInspect().getOnMessage());
                return true;
            }

            this.betaProtect.usersInInteractMode.remove(user.getId());
            sender.sendMessage(this.plugin.getConfig().getBetaProtect().getCommands().getBetaProtect().getInspect().getOffMessage());
            return true;
        }

        sender.sendMessage(this.plugin.getConfig().getErrors().getInvalidArgument(args[0]));
        return false;
    }

}
