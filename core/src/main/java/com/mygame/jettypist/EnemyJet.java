package com.mygame.jettypist;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class EnemyJet {
    public float x, y;
    public String word;
    private Texture texture;
    private BitmapFont font;
    private GlyphLayout layout;
    public static final int WIDTH = 64, HEIGHT = 64;
    public float fireTimer = 0f;
    public static final float FIRE_INTERVAL = 5f;

    public EnemyJet(float x, float y, String word, Texture texture, BitmapFont font) {
        this.x = x;
        this.y = y;
        this.word = word;
        this.texture = texture;
        this.font = font;
        this.layout = new GlyphLayout();
    }

    public void render(SpriteBatch batch) {
        batch.draw(texture, x, y, WIDTH, HEIGHT);
        layout.setText(font, word);
        font.draw(batch, word, x + (WIDTH - layout.width) / 2, y + HEIGHT + layout.height + 5);
    }

    public boolean shouldFire(float delta) {
        fireTimer += delta;
        if (fireTimer >= FIRE_INTERVAL) {
            fireTimer = 0f;
            return true;
        }
        return false;
    }
}
