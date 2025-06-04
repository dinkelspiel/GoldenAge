package dev.keii.goldenage.betaprotect.dao;

import dev.keii.goldenage.dao.UserDao;
import dev.keii.goldenage.dao.WorldDao;
import dev.keii.goldenage.models.User;
import dev.keii.goldenage.models.World;
import dev.keii.goldenage.utils.DatabaseUtility;
import dev.keii.goldenage.utils.DateUtility;
import dev.keii.goldenage.betaprotect.models.*;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.block.Block;

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
            if (blockTransaction.getUser() != null)
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

    public List<BlockTransaction> getBlockTransactionsAtLocation(int x, int y, int z, int amount, int page) {
        List<BlockTransaction> transactionList = new ArrayList<>();

        try (PreparedStatement stmt = db.getConnection().prepareStatement("SELECT id, actor, user_id, x, y, z, world_id, action, block_id, block_data, created_at FROM block_transactions WHERE x = ? AND y = ? AND z = ? ORDER BY created_at DESC LIMIT ? OFFSET ?")) {
            stmt.setInt(1, x);
            stmt.setInt(2, y);
            stmt.setInt(3, z);

            stmt.setInt(4, amount);
            stmt.setInt(5, page * amount);

            ResultSet rs = stmt.executeQuery();


            while (rs.next()) {
                int userId = rs.getInt(3);
                User user = null;
                if (!rs.wasNull()) {
                    UserDao userDao = new UserDao(this.db);
                    user = userDao.getUserById(userId);

                    if (user == null) {
                        throw new Exception("User was not found in DB");
                    }
                }

                WorldDao worldDao = new WorldDao(this.db);
                World world = worldDao.getWorldById(rs.getInt(7));

                if (world == null) {
                    throw new Exception("World was not found in DB");
                }

                BlockTransaction blockTransaction = new BlockTransaction(rs.getInt(1), TransactionActor.fromInt(rs.getInt(2)), TransactionAction.fromInt(rs.getInt(8)), user, rs.getInt(4), rs.getInt(5), rs.getInt(6), rs.getInt(9), (byte) rs.getInt(10), world, DateUtility.epochSecondsToDateTime(rs.getInt(11)));
                transactionList.add(blockTransaction);
            }

            rs.close();
            stmt.close();
            return transactionList;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
