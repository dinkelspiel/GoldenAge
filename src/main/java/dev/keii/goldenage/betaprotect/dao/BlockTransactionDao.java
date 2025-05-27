package dev.keii.goldenage.betaprotect.dao;

import dev.keii.goldenage.betaprotect.models.BlockTransaction;
import dev.keii.goldenage.utils.DatabaseUtility;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.time.ZoneOffset;

public class BlockTransactionDao {
    private final DatabaseUtility db;

    public BlockTransactionDao(DatabaseUtility db) {
        this.db = db;
    }

    public void insertBlockTransaction(BlockTransaction blockTransaction) {
        if (blockTransaction.getId() != null) {
            throw new IllegalArgumentException("BlockTransaction ID must be null for new block transactions");
        }

        try (PreparedStatement stmt = db.getConnection().prepareStatement("INSERT INTO block_transactions(actor, user_id, x, y, z, world_id, action, block_id, block_data, created_at) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)")) {
            stmt.setInt(1, blockTransaction.getActor().toInt());
            if(blockTransaction.getUser() != null)
                stmt.setInt(2, blockTransaction.getUser().getId());
            else
                stmt.setNull(2, Types.INTEGER);
            stmt.setInt(3, blockTransaction.getX());
            stmt.setInt(4, blockTransaction.getY());
            stmt.setInt(5, blockTransaction.getZ());
            stmt.setInt(6, blockTransaction.getWorldId());
            stmt.setInt(7, blockTransaction.getAction().toInt());
            stmt.setInt(8, blockTransaction.getBlockId());
            stmt.setInt(9, blockTransaction.getBlockData());
            stmt.setInt(10, (int) blockTransaction.getCreatedAt().toEpochSecond(ZoneOffset.UTC));
            stmt.execute();
            stmt.close();

            db.getConnection().commit();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
