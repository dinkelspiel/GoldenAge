package dev.keii.goldenage.migration.migrations;

import dev.keii.goldenage.migration.Migration;
import dev.keii.goldenage.migration.Migrator;

import java.sql.SQLException;
import java.sql.Statement;

public class CreateUsers0001 extends Migration {

    public CreateUsers0001(Migrator migrator) {
        super(migrator);
    }

    @Override
    public String getName() {
        return "00002_createUsers";
    }

    @Override
    public void up(Statement stmt) throws SQLException {
        stmt.execute("CREATE TABLE IF NOT EXISTS users (\n" +
                "    id INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                "    uuid TEXT NOT NULL,\n" +
                "    created_at INTEGER NOT NULL,\n" +
                "    updated_at INTEGER\n" +
                ");\n" +
                "\n" +
                "CREATE INDEX idx_users_uuid ON users(uuid);");
    }

    @Override
    public void down(Statement stmt) throws SQLException {
        stmt.execute("DROP TABLE IF EXISTS users; DROP INDEX IF EXISTS idx_users_uuid;");
    }
}
