package com.mygame.jettypist;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import java.util.List;

public class TypingController extends InputAdapter {
    private EnemyJet targetEnemy = null;
    private int wordProgress = 0;
    private final SpawnManager spawnManager;
    private final List<Bullet> bullets;
    private int killCount = 0;
    private int hitRecord = 0;
    private int lives = 5;
    private static final int MAX_LIVES = 5;

    public TypingController(SpawnManager spawnManager, List<Bullet> bullets) {
        this.spawnManager = spawnManager;
        this.bullets = bullets;
    }

    public int getKillCount() { return killCount; }
    public int getHitRecord() { return hitRecord; }
    public int getLives() { return lives; }

    @Override
    public boolean keyTyped(char character) {
        if (!Character.isLetter(character)) return false;
        character = Character.toLowerCase(character);
        // If current target is removed or reaches player, reset lock and remove enemy
        if (targetEnemy != null) {
            boolean notPresent = !spawnManager.getEnemies().contains(targetEnemy);
            boolean reachedPlayer = targetEnemy.y <= 32; // Player Y threshold (player at y=20, height=64)
            if (notPresent || reachedPlayer) {
                spawnManager.getEnemies().remove(targetEnemy);
                targetEnemy = null;
                wordProgress = 0;
            }
        }
        if (targetEnemy == null) {
            for (EnemyJet enemy : spawnManager.getEnemies()) {
                if (enemy.word.length() > 0 && enemy.word.charAt(0) == character) {
                    targetEnemy = enemy;
                    wordProgress = 1;
                    fireBullet(targetEnemy, 0);
                    // Remove first letter from word
                    targetEnemy.word = targetEnemy.word.substring(1);
                    if (targetEnemy.word.length() == 0) {
                        spawnManager.getEnemies().remove(targetEnemy);
                        killCount++;
                        hitRecord++;
                        if (killCount % 2 == 0 && lives < MAX_LIVES) {
                            lives++;
                        }
                        targetEnemy = null;
                        wordProgress = 0;
                    }
                    return true;
                }
            }
        } else {
            if (targetEnemy.word.length() > 0 && targetEnemy.word.charAt(0) == character) {
                fireBullet(targetEnemy, 0);
                targetEnemy.word = targetEnemy.word.substring(1);
                if (targetEnemy.word.length() == 0) {
                    spawnManager.getEnemies().remove(targetEnemy);
                    killCount++;
                    hitRecord++;
                    if (killCount % 2 == 0 && lives < MAX_LIVES) {
                        lives++;
                    }
                    targetEnemy = null;
                    wordProgress = 0;
                }
                return true;
            }
        }
        return false;
    }

    private void fireBullet(EnemyJet enemy, int letterIndex) {
        // Add a bullet aimed at the enemy (optionally at the letterIndex position)
        bullets.add(new Bullet(enemy, letterIndex));
    }
}
