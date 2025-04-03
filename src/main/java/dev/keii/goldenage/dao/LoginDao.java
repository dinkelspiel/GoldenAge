package dev.keii.goldenage.dao;

import dev.keii.goldenage.models.Login;
import dev.keii.goldenage.models.User;
import dev.keii.goldenage.utils.DatabaseUtility;
import dev.keii.goldenage.utils.DateUtility;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;

public class LoginDao {
    private final DatabaseUtility db;

    public LoginDao(DatabaseUtility db) {
        this.db = db;
    }

    public void insertLogin(Login login) {
        if (login.getId() != null) {
            throw new IllegalArgumentException("Login ID must be null for new logins");
        }

        try (PreparedStatement stmt = db.getConnection().prepareStatement("INSERT INTO logins(user_id, created_at, updated_at) VALUES(?, ?, ?)")) {
            stmt.setInt(1, login.getUserId());
            stmt.setInt(2, (int) login.getCreatedAt().toEpochSecond(ZoneOffset.UTC));
            stmt.setNull(3, Types.INTEGER);
            stmt.execute();
            stmt.close();

            db.getConnection().commit();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Login getLatestLoginByUser(User user) {
        try {
            PreparedStatement stmt = db.getConnection().prepareStatement("SELECT id, user_id, created_at FROM logins WHERE user_id = ? ORDER BY id DESC LIMIT 1");
            stmt.setInt(1, user.getId());
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                int loginId = rs.getInt(1);
                LocalDateTime timestamp = DateUtility.epochSecondsToDateTime(rs.getInt(3));
                rs.close();
                stmt.close();
                return new Login(loginId, user, timestamp);
            }

            rs.close();
            stmt.close();
            return null;
        } catch (SQLException e) {
            return null;
        }
    }

    public List<Login> getLoginsByUser(User user, int limit) {
        try {
            List<Login> loginList = new ArrayList<>();
            PreparedStatement stmt = db.getConnection().prepareStatement("SELECT id, user_id, created_at FROM logins WHERE user_id = ? ORDER BY id DESC LIMIT " + limit);
            stmt.setInt(1, user.getId());
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                int loginId = rs.getInt(1);
                LocalDateTime timestamp = DateUtility.epochSecondsToDateTime(rs.getInt(3));
                loginList.add(new Login(loginId, user, timestamp));
            }

            rs.close();
            stmt.close();
            return loginList;
        } catch (SQLException e) {
            return null;
        }
    }

    public List<Login> getLogins(int limit) {
        try {
            List<Login> loginList = new ArrayList<>();
            PreparedStatement stmt = db.getConnection().prepareStatement("SELECT id, user_id, created_at FROM logins GROUP BY user_id ORDER BY id DESC LIMIT " + limit);
            ResultSet rs = stmt.executeQuery();
            UserDao userDao = new UserDao(this.db);

            while (rs.next()) {
                int loginId = rs.getInt(1);
                User user = userDao.getUserById(rs.getInt(2));
                if(user == null)
                {
                    throw new Exception("User was not found by id " + rs.getInt(2));
                }
                LocalDateTime timestamp = DateUtility.epochSecondsToDateTime(rs.getInt(3));
                loginList.add(new Login(loginId, user, timestamp));
            }

            rs.close();
            stmt.close();
            return loginList;
        } catch (SQLException e) {
            return null;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
