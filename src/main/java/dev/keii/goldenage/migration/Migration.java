package dev.keii.goldenage.migration;

import dev.keii.goldenage.utils.DatabaseUtility;

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

    public abstract String getName();

    public void migrate(int batch) throws SQLException {
        Statement stmt = db.getConnection().createStatement();
        if(!hasBeenMigrated()) {
            up(stmt);
            createMigrationEntry(batch);
        }
        db.getConnection().commit();
        stmt.close();
    }

    public void rollback(int batch) throws SQLException {
        Statement stmt = db.getConnection().createStatement();
        if(isInBatch(batch)) {
            down(stmt);
            createMigrationEntry(batch);
        }
        db.getConnection().commit();
        stmt.close();
    }

    public boolean hasBeenMigrated() throws SQLException {
        PreparedStatement stmt = db.getConnection().prepareStatement("SELECT * FROM migrations WHERE migration = ? LIMIT 1");
        stmt.setString(1, getName());
        stmt.executeQuery();
        ResultSet rs = stmt.getResultSet();
        stmt.close();

        return rs.next();
    }

    public boolean isInBatch(int batch) throws SQLException {
        PreparedStatement stmt = db.getConnection().prepareStatement("SELECT batch FROM migrations WHERE migration = ? LIMIT 1");
        stmt.setString(1, getName());
        stmt.executeQuery();
        ResultSet rs = stmt.getResultSet();
        stmt.close();

        if(rs.next())
        {
            return rs.getInt(1) == batch;
        }

        return false;
    }

    public void createMigrationEntry(int batch) throws SQLException {
        PreparedStatement stmt = db.getConnection().prepareStatement("INSERT INTO migrations(migration, batch) VALUES(?, ?);");
        stmt.setString(1, getName());
        stmt.setInt(2, batch);
        stmt.execute();
        db.getConnection().commit();
        stmt.close();
    }


    public void deleteMigrationEntry(int batch) throws SQLException {
        PreparedStatement stmt = db.getConnection().prepareStatement("DELETE FROM migrations WHERE migration = ?");
        stmt.setString(1, getName());
        stmt.execute();
        db.getConnection().commit();
        stmt.close();
    }
}
