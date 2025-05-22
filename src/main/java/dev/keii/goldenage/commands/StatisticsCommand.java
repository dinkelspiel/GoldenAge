package dev.keii.goldenage.commands;

import dev.keii.goldenage.GoldenAge;
import dev.keii.goldenage.config.Env;
import dev.keii.goldenage.migration.Migrator;
import dev.keii.goldenage.utils.DatabaseUtility;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.SQLException;

@SuppressWarnings("unused")
public class StatisticsCommand implements CommandExecutor {
    GoldenAge plugin;

    public StatisticsCommand(GoldenAge plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        // Don't allow execution of this command if the environment isn't development
        if (!plugin.getConfig().getEnv().equals(Env.Development)) {
            return false;
        }

        if (args.length == 0) {
            sender.sendMessage(this.plugin.getConfig().getErrors().getInsufficientArguments());
            return false;
        }

        if (args[0].equalsIgnoreCase("send")) {
            Integer responseCode = this.plugin.getStatistics().sendStatistics();
            if (responseCode == null) {
                sender.sendMessage("Internal error submitting statistics");
                return true;
            }
            if (responseCode == 201) {
                sender.sendMessage("Sent statistics");
                return true;
            }

            sender.sendMessage("Error sending statistics check logs");
            return true;
        }

        sender.sendMessage("Invalid argument");
        return false;

    }

}
