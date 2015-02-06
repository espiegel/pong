package com.pong.model.entity;

import com.pong.model.Rectangle;
import com.pong.model.Velocity;

/**
 * Created by Eidan on 1/31/2015.
 */
public abstract class Entity {
    public Rectangle bounds;
    public Velocity v = new Velocity(0,0);

    public Entity(Rectangle bounds) {
        this.bounds = bounds;
    }

    public void update() {
        bounds.x += v.x;
        bounds.y += v.y;
    }

}
