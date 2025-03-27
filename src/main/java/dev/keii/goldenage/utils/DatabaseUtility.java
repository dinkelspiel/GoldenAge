package dev.keii.goldenage.utils;

import dev.keii.goldenage.GoldenAge;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseUtility {
    @Getter @Setter
    private String databaseUrl;

    @Getter
    private Connection connection;

    private GoldenAge plugin;

    public DatabaseUtility(GoldenAge plugin, String databaseUrl) {
        setDatabaseUrl(databaseUrl);
        this.plugin = plugin;
    }

    public void openConnection() {
        try {
            Class.forName("org.sqlite.JDBC");
            this.connection = DriverManager.getConnection(this.databaseUrl);
            this.connection.setAutoCommit(false);
        } catch (SQLException e) {
            GoldenAge.getLogger().severe("Failed to open sqlite database connection!");
            GoldenAge.getLogger().severe(e.getMessage());
            Bukkit.getPluginManager().disablePlugin(plugin);
        } catch (ClassNotFoundException e) {
            GoldenAge.getLogger().severe("Could not find org.sqlite.JDBC");
            GoldenAge.getLogger().severe(e.getMessage());
        }
    }

    public void closeConnection() {
        try {
            this.connection.close();
        } catch (SQLException e) {
            GoldenAge.getLogger().severe("Failed to close sqlite database connection!");
            GoldenAge.getLogger().severe(e.getMessage());
            Bukkit.getPluginManager().disablePlugin(plugin);
            return;
        }
    }
}
