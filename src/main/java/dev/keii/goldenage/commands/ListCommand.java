package dev.keii.goldenage.commands;

import dev.keii.goldenage.utils.PlayerUtility;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

@SuppressWarnings("unused")
public class ListCommand extends JavaPlugin implements CommandExecutor {

    @Override
    public void onEnable() {
        PluginDescriptionFile description = getDescription();
        System.out.println("[ListCommand] Enabled!");

        getCommand("list").setExecutor(this);
    }

    @Override
    public void onDisable() {
        PluginDescriptionFile description = getDescription();
        System.out.println("[ListCommand] Disabled!");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("list")) {
            PlayerUtility playerUtility = new PlayerUtility();
            sender.sendMessage(ChatColor.GREEN + playerUtility.getPlayersListString());

            return true;
        }

        return false;
    }

}
