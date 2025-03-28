package dev.keii.goldenage.migration;

import dev.keii.goldenage.migration.migrations.CreateLogins0002;
import dev.keii.goldenage.migration.migrations.CreateUsers0001;
import dev.keii.goldenage.utils.DatabaseUtility;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Migrator {
    public DatabaseUtility db;

    public Migrator(DatabaseUtility db)
    {
        this.db = db;
    }

    public void setupMigrationsTable() throws SQLException {
        Statement stmt = db.getConnection().createStatement();
        stmt.execute("CREATE TABLE IF NOT EXISTS migrations( id INTEGER PRIMARY KEY AUTOINCREMENT, migration TEXT NOT NULL, batch INTEGER NOT NULL )");
        db.getConnection().commit();
        stmt.close();
    }

    public int getLatestBatch() throws SQLException {
        Statement stmt = db.getConnection().createStatement();
        stmt.executeQuery("SELECT batch FROM migrations ORDER BY id DESC LIMIT 1");
        ResultSet rs = stmt.getResultSet();
        stmt.close();

        if(rs.next())
        {
            return rs.getInt(1);
        } else {
            return 0;
        }
    }

    public void migrate() throws SQLException {
        int newBatch = getLatestBatch() + 1;

        new CreateUsers0001(this).migrate(newBatch);
        new CreateLogins0002(this).migrate(newBatch);
    }

    public void rollback() throws SQLException {
        int latestBatch = getLatestBatch();

        new CreateLogins0002(this).rollback(latestBatch);
        new CreateUsers0001(this).rollback(latestBatch);
    }
}
