package dev.keii.goldenage.commands;

import dev.keii.goldenage.GoldenAge;
import dev.keii.goldenage.utils.PlayerUtility;
import dev.keii.goldenage.utils.StringSubstitutor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("unused")
public class ListCommand implements CommandExecutor {
    private GoldenAge plugin;

    public ListCommand(GoldenAge plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("list")) {
            PlayerUtility playerUtility = new PlayerUtility();
            Map<String, Object> params = new HashMap<>();
            params.put("online", playerUtility.getOnlinePlayers());
            params.put("max", playerUtility.getMaxPlayers());
            params.put("playerList", playerUtility.getPlayersListString());

            StringSubstitutor substitutor = new StringSubstitutor(params);
            sender.sendMessage(substitutor.replace(plugin.getConfig().getCommands().getList().getFormat()));

            return true;
        }

        return false;
    }

}
