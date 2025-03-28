package dev.keii.goldenage.migration.migrations;

import dev.keii.goldenage.migration.Migration;
import dev.keii.goldenage.migration.Migrator;

import java.sql.SQLException;
import java.sql.Statement;

public class CreateLogins0002 extends Migration {
    public CreateLogins0002(Migrator migrator) {
        super(migrator);
    }

    @Override
    public String getName() {
        return "00001_createLogins";
    }

    @Override
    public void up(Statement stmt) throws SQLException {
        stmt.execute("CREATE TABLE IF NOT EXISTS logins (\n" +
                "    id INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                "    user_id INTEGER NOT NULL,\n" +
                "    created_at INTEGER NOT NULL,\n" +
                "    updated_at INTEGER\n" +
                "    FOREIGN KEY(user_id) REFERENCES users(id)\n" +
                ");");
    }

    @Override
    public void down(Statement stmt) throws SQLException {
        stmt.execute("DROP TABLE IF EXISTS logins;");
    }
}
