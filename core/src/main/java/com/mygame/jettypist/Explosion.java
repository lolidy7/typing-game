package com.mygame.jettypist;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;

public class Explosion {
    private static final int FRAME_COLS = 4;
    private static final int FRAME_ROWS = 1;
    private static final float ANIMATION_DURATION = 0.5f;
    
    private final Texture texture;
    private final Vector2 position;
    private float stateTime;
    private boolean isActive;
    private final float frameTime;
    private int currentFrame;
    private final float width;
    private final float height;

    public Explosion(float x, float y, float size) {
        texture = new Texture("images/explosion.png");
        position = new Vector2(x - size/2, y - size/2);
        stateTime = 0;
        isActive = true;
        frameTime = ANIMATION_DURATION / (FRAME_COLS * FRAME_ROWS);
        currentFrame = 0;
        width = size;
        height = size;
    }

    public void update(float delta) {
        if (!isActive) return;
        
        stateTime += delta;
        currentFrame = (int)(stateTime / frameTime);
        
        if (currentFrame >= FRAME_COLS * FRAME_ROWS) {
            isActive = false;
        }
    }

    public void render(SpriteBatch batch) {
        if (!isActive) return;

        int frameX = currentFrame % FRAME_COLS;
        int frameY = currentFrame / FRAME_COLS;
        
        float u = (float)frameX / FRAME_COLS;
        float v = (float)frameY / FRAME_ROWS;
        float u2 = u + 1f/FRAME_COLS;
        float v2 = v + 1f/FRAME_ROWS;

        batch.draw(texture, 
                  position.x, position.y,
                  width, height,
                  u, v,
                  u2, v2);
    }

    public boolean isActive() {
        return isActive;
    }

    public void dispose() {
        texture.dispose();
    }
}
