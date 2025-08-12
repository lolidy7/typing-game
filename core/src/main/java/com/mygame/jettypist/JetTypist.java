package com.mygame.jettypist;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class JetTypist extends ApplicationAdapter {
    private GameScreen gameScreen;

    @Override
    public void create() {
        gameScreen = new GameScreen();
    }

    @Override
    public void render() {
        gameScreen.render(Gdx.graphics.getDeltaTime());
    }

    @Override
    public void dispose() {
        gameScreen.dispose();
    }
}
