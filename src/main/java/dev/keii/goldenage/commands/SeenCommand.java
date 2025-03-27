package dev.keii.goldenage.commands;

import dev.keii.goldenage.utils.PlayerUtility;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

@SuppressWarnings("unused")
public class SeenCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        PlayerUtility playerUtility = new PlayerUtility();
        sender.sendMessage(ChatColor.GREEN + playerUtility.getPlayersListString());

        return true;
    }

}
