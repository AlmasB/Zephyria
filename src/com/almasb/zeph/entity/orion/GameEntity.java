package com.almasb.zeph.entity.orion;

import com.almasb.fxgl.entity.Entity;

public abstract class GameEntity implements java.io.Serializable {

    private static final long serialVersionUID = 924533834152552568L;


    /**
     * Object ID in database
     */
    public final int id;

    /**
     * name - in game name
     * description - info about object
     * textureName - file name of the texture in assets/textures/
     */
    public final String name, description, textureName;

    public GameEntity(int id, String name, String description, String textureName) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.textureName = textureName;
    }

    @Override
    public String toString() {
        return id + ":" + name;
    }

    public abstract Entity toEntity();
}
