package dev.keii.goldenage.dao;

import dev.keii.goldenage.GoldenAge;
import dev.keii.goldenage.models.Login;
import dev.keii.goldenage.utils.DatabaseUtility;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.time.ZoneOffset;

public class LoginDao {
    private final DatabaseUtility db;

    public LoginDao(GoldenAge plugin) {
        this.db = plugin.getDatabaseUtility();
    }

    public long insertUser(Login login) {
        try(PreparedStatement stmt = db.getConnection().prepareStatement("INSERT INTO logins(uuid, created_at, updated_at) VALUES(?, ?, ?)")) {
            stmt.setString(1, login.getUuid().toString());
            stmt.setLong(2, login.getCreatedAt().toEpochSecond(ZoneOffset.UTC));
            stmt.setNull(3, Types.INTEGER);
            stmt.executeQuery();

            db.getConnection().commit();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<User> getAllUsers() {
        List<User> userList = new ArrayList<>();
        Cursor cursor = db.rawQuery("SELECT * FROM users", null);

        if (cursor.moveToFirst()) {
            do {
                User user = new User(
                        cursor.getInt(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3)
                );
                userList.add(user);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return userList;
    }

    public void close() {
        db.close();
    }
}
