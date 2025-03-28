package dev.keii.goldenage.migration;

import dev.keii.goldenage.GoldenAge;
import dev.keii.goldenage.migration.migrations.V0002_CreateLogins;
import dev.keii.goldenage.migration.migrations.V0001_CreateUsers;
import dev.keii.goldenage.migration.migrations.V0003_CreateUserNames;
import dev.keii.goldenage.utils.DatabaseUtility;
import org.bukkit.Bukkit;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Migrator {
    public DatabaseUtility db;
    private final List<Migration> migrations;
    private GoldenAge plugin;

    public Migrator(GoldenAge plugin)
    {
        this.plugin = plugin;
        this.db = plugin.getDatabaseUtility();
        migrations = new ArrayList<>();
        migrations.add(new V0001_CreateUsers(this));
        migrations.add(new V0002_CreateLogins(this));
        migrations.add(new V0003_CreateUserNames(this));
    }

    public void setupMigrationsTable() throws SQLException {
        Statement stmt = db.getConnection().createStatement();
        stmt.execute("CREATE TABLE IF NOT EXISTS migrations( id INTEGER PRIMARY KEY AUTOINCREMENT, migration TEXT NOT NULL, batch INTEGER NOT NULL )");
        db.getConnection().commit();
        stmt.close();
    }

    public int getLatestBatch() throws SQLException {
        Statement stmt = db.getConnection().createStatement();
        ResultSet rs = stmt.executeQuery("SELECT batch FROM migrations ORDER BY id DESC LIMIT 1");

        if(rs.next())
        {
            int latestBatch = rs.getInt(1);
            rs.close();
            stmt.close();
            return latestBatch;
        }
        rs.close();
        stmt.close();
        return 0;
    }

    public void migrate() throws SQLException {
        int newBatch = getLatestBatch() + 1;

        GoldenAge.getLogger().info("Migrate");
        for (Migration migration : migrations)
        {
            migration.migrate(newBatch);
        }
    }

    public void rollback() throws SQLException {
        int latestBatch = getLatestBatch();

        GoldenAge.getLogger().info("Rollback");

        Collections.reverse(migrations);
        for (Migration migration : migrations)
        {
            migration.rollback(latestBatch);
        }
        Collections.reverse(migrations);
    }
}
