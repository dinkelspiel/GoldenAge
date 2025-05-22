package dev.keii.goldenage.utils;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.plugin.SimplePluginManager;

import java.lang.reflect.Field;
import java.util.Map;

public class CommandUtility {
    @SuppressWarnings("unchecked")
    public static void unregisterCommand(String name) {
        try {
            SimplePluginManager pluginManager = (SimplePluginManager) Bukkit.getPluginManager();

            Field commandMapField = SimplePluginManager.class.getDeclaredField("commandMap");
            commandMapField.setAccessible(true);
            SimpleCommandMap commandMap = (SimpleCommandMap) commandMapField.get(pluginManager);

            Field knownCommandsField = SimpleCommandMap.class.getDeclaredField("knownCommands");
            knownCommandsField.setAccessible(true);
            Map<String, Command> knownCommands = (Map<String, Command>) knownCommandsField.get(commandMap);

            // Remove the command and its aliases from the map
            Command cmd = knownCommands.get(name);
            if (cmd != null) {
                knownCommands.remove(name);
                for (String alias : cmd.getAliases()) {
                    knownCommands.remove(alias);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
