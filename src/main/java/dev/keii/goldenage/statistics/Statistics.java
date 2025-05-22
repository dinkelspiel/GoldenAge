package dev.keii.goldenage.statistics;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class Statistics {
    private final JavaPlugin plugin;
    private final String remote;
    private final int serverId;
    private final String serverSecret;

    public Statistics(JavaPlugin plugin, String remote, int serverId, String serverSecret) {
        this.plugin = plugin;
        this.remote = remote;
        this.serverId = serverId;  
        this.serverSecret = serverSecret;
    }

    final int ticksInASecond = 20;
    final int secondsInAnHour = 3600;

    public void beginScheduler() {
        int interval = ticksInASecond * secondsInAnHour;
        Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, () -> {
            int playerCount = plugin.getServer().getOnlinePlayers().length;
            String gameVersion = this.plugin.getServer().getGameVersion();
            String serverEnvironment = this.plugin.getServer().getServerEnvironment();
            String operatingSystem = System.getProperty("os.name");
            String arch = System.getProperty("os.arch");
            String javaVersion = System.getProperty("java.version");

            try {
                URL url = (new URI(this.remote)).toURL(); // Replace with actual URL
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();

                // Set up connection properties
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/json; utf-8");
                conn.setRequestProperty("Accept", "application/json");
                conn.setDoOutput(true);

                // JSON body
                StringBuilder body = new StringBuilder();

                body.append("{");
                body.append("\"serverId\":").append(this.serverId).append(",");
                body.append("\"serverSecret\":\"").append(this.serverSecret).append("\",");

                body.append("\"playerCount\":").append(playerCount).append(",");
                body.append("\"gameVersion\":\"").append(gameVersion).append("\",");
                body.append("\"serverEnvironment\":\"").append(serverEnvironment).append("\",");
                body.append("\"operatingSystem\":\"").append(operatingSystem).append("\",");
                body.append("\"arch\":\"").append(arch).append("\",");
                body.append("\"javaVersion\":\"").append(javaVersion).append("\",");

                body.append("}");

                String jsonInputString = body.toString();

                // Send JSON body
                try (OutputStream os = conn.getOutputStream()) {
                    byte[] input = jsonInputString.getBytes(StandardCharsets.UTF_8);
                    os.write(input, 0, input.length);
                }

                // Read the response
                int responseCode = conn.getResponseCode();
                Bukkit.broadcastMessage("Response Code: " + responseCode);
                Bukkit.broadcastMessage(conn.getResponseMessage());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, interval, interval);
    }
}
