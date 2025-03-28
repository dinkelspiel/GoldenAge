package dev.keii.goldenage.migration.migrations;

import dev.keii.goldenage.migration.Migration;
import dev.keii.goldenage.migration.Migrator;

import java.sql.SQLException;
import java.sql.Statement;

public class V0003_CreateUserNames extends Migration {

    public V0003_CreateUserNames(Migrator migrator) {
        super(migrator);
    }

    @Override
    public void up(Statement stmt) throws SQLException {
        stmt.execute("CREATE TABLE IF NOT EXISTS user_names (\n" +
                "    id INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                "    user_id INTEGER NOT NULL,\n" +
                "    name TEXT NOT NULL,\n" +
                "    created_at INTEGER NOT NULL,\n" +
                "    FOREIGN KEY(user_id) REFERENCES users(id)\n" +
                ");\n");
    }

    @Override
    public void down(Statement stmt) throws SQLException {
        stmt.execute("DROP TABLE IF EXISTS user_names;");
    }
}
