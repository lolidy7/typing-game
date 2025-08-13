
package com.mygame.jettypist;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import java.util.List;
import java.util.ArrayList;
import java.util.Random;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import com.badlogic.gdx.Gdx;

public class SpawnManager {
    private float timer = 0f;
    private float pauseTimer = 0f;
    private static final float PAUSE_AFTER_SCALE = 2.0f;
    private final Texture enemyTexture;
    private final BitmapFont font;
    private String[] easyWords = {"alpha", "bravo", "charlie", "delta", "echo", "foxtrot", "golf", "hotel", "india", "juliet"};
    private String[] mediumWords;
    private String[] hardWords;
    private String[] words;
    private float spawnInterval = 1.5f; // seconds between spawns
    private float enemySpeed = 40f;
    private final float screenWidth = 800;
    private final float minX = 0;
    private final float maxX = screenWidth - EnemyJet.WIDTH;
    private final List<EnemyJet> enemies = new ArrayList<>();
    private final Random random = new Random();
    private float elapsed = 0f;
    private int killCount = 0;
    private int difficultyLevel = 0; // 0: easy, 1: medium, 2: hard

    public SpawnManager(Texture enemyTexture, BitmapFont font) {
        this.enemyTexture = enemyTexture;
        this.font = font;
        this.words = easyWords;
        this.mediumWords = loadWords("words/medium.txt");
        this.hardWords = loadWords("words/hard.txt");
    }

    private String[] loadWords(String path) {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(Gdx.files.internal(path).read()));
            List<String> list = new ArrayList<>();
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (!line.isEmpty()) list.add(line);
            }
            reader.close();
            return list.toArray(new String[0]);
        } catch (Exception e) {
            return new String[] {"error"};
        }
    }

    public void update(float delta) {
        elapsed += delta;
        boolean scaled = false;
        if ((int)elapsed / 30 > difficultyLevel) {
            difficultyLevel++;
            scaled = true;
        }
        if (killCount / 20 > difficultyLevel) {
            difficultyLevel = killCount / 20;
            scaled = true;
        }
        if (scaled) {
            if (difficultyLevel == 1) {
                words = mediumWords;
                enemySpeed = 50f;
                spawnInterval = 1.0f;
            } else if (difficultyLevel >= 2) {
                words = hardWords;
                enemySpeed = 60f;
                spawnInterval = 0.7f;
            }
            pauseTimer = PAUSE_AFTER_SCALE;
        }

        boolean canSpawn = true;
        if (pauseTimer > 0f) {
            pauseTimer -= delta;
            canSpawn = false;
        }
        if (enemies.size() > 5) {
            canSpawn = false;
        }
        if (canSpawn) {
            timer += delta;
            if (timer >= spawnInterval) {
                timer = 0f;
                spawnEnemy();
            }
        }
        float playerX = 400; // Center X (player at bottom center)
        float playerY = 32;  // Player Y threshold
        for (int i = enemies.size() - 1; i >= 0; i--) {
            EnemyJet enemy = enemies.get(i);
            // Move toward player
            float dx = playerX - (enemy.x + EnemyJet.WIDTH / 2f);
            float dy = playerY - (enemy.y + EnemyJet.HEIGHT / 2f);
            float dist = (float)Math.sqrt(dx*dx + dy*dy);
            float speed = enemySpeed;
            if (dist > 1e-3) {
                enemy.x += (dx / dist) * speed * delta;
                enemy.y += (dy / dist) * speed * delta;
            }
            // Remove if reached player
            if (enemy.y <= playerY) {
                enemies.remove(i);
            }
        }
    }

    public void incrementKillCount() {
        killCount++;
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
