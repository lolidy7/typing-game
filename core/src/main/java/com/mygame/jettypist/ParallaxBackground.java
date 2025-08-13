package com.mygame.jettypist;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.Gdx;

public class ParallaxBackground {
    private final Texture texture;
    private final float scrollSpeed;
    private float[] offsets;
    private final int layerCount;

    public ParallaxBackground(String texturePath, float scrollSpeed, int layerCount) {
        this.texture = new Texture(texturePath);
        this.texture.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);
        this.scrollSpeed = scrollSpeed;
        this.layerCount = layerCount;
        this.offsets = new float[layerCount];
        
        // Initialize different offsets for each layer
        for (int i = 0; i < layerCount; i++) {
            offsets[i] = 0;
        }
    }

    public void update(float delta) {
        for (int i = 0; i < layerCount; i++) {
            // Each layer moves at a different speed for parallax effect
            float layerSpeed = scrollSpeed * (1f - (float)i / layerCount);
            offsets[i] = (offsets[i] + layerSpeed * delta) % texture.getHeight();
        }
    }

    public void render(SpriteBatch batch) {
    int screenWidth = Gdx.graphics.getWidth();
    int screenHeight = Gdx.graphics.getHeight();

    for (int i = 0; i < layerCount; i++) {
        // Adjust alpha for each layer to create depth
        float alpha = 1.0f - (float)i / (layerCount * 2f);
        batch.setColor(1, 1, 1, alpha);
        // Draw the scrolling texture
        batch.draw(texture,
            0, 0,                           // Position
            screenWidth, screenHeight,      // Size (now int)
            0, (int)offsets[i],            // Source position (offsets[i] as int)
            texture.getWidth(), texture.getHeight(), // Source size
            false, false);                 // Flip
    }
        batch.setColor(1, 1, 1, 1);  // Reset color
    }

    public void dispose() {
        texture.dispose();
    }
}
