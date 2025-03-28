package dev.keii.goldenage.commands;

import dev.keii.goldenage.migration.Migrator;
import dev.keii.goldenage.utils.DatabaseUtility;
import dev.keii.goldenage.utils.PlayerUtility;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.sql.SQLException;

@SuppressWarnings("unused")
public class DatabaseCommand implements CommandExecutor {
    DatabaseUtility db;

    public DatabaseCommand(DatabaseUtility db) {
        this.db = db;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (!((Player) sender).getAddress().toString().contains("127.0.0.1")) {
            sender.sendMessage("Unknown command. Type \"help\" for help.");
            return true;
        }

        if (args.length == 0) {
            sender.sendMessage("No argument is specified.");
            return false;
        }

        Migrator migrator = new Migrator(db);
        try {

            if (args[0].equalsIgnoreCase("migrate")) {
                migrator.migrate();

            } else if (args[0].equalsIgnoreCase("rollback")) {
                migrator.rollback();
            }
        } catch (SQLException e) {
                            throw new RuntimeException(e);
        }

        return true;
    }

}
