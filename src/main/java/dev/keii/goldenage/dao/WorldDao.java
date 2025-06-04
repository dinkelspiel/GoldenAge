package dev.keii.goldenage.dao;

import dev.keii.goldenage.models.Login;
import dev.keii.goldenage.models.World;
import dev.keii.goldenage.utils.DatabaseUtility;

import javax.annotation.Nullable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.time.ZoneOffset;
import java.util.UUID;

public class WorldDao {
    private final DatabaseUtility db;

    public WorldDao(DatabaseUtility db) {
        this.db = db;
    }

    public void insertWorld(World world) {
        if (world.getId() != null) {
            throw new IllegalArgumentException("World ID must be null for new worlds");
        }

        try (PreparedStatement stmt = db.getConnection().prepareStatement("INSERT INTO worlds(world_name, uuid) VALUES(?, ?)")) {
            stmt.setString(1, world.getWorldName());
            stmt.setString(2, world.getUuid().toString());
            stmt.execute();
            stmt.close();

            db.getConnection().commit();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public @Nullable World getWorldByUuid(UUID uuid) {
        try {
            PreparedStatement stmt = db.getConnection().prepareStatement("SELECT id, world_name, uuid FROM worlds WHERE uuid = ? LIMIT 1");
            stmt.setString(1, uuid.toString());
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                World world = new World(rs.getInt(1), rs.getString(2), UUID.fromString(rs.getString(3)));
                rs.close();
                stmt.close();
                return world;
            }

            rs.close();
            stmt.close();
            return null;
        } catch (SQLException e) {
            return null;
        }
    }

    public @Nullable World getWorldById(int id) {
        try {
            PreparedStatement stmt = db.getConnection().prepareStatement("SELECT id, world_name, uuid FROM worlds WHERE id = ? LIMIT 1");
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                World world = new World(rs.getInt(1), rs.getString(2), UUID.fromString(rs.getString(3)));
                rs.close();
                stmt.close();
                return world;
            }

            rs.close();
            stmt.close();
            return null;
        } catch (SQLException e) {
            return null;
        }
    }
}
