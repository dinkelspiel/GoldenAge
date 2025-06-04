package dev.keii.goldenage.betaprotect.models;

import dev.keii.goldenage.models.User;
import dev.keii.goldenage.models.World;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.block.Block;
import org.jetbrains.annotations.Nullable;

import java.time.LocalDateTime;

public class BlockTransaction {
    @Getter
    @Setter
    private Integer id = null;
    @Getter
    private TransactionActor actor;
    @Getter
    @Nullable
    private Integer userId = null;
    @Getter
    @Nullable
    private User user = null;
    @Getter
    private int x, y, z;
    @Getter
    private int worldId;
    @Getter
    private World world;
    @Getter
    private TransactionAction action;
    @Getter
    private int blockId;
    @Getter
    @Nullable
    private Block block;
    @Getter
    private byte blockData;
    @Setter
    @Getter
    private LocalDateTime createdAt;

    public BlockTransaction(
                            TransactionActor actor, TransactionAction action, @Nullable User user, Block block, World world, LocalDateTime createdAt
    ) {
        this.actor = actor;
        if (user != null)
            this.userId = user.getId();
        this.user = user;
        this.x = block.getX();
        this.y = block.getY();
        this.z = block.getZ();
        this.worldId = world.getId();
        this.world = world;
        this.action = action;
        this.blockId = block.getTypeId();
        this.block = block;
        this.blockData = block.getData();
        this.createdAt = createdAt;
    }

    public BlockTransaction(
                            int id, TransactionActor actor, TransactionAction action, @Nullable User user, int x, int y, int z, int blockTypeId, byte blockData, World world, LocalDateTime createdAt
    ) {
        this.id = id;
        this.actor = actor;
        if (user != null)
            this.userId = user.getId();
        this.user = user;
        this.x = x;
        this.y = y;
        this.z = z;
        this.worldId = world.getId();
        this.world = world;
        this.action = action;
        this.blockId = blockTypeId;
        this.block = null;
        this.blockData = blockData;
        this.createdAt = createdAt;
    }
}
