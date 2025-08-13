package com.mygame.jettypist;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;

public class SoundManager {
    private Sound shootSound;
    private Sound enemyHitSound;
    private Sound playerHitSound;

    public SoundManager() {
        shootSound = Gdx.audio.newSound(Gdx.files.internal("sounds/shoot.wav"));
        enemyHitSound = Gdx.audio.newSound(Gdx.files.internal("sounds/enemy_hit.wav"));
        playerHitSound = Gdx.audio.newSound(Gdx.files.internal("sounds/player_hit.wav"));
    }

    public void playShoot() {
        shootSound.play(0.5f);
    }

    public void playEnemyHit() {
        enemyHitSound.play(0.6f);
    }

    public void playPlayerHit() {
        playerHitSound.play(0.7f);
    }

    public void dispose() {
        shootSound.dispose();
        enemyHitSound.dispose();
        playerHitSound.dispose();
    }
}
