package dev.keii.goldenage.betaprotect.models;

import dev.keii.goldenage.models.User;
import dev.keii.goldenage.models.World;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Material;

import javax.annotation.Nullable;
import java.time.LocalDateTime;

public class ContainerTransaction {
    @Getter
    @Setter
    private Integer id = null;
    @Getter
    private int userId;
    @Getter
    private User user;
    @Getter
    private int x, y, z;
    @Getter
    private int worldId;
    @Getter
    private World world;
    @Getter
    private TransactionAction action;
    @Getter
    private int itemId;
    @Getter
    private Material item;
    @Getter
    @Nullable
    private Integer itemData;
    @Getter
    private Integer itemCount;
    @Setter
    private LocalDateTime createdAt;

    public ContainerTransaction(
            int userId,
            User user,
            int x, int y, int z,
            int worldId,
            World world,
            TransactionAction action,
            int itemId,
            Material item,
            @Nullable Integer itemData,
            Integer itemCount,
            LocalDateTime createdAt
    ) {
        this.userId = userId;
        this.user = user;
        this.x = x;
        this.y = y;
        this.z = z;
        this.worldId = worldId;
        this.world = world;
        this.action = action;
        this.itemId = itemId;
        this.item = item;
        this.itemData = itemData;
        this.itemCount = itemCount;
        this.createdAt = createdAt;
    }

    public ContainerTransaction(
            Integer id,
            int userId,
            User user,
            int x, int y, int z,
            int worldId,
            World world,
            TransactionAction action,
            int itemId,
            Material item,
            @Nullable Integer itemData,
            Integer itemCount,
            LocalDateTime createdAt
    ) {
        this.id = id;
        this.userId = userId;
        this.user = user;
        this.x = x;
        this.y = y;
        this.z = z;
        this.worldId = worldId;
        this.world = world;
        this.action = action;
        this.itemId = itemId;
        this.item = item;
        this.itemData = itemData;
        this.itemCount = itemCount;
        this.createdAt = createdAt;
    }
}
