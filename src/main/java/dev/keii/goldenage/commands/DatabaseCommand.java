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
public class DatabaseCommand implements CommandExecutor {
    DatabaseUtility db;
    Migrator migrator;
    GoldenAge plugin;

    public DatabaseCommand(GoldenAge plugin) {
        this.db = plugin.getDatabaseUtility();
        this.migrator = new Migrator(plugin);
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

        try {
            if (args[0].equalsIgnoreCase("migrate")) {
                migrator.migrate();
            } else if (args[0].equalsIgnoreCase("rollback")) {
                migrator.rollback();
            } else {
                sender.sendMessage(this.plugin.getConfig().getErrors().getInvalidArgument(args[0]));
                return false;
            }
        } catch (SQLException e) {
            sender.sendMessage("Failed to migrate");
            sender.sendMessage(e.getMessage());
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            sender.sendMessage(sw.toString());
        }

        return true;
    }

}
