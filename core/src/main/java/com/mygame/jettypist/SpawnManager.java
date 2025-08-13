
package com.mygame.jettypist;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import java.util.List;
import java.util.ArrayList;
import java.util.Random;

public class SpawnManager {
    private float timer = 0f;
    private final Texture enemyTexture;
    private final BitmapFont font;
    private final String[] words = {"alpha", "bravo", "charlie", "delta", "echo", "foxtrot", "golf", "hotel", "india", "juliet"};
    private final float spawnInterval = 1.5f; // seconds between spawns
    private final float screenWidth = 800;
    private final float minX = 0;
    private final float maxX = screenWidth - EnemyJet.WIDTH;
    private final List<EnemyJet> enemies = new ArrayList<>();
    private final Random random = new Random();

    public SpawnManager(Texture enemyTexture, BitmapFont font) {
        this.enemyTexture = enemyTexture;
        this.font = font;
    }

    public void update(float delta) {
        timer += delta;
        if (timer >= spawnInterval) {
            timer = 0f;
            spawnEnemy();
        }
        for (EnemyJet enemy : enemies) {
            enemy.y -= 40 * delta; // Move down at 40 px/sec
        }
    }

    private void spawnEnemy() {
        // Find a non-overlapping x position
        float x;
        int tries = 0;
        boolean valid;
        do {
            valid = true;
            x = minX + random.nextFloat() * (maxX - minX);
            for (EnemyJet e : enemies) {
                if (Math.abs(x - e.x) < EnemyJet.WIDTH + 10) {
                    valid = false;
                    break;
                }
            }
            tries++;
        } while (!valid && tries < 100);
        float y = 480 - 80; // Near top, assuming height 480
        String word = words[random.nextInt(words.length)];
        // Clamp so word is fully visible
        GlyphLayout tempLayout = new GlyphLayout();
        tempLayout.setText(font, word);
        float wordWidth = tempLayout.width;
        float clampedX = x;
        float wordLeft = clampedX + (EnemyJet.WIDTH - wordWidth) / 2f;
        float wordRight = wordLeft + wordWidth;
        // Clamp so word is not off left edge
        if (wordLeft < 0) {
            clampedX += -wordLeft;
        }
        // Recalculate after left clamp
        wordLeft = clampedX + (EnemyJet.WIDTH - wordWidth) / 2f;
        wordRight = wordLeft + wordWidth;
        // Clamp so word is not off right edge
        if (wordRight > screenWidth) {
            clampedX -= (wordRight - screenWidth);
        }
        // Clamp so enemy is not off left/right edge
        clampedX = Math.max(minX, Math.min(clampedX, maxX));
        enemies.add(new EnemyJet(clampedX, y, word, enemyTexture, font));
    }

    public List<EnemyJet> getEnemies() {
        return enemies;
    }
}
