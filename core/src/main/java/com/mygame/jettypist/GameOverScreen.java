package com.mygame.jettypist;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
// ...existing code...
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;

public class GameOverScreen implements Screen {
    private final SpriteBatch batch;
    private final BitmapFont font;
    private final GlyphLayout layout;
    private final Stage stage;
    private final TextButton restartButton;
    private final int finalWpm;
    private final int finalHitRecord;
    private final int finalSeconds;
    private final Runnable onRestart;

    public GameOverScreen(int wpm, int hitRecord, int seconds, Runnable onRestart) {
        this.batch = new SpriteBatch();
        this.font = new BitmapFont();
        this.layout = new GlyphLayout();
        this.finalWpm = wpm;
        this.finalHitRecord = hitRecord;
        this.finalSeconds = seconds;
        this.onRestart = onRestart;
        this.stage = new Stage();
        Skin skin = new Skin(Gdx.files.internal("uiskin.json"));
        restartButton = new TextButton("Restart", skin);
        restartButton.setPosition(Gdx.graphics.getWidth() / 2f - 60, Gdx.graphics.getHeight() / 2f - 80);
        restartButton.setSize(120, 40);
        restartButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
        GameOverScreen.this.onRestart.run();
            }
        });
        stage.addActor(restartButton);
    // ...existing code...
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0, 1);
        batch.begin();
        String over = "GAME OVER";
        layout.setText(font, over);
        font.draw(batch, over, (Gdx.graphics.getWidth() - layout.width) / 2, Gdx.graphics.getHeight() - 60);
        String wpmText = "Final WPM: " + finalWpm;
        layout.setText(font, wpmText);
        font.draw(batch, wpmText, (Gdx.graphics.getWidth() - layout.width) / 2, Gdx.graphics.getHeight() - 120);
        String hitText = "Final Hit Record: " + finalHitRecord;
        layout.setText(font, hitText);
        font.draw(batch, hitText, (Gdx.graphics.getWidth() - layout.width) / 2, Gdx.graphics.getHeight() - 160);
        String timeText = "Total Survival Time: " + finalSeconds + "s";
        layout.setText(font, timeText);
        font.draw(batch, timeText, (Gdx.graphics.getWidth() - layout.width) / 2, Gdx.graphics.getHeight() - 200);
        batch.end();
        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {}
    @Override
    public void pause() {}
    @Override
    public void resume() {}
    @Override
    public void hide() {}
    @Override
    public void dispose() {
        batch.dispose();
        font.dispose();
        stage.dispose();
    }
}
