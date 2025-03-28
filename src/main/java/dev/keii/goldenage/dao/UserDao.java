package dev.keii.goldenage.dao;

import dev.keii.goldenage.GoldenAge;
import dev.keii.goldenage.models.Login;
import dev.keii.goldenage.models.User;
import dev.keii.goldenage.utils.DatabaseUtility;
import dev.keii.goldenage.utils.DateUtility;

import javax.annotation.Nullable;
import java.sql.*;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class UserDao {
    private final DatabaseUtility db;

    public UserDao(DatabaseUtility db) {
        this.db = db;
    }

    public void insertUser(User user) throws IllegalArgumentException {
        if(user.getId() != null)
        {
            throw new IllegalArgumentException("User ID must be null for new users");
        }

        try(PreparedStatement stmt = db.getConnection().prepareStatement("INSERT INTO user(uuid, created_at, updated_at) VALUES(?, ?, ?)")) {
            stmt.setString(1, user.getUuid().toString());
            stmt.setLong(2, user.getCreatedAt().toEpochSecond(ZoneOffset.UTC));
            stmt.setNull(3, Types.INTEGER);
            stmt.executeQuery();
            stmt.close();

            db.getConnection().commit();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public @Nullable User getUserById(int userId) {
        try {
            PreparedStatement stmt = db.getConnection().prepareStatement("SELECT id, uuid, created_at, updated_at FROM users WHERE id = ? LIMIT 1");
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();

            if(rs.next())
            {
                return new User(rs.getInt(1), UUID.fromString(rs.getString(2)), DateUtility.epochSecondsToDateTime(rs.getInt(3)), DateUtility.epochSecondsToDateTime(rs.getInt(4)));
            }

            rs.close();
            stmt.close();
            return null;
        } catch (SQLException e) {
            return null;
        }
    }

    public @Nullable User getUserByUuid(UUID uuid) {
        try {
            PreparedStatement stmt = db.getConnection().prepareStatement("SELECT id, uuid, created_at, updated_at FROM users WHERE uuid = ? LIMIT 1");
            stmt.setString(1, uuid.toString());
            ResultSet rs = stmt.executeQuery();

            if(rs.next())
            {
                return new User(rs.getInt(1), UUID.fromString(rs.getString(2)), DateUtility.epochSecondsToDateTime(rs.getInt(3)), DateUtility.epochSecondsToDateTime(rs.getInt(4)));
            }

            rs.close();
            stmt.close();
            return null;
        } catch (SQLException e) {
            return null;
        }
    }
}
