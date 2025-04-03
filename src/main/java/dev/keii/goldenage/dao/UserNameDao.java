package dev.keii.goldenage.dao;

import dev.keii.goldenage.models.User;
import dev.keii.goldenage.models.UserName;
import dev.keii.goldenage.utils.DatabaseUtility;
import dev.keii.goldenage.utils.DateUtility;

import javax.annotation.Nullable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.ZoneOffset;

public class UserNameDao {
    private final DatabaseUtility db;

    public UserNameDao(DatabaseUtility db) {
        this.db = db;
    }

    public void insertUserName(UserName userName) throws IllegalArgumentException {
        if (userName.getId() != null) {
            throw new IllegalArgumentException("UserName ID must be null for new usernames");
        }

        try (PreparedStatement stmt = db.getConnection().prepareStatement("INSERT INTO user_names(user_id, name, created_at) VALUES(?, ?, ?)")) {
            stmt.setInt(1, userName.getUserId());
            stmt.setString(2, userName.getName());
            stmt.setLong(3, userName.getCreatedAt().toEpochSecond(ZoneOffset.UTC));
            stmt.execute();
            stmt.close();

            db.getConnection().commit();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public @Nullable UserName getUserNameByName(String name) {
        try {
            PreparedStatement stmt = db.getConnection().prepareStatement("SELECT id, user_id, name, created_at FROM user_names WHERE name = ? LIMIT 1");
            stmt.setString(1, name);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                UserDao userDao = new UserDao(db);
                User user = userDao.getUserById(rs.getInt(2));
                if (user == null) {
                    throw new Exception("User relationship failed for username");
                }
                UserName userName = new UserName(rs.getInt(1), user, rs.getString(3), DateUtility.epochSecondsToDateTime(rs.getInt(4)));
                rs.close();
                stmt.close();
                return userName;
            }

            rs.close();
            stmt.close();
            return null;
        } catch (SQLException e) {
            return null;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public @Nullable UserName getUserNameByUser(User user) {
        try {
            PreparedStatement stmt = db.getConnection().prepareStatement("SELECT id, user_id, name, created_at FROM user_names WHERE user_id = ? LIMIT 1");
            stmt.setInt(1, user.getId());
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                UserName userName = new UserName(rs.getInt(1), user, rs.getString(3), DateUtility.epochSecondsToDateTime(rs.getInt(4)));
                rs.close();
                stmt.close();
                return userName;
            }

            rs.close();
            stmt.close();
            return null;
        } catch (SQLException e) {
            return null;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
