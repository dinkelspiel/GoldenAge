package dev.keii.goldenage.statistics;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import javax.annotation.Nullable;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

public class Statistics {
    private final JavaPlugin plugin;

    public Statistics(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    final int ticksInASecond = 20;
    final int secondsInAnHour = 3600;

    public void beginScheduler() {
        int interval = ticksInASecond * secondsInAnHour;
        Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, () -> {
            int playerCount = plugin.getServer().getOnlinePlayers().length;
            boolean onlineMode = plugin.getServer().getOnlineMode();
            String version = this.plugin.getServer().getGameVersion();
            String environment = this.plugin.getServer().getServerEnvironment();
            @Nullable String publicIp;
            try {
                URL url = new URL("https://api.ipify.org");
                BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
                in.readLine();
            } catch (IOException e) {
                publicIp = null;
            }
            String operatingSystem = System.getProperty("os.name");
            String arch = System.getProperty("os.arch");
            String javaVersion = System.getProperty("java.version");

        }, interval, interval);
    }
}
