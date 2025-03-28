package dev.keii.goldenage.migration;

import dev.keii.goldenage.GoldenAge;
import dev.keii.goldenage.utils.DatabaseUtility;
import org.bukkit.Bukkit;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public abstract class Migration {
    public DatabaseUtility db;
    public Migrator migrator;

    public Migration(Migrator migrator)
    {
        this.db = migrator.db;
        this.migrator = migrator;
    }

    public abstract void up(Statement stmt) throws SQLException;
    public abstract void down(Statement stmt) throws SQLException;

    public void migrate(int batch) throws SQLException {
        Statement stmt = db.getConnection().createStatement();
        if(!hasBeenMigrated()) {
            up(stmt);
            createMigrationEntry(batch);
            GoldenAge.getLogger().info(this.getClass().getSimpleName());
        }
        db.getConnection().commit();
        stmt.close();
    }

    public void rollback(int batch) throws SQLException {
        Statement stmt = db.getConnection().createStatement();
        if(isInBatch(batch)) {
            down(stmt);
            deleteMigrationEntry();
            GoldenAge.getLogger().info(this.getClass().getSimpleName());
        }
        db.getConnection().commit();
        stmt.close();
    }

    public boolean hasBeenMigrated() throws SQLException {
        PreparedStatement stmt = db.getConnection().prepareStatement("SELECT * FROM migrations WHERE migration = ? LIMIT 1");
        stmt.setString(1, this.getClass().getSimpleName());
        ResultSet rs = stmt.executeQuery();
        boolean a = rs.next();
        stmt.close();
        rs.close();

        return a;
    }

    public boolean isInBatch(int batch) throws SQLException {
        PreparedStatement stmt = db.getConnection().prepareStatement("SELECT batch FROM migrations WHERE migration = ? LIMIT 1");
        stmt.setString(1, this.getClass().getSimpleName());
        ResultSet rs = stmt.executeQuery();

        if(rs.next())
        {
            boolean inBatch = rs.getInt(1) == batch;
            stmt.close();
            rs.close();

            return inBatch;
        }

        return false;
    }

    public void createMigrationEntry(int batch) throws SQLException {
        PreparedStatement stmt = db.getConnection().prepareStatement("INSERT INTO migrations(migration, batch) VALUES(?, ?);");
        stmt.setString(1, this.getClass().getSimpleName());
        stmt.setInt(2, batch);
        stmt.execute();
        db.getConnection().commit();
        stmt.close();
    }

    public void deleteMigrationEntry() throws SQLException {
        PreparedStatement stmt = db.getConnection().prepareStatement("DELETE FROM migrations WHERE migration = ?");
        stmt.setString(1, this.getClass().getSimpleName());
        stmt.execute();
        db.getConnection().commit();
        stmt.close();
    }
}
