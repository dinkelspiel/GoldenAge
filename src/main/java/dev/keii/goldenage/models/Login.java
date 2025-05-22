package dev.keii.goldenage.models;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

public class Login {
    @Getter @Setter
    private Integer id = null;
    @Getter
    private int userId;
    @Getter
    private User user;
    @Getter @Setter
    private LocalDateTime createdAt;

    public Login(User user, LocalDateTime createdAt)
    {
        this.userId = user.getId();
        this.user = user;
        this.createdAt = createdAt;
    }

    public Login(int id, User user, LocalDateTime createdAt)
    {
        this.id = id;
        this.userId = user.getId();
        this.user = user;
        this.createdAt = createdAt;
    }
}
