package dev.keii.goldenage.models;

import lombok.Getter;
import lombok.Setter;

import javax.annotation.Nullable;
import java.time.LocalDateTime;
import java.util.UUID;

public class User {
    @Getter @Setter
    private Integer id = null;
    @Getter @Setter
    private UUID uuid;
    @Getter @Setter
    private LocalDateTime createdAt;
    @Getter @Setter @Nullable
    private LocalDateTime updatedAt;

    public User(UUID uuid, LocalDateTime createdAt, @Nullable LocalDateTime updatedAt)
    {
        this.uuid = uuid;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public User(int id, UUID uuid, LocalDateTime createdAt, @Nullable LocalDateTime updatedAt)
    {
        this.id = id;
        this.uuid = uuid;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
}
