package dev.keii.goldenage.models;

import lombok.Getter;
import lombok.Setter;

import javax.annotation.Nullable;
import java.time.LocalDateTime;
import java.util.UUID;

public class Login {
    @Getter @Setter
    private int id;
    @Getter @Setter
    private UUID uuid;
    @Getter @Setter
    private LocalDateTime createdAt;
    @Getter @Setter @Nullable
    private LocalDateTime updatedAt;

    public Login(int id, UUID uuid, LocalDateTime createdAt, @Nullable LocalDateTime updatedAt)
    {
        this.id = id;
        this.uuid = uuid;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Login(int id, String uuid, LocalDateTime createdAt, @Nullable LocalDateTime updatedAt)
    {
        this.id = id;
        this.uuid = UUID.fromString(uuid);
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
}
