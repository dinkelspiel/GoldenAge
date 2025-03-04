package dev.keii.goldenage.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;
import java.util.Comparator;

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
            Player[] players = Bukkit.getOnlinePlayers();
            int onlinePlayers = players.length;
            int maxPlayers = Bukkit.getMaxPlayers();

            StringBuilder playerList = new StringBuilder();
            for (Player player : players) {
                ChatColor playerColor = getPlayerColor(player);
                playerList.append(playerColor).append(player.getName()).append(ChatColor.WHITE).append(", ");
            }
            playerList.setLength(playerList.length() - 2);

            sender.sendMessage(ChatColor.GREEN + "Online players (" + onlinePlayers + "/" + maxPlayers + "): " + playerList.toString());

            return true;
        }

        return false;
    }

    private ChatColor getPlayerColor(Player player) {
        return ChatColor.YELLOW;
    }
}
