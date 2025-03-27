package dev.keii.goldenage.utils;

import org.bukkit.Bukkit;

import java.util.logging.Level;

public class Logger {
    private String prefix;

    public Logger(String prefix)
    {
        this.prefix = prefix;
    }

    public String getFormattedMessage(String message)
    {
        return prefix + " " + message;
    }

    public void info(String message) {
        Bukkit.getLogger().info(getFormattedMessage(message));
    }

    public void warning(String message) {
        Bukkit.getLogger().warning(getFormattedMessage(message));
    }

    public void severe(String message) {
        Bukkit.getLogger().severe(getFormattedMessage(message));
    }

    public void log(Level level, String message)
    {
        Bukkit.getLogger().log(level, getFormattedMessage(message));
    }
}
