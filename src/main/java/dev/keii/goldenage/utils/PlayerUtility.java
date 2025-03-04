package dev.keii.goldenage.utils;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class PlayerUtility {
    private ChatColor getPlayerColor(Player player) {
        return ChatColor.YELLOW;
    }

    public String getPlayersListString()
    {
        Player[] players = Bukkit.getOnlinePlayers();
        int onlinePlayers = players.length;
        int maxPlayers = Bukkit.getMaxPlayers();

        StringBuilder playerList = new StringBuilder();
        for (Player player : players) {
            ChatColor playerColor = getPlayerColor(player);
            playerList.append(playerColor).append(player.getName()).append(ChatColor.WHITE).append(", ");
        }
        playerList.setLength(playerList.length() - 2);

        return "Online players (" + onlinePlayers + "/" + maxPlayers + "): " + playerList.toString();
    }
}
