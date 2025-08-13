package com.mygame.jettypist;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class JetTypist extends ApplicationAdapter {
    private Screen currentScreen;

    @Override
    public void create() {
        startGame();
    }

    private void startGame() {
    if (currentScreen != null) currentScreen.dispose();
    GameScreen gameScreen = new GameScreen(this);
    currentScreen = gameScreen;
    currentScreen.show();
    }

    public void showGameOver(final int wpm, final int hitRecord, final int seconds) {
        if (currentScreen != null) currentScreen.dispose();
        currentScreen = new GameOverScreen(wpm, hitRecord, seconds, new Runnable() {
            @Override
            public void run() {
                startGame();
            }
        });
        currentScreen.show();
    }

    @Override
    public void render() {
        if (currentScreen != null) currentScreen.render(Gdx.graphics.getDeltaTime());
    }

    @Override
    public void dispose() {
        if (currentScreen != null) currentScreen.dispose();
    }
}
