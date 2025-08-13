package com.mygame.jettypist;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Fireball {
    public float x, y;
    public float speed = 200f;
    public boolean active = true;
    private static final float FIREBALL_WIDTH = 16;
    private static final float FIREBALL_HEIGHT = 16;
    private static Texture fireballTexture;
    private float targetX, targetY;

    public Fireball(float startX, float startY, float targetX, float targetY) {
        this.x = startX;
        this.y = startY;
        this.targetX = targetX;
        this.targetY = targetY;
    }

    public static void setTexture(Texture texture) {
        fireballTexture = texture;
    }

    public void update(float delta) {
        if (!active) return;
        float dx = targetX - x;
        float dy = targetY - y;
        float dist = (float)Math.sqrt(dx*dx + dy*dy);
        if (dist < 10) {
            active = false;
            return;
        }
        x += (dx / dist) * speed * delta;
        y += (dy / dist) * speed * delta;
    }

    public void render(SpriteBatch batch) {
        if (active && fireballTexture != null) {
            batch.draw(fireballTexture, x, y, FIREBALL_WIDTH, FIREBALL_HEIGHT);
        }
    }

    public boolean collidesWithPlayer(float playerX, float playerY, float playerW, float playerH) {
        return active && x + FIREBALL_WIDTH > playerX && x < playerX + playerW && y + FIREBALL_HEIGHT > playerY && y < playerY + playerH;
    }
}
