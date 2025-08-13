package com.mygame.jettypist;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Bullet {
    public float x, y;
    public float speed = 400f;
    public EnemyJet target;
    public int letterIndex; // Which letter this bullet represents
    public boolean active = true;
    private static final float BULLET_WIDTH = 8;
    private static final float BULLET_HEIGHT = 16;
    private static Texture bulletTexture;

    public Bullet(EnemyJet target, int letterIndex) {
        this.target = target;
        this.letterIndex = letterIndex;
        // Start at player position (bottom center)
        this.x = 400 - BULLET_WIDTH / 2f;
        this.y = 32; // Just above player sprite
    }

    public static void setTexture(Texture texture) {
        bulletTexture = texture;
    }

    public void update(float delta) {
        if (!active || target == null) return;
        // Move towards target
        float targetX = target.x + EnemyJet.WIDTH / 2f;
        float targetY = target.y + EnemyJet.HEIGHT / 2f;
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
        if (active && bulletTexture != null) {
            batch.draw(bulletTexture, x, y, BULLET_WIDTH, BULLET_HEIGHT);
        }
    }
}
