package dev.keii.goldenage.migration.migrations;

import dev.keii.goldenage.migration.Migration;
import dev.keii.goldenage.migration.Migrator;

import java.sql.SQLException;
import java.sql.Statement;

public class V0004_CreateWorlds extends Migration {

    public V0004_CreateWorlds(Migrator migrator) {
        super(migrator);
    }

    @Override
    public void up(Statement stmt) throws SQLException {
        stmt.execute("CREATE TABLE IF NOT EXISTS worlds (\n" +
                "    id INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                "    world_name TEXT NOT NULL,\n" +
                "    uuid TEXT NOT NULL\n" +
                ");\n");
    }

    @Override
    public void down(Statement stmt) throws SQLException {
        stmt.execute("DROP TABLE IF EXISTS worlds;");
    }
}
