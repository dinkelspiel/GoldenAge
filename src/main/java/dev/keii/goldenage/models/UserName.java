package dev.keii.goldenage.models;

import lombok.Getter;
import lombok.Setter;

import javax.annotation.Nullable;
import java.time.LocalDateTime;
import java.util.UUID;

public class UserName {
    @Getter @Setter
    private Integer id = null;
    @Getter
    private int userId;
    @Getter
    private User user;
    @Getter @Setter
    private String name;
    @Getter @Setter
    private LocalDateTime createdAt;

    public UserName(User user, String name, LocalDateTime createdAt)
    {
        this.userId = user.getId();
        this.name = name;
        this.createdAt = createdAt;
    }

    public UserName(int id, User user, String name, LocalDateTime createdAt)
    {
        this.id = id;
        this.userId = user.getId();
        this.name = name;
        this.createdAt = createdAt;
    }
}
