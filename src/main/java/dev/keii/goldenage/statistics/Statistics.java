package dev.keii.goldenage.statistics;

import dev.keii.goldenage.statistics.listeners.PlayerJoinListener;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import javax.annotation.Nullable;
import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.Set;

public class Statistics {
    private final JavaPlugin plugin;
    private final String remote;
    private final @Nullable Integer serverId;
    private final @Nullable String serverSecret;
    public final Set<String> uniquePlayersPerSchedule;

    public Statistics(JavaPlugin plugin, String remote, int serverId, String serverSecret) {
        this.plugin = plugin;
        this.remote = remote;
        this.serverId = serverId;
        this.serverSecret = serverSecret;
        this.uniquePlayersPerSchedule = new HashSet<>();
    }

    protected Statistics(JavaPlugin plugin, String remote) {
        this.plugin = plugin;
        this.remote = remote;
        this.serverId = null;
        this.serverSecret = null;
        this.uniquePlayersPerSchedule = new HashSet<>();
    }

    final int ticksInASecond = 20;
    final int secondsInAnHour = 3600;

    public void beginScheduler() {
        // Register Events
        PluginManager pm = Bukkit.getPluginManager();
        pm.registerEvents(new PlayerJoinListener(this), this.plugin);

        // Register Scheduler
        int interval = ticksInASecond * secondsInAnHour;
        Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, () -> {
            sendStatistics();
            Bukkit.getLogger().info("[GoldenAge] Sent automated statistics");
        }, interval, interval);
    }

    public Integer sendStatistics() {
        int playerCount = this.uniquePlayersPerSchedule.size();
        // Reset unique player count for next schedule
        this.uniquePlayersPerSchedule.clear();

        // String gameVersion = this.plugin.getServer().getGameVersion();
        String gameVersion = "b1.7.3"; // getGameVersion() is broken currently waiting for upstream fix
        String serverEnvironment = this.plugin.getServer().getServerEnvironment();
        String operatingSystem = System.getProperty("os.name");
        String arch = System.getProperty("os.arch");
        String javaVersion = System.getProperty("java.version");

        try {
            URL url = (new URI(this.remote + "/api/statistics")).toURL();
            HttpURLConnection conn;
            if (url.getProtocol().equalsIgnoreCase("https")) {
                conn = (HttpsURLConnection) url.openConnection();
            } else {
                conn = (HttpURLConnection) url.openConnection();
            }

            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json; utf-8");
            conn.setRequestProperty("Accept", "application/json");
            conn.setDoOutput(true);

            StringBuilder body = new StringBuilder();

            body.append("{");
            if (this.serverId != null) {
                body.append("\"serverId\":").append(this.serverId).append(",");
                body.append("\"serverSecret\":\"").append(this.serverSecret).append("\",");
            }

            body.append("\"playerCount\":").append(playerCount).append(",");
            body.append("\"gameVersion\":\"").append(gameVersion).append("\",");
            body.append("\"serverEnvironment\":\"").append(serverEnvironment).append("\",");
            body.append("\"operatingSystem\":\"").append(operatingSystem).append("\",");
            body.append("\"arch\":\"").append(arch).append("\",");
            body.append("\"javaVersion\":\"").append(javaVersion).append("\"");

            body.append("}");

            String jsonInputString = body.toString();

            // Send JSON body
            try (OutputStream os = conn.getOutputStream()) {
                byte[] input = jsonInputString.getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }

            // Handle Error
            if (conn.getResponseCode() != 201) {
                InputStream responseStream;

                if (conn.getResponseCode() >= 200 && conn.getResponseCode() < 300) {
                    responseStream = conn.getInputStream();
                } else {
                    responseStream = conn.getErrorStream();
                }

                BufferedReader in = new BufferedReader(new InputStreamReader(responseStream,
                        StandardCharsets.UTF_8));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = in.readLine()) != null) {
                    response.append(line).append("\n");
                }
                in.close();

                Bukkit.getLogger().severe(
                        "Error when submitting statistics: " + conn.getResponseMessage() + " " + response.toString());
            }

            return conn.getResponseCode();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
