package dev.keii.goldenage.commands;

import dev.keii.goldenage.GoldenAge;
import dev.keii.goldenage.migration.Migrator;
import dev.keii.goldenage.utils.DatabaseUtility;
import dev.keii.goldenage.utils.PlayerUtility;
import org.bukkit.ChatColor;
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

        if(sender instanceof Player) {
            if(!((Player) sender).getAddress().toString().contains("127.0.0.1"))
            {
                sender.sendMessage(((Player) sender).getAddress().toString());
                sender.sendMessage("Unknown command. Type \"help\" for help.");
                return true;
            }
        }

        if (args.length == 0) {
            sender.sendMessage("No argument is specified.");
            return false;
        }

        try {

            if (args[0].equalsIgnoreCase("migrate")) {
                migrator.migrate();
            } else if (args[0].equalsIgnoreCase("rollback")) {
                migrator.rollback();
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
