package dev.keii.goldenage.models;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

public class World {
    @Getter
    @Setter
    private Integer id = null;
    @Getter @Setter
    private String worldName;
    @Getter @Setter
    private UUID uuid;

    public World(String worldName, UUID uuid) {
        this.worldName = worldName;
        this.uuid = uuid;
    }

    public World(Integer id, String worldName, UUID uuid) {
        this.id = id;
        this.worldName = worldName;
        this.uuid = uuid;
    }
}
