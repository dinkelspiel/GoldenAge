package dev.keii.goldenage.migration.migrations;

import dev.keii.goldenage.migration.Migration;
import dev.keii.goldenage.migration.Migrator;

import java.sql.SQLException;
import java.sql.Statement;

public class V0005_CreateTransactions extends Migration {

    public V0005_CreateTransactions(Migrator migrator) {
        super(migrator);
    }

    @Override
    public void up(Statement stmt) throws SQLException {
        stmt.execute("CREATE TABLE IF NOT EXISTS container_transactions (\n" +
                "    id INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                "    user_id INTEGER NOT NULL,\n" +
                "    x INTEGER NOT NULL,\n" +
                "    y INTEGER NOT NULL,\n" +
                "    z INTEGER NOT NULL,\n" +
                "    world_id INTEGER NOT NULL,\n" +
                "    action INTEGER NOT NULL,\n" +
                "    item_id INTEGER NOT NULL,\n" +
                "    item_data INTEGER NOT NULL,\n" +
                "    item_count INTEGER NOT NULL,\n" +
                "    created_at INTEGER NOT NULL,\n" +
                "    FOREIGN KEY(user_id) REFERENCES users(id)\n" +
                "    FOREIGN KEY(world_id) REFERENCES worlds(id)\n" +
                ");\n");

        stmt.execute("CREATE TABLE IF NOT EXISTS block_transactions (\n" +
                "    id INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                "    actor INTEGER NOT NULL,\n" +
                "    user_id INTEGER,\n" +
                "    x INTEGER NOT NULL,\n" +
                "    y INTEGER NOT NULL,\n" +
                "    z INTEGER NOT NULL,\n" +
                "    world_id INTEGER NOT NULL,\n" +
                "    action INTEGER NOT NULL,\n" +
                "    block_id INTEGER NOT NULL,\n" +
                "    block_data INTEGER NOT NULL,\n" +
                "    created_at INTEGER NOT NULL,\n" +
                "    FOREIGN KEY(user_id) REFERENCES users(id)\n" +
                "    FOREIGN KEY(world_id) REFERENCES worlds(id)\n" +
                ");\n");

        stmt.execute("CREATE INDEX IF NOT EXISTS idx_container_xyz ON container_transactions (x, y, z);");
        stmt.execute("CREATE INDEX IF NOT EXISTS idx_block_xyz ON block_transactions (x, y, z);");
    }

    @Override
    public void down(Statement stmt) throws SQLException {
        stmt.execute("DROP TABLE IF EXISTS container_transactions;");
        stmt.execute("DROP TABLE IF EXISTS block_transactions;");
    }
}
