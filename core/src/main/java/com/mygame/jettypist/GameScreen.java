
package com.mygame.jettypist;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Input;
import java.util.ArrayList;
import java.util.List;

public class GameScreen implements Screen {
	private SpriteBatch batch;
	private Texture playerTexture;
	private Texture heartFullTexture;
	private Texture heartEmptyTexture;
	private Texture enemyTexture;
	private Texture bulletTexture;
	private BitmapFont font;
	private GlyphLayout layout;
	private SpawnManager spawnManager;
	private TypingController typingController;
	private List<Bullet> bullets;
	private int lives = 5;
	private int wpm = 72;
	private int hitRecord = 0;
	private int killCount = 0;

	public GameScreen() {
		batch = new SpriteBatch();
		playerTexture = new Texture("images/player.png");
		heartFullTexture = new Texture("images/heart_full.png");
		heartEmptyTexture = new Texture("images/heart_empty.png");
		enemyTexture = new Texture("images/enemy.png");
		bulletTexture = new Texture("images/bullet.png");
		Bullet.setTexture(bulletTexture);
		font = new BitmapFont();
		layout = new GlyphLayout();
		spawnManager = new SpawnManager(enemyTexture, font);
		bullets = new ArrayList<>();
		typingController = new TypingController(spawnManager, bullets);
		InputMultiplexer multiplexer = new InputMultiplexer();
		multiplexer.addProcessor(typingController);
		Gdx.input.setInputProcessor(multiplexer);
	}

	@Override
	public void render(float delta) {
		ScreenUtils.clear(0.15f, 0.15f, 0.2f, 1f);
		spawnManager.update(delta);
		// Update bullets
		for (Bullet bullet : bullets) {
			bullet.update(delta);
		}
		// Remove inactive bullets
		bullets.removeIf(b -> !b.active);

		// Sync lives, hit record, kill count from TypingController
		lives = typingController.getLives();
		hitRecord = typingController.getHitRecord();
		killCount = typingController.getKillCount();

		batch.begin();
		// Draw hearts (lives) in top-left
		int heartSize = 32;
		int heartPadding = 8;
		for (int i = 0; i < 5; i++) {
			Texture heart = i < lives ? heartFullTexture : heartEmptyTexture;
			batch.draw(heart, 10 + i * (heartSize + heartPadding), Gdx.graphics.getHeight() - heartSize - 10, heartSize, heartSize);
		}
		// Draw WPM in top-right
		String wpmText = "WPM: " + wpm;
		layout.setText(font, wpmText);
		font.draw(batch, wpmText, Gdx.graphics.getWidth() - layout.width - 20, Gdx.graphics.getHeight() - 20);
		// Draw Hit Record in top-center
		String hitText = "Hit Record: " + hitRecord;
		layout.setText(font, hitText);
		font.draw(batch, hitText, (Gdx.graphics.getWidth() - layout.width) / 2, Gdx.graphics.getHeight() - 20);
		// Draw enemy jets
		for (EnemyJet enemy : spawnManager.getEnemies()) {
			enemy.render(batch);
		}
		// Draw bullets
		for (Bullet bullet : bullets) {
			bullet.render(batch);
		}
		// Draw player at bottom-center
		int playerWidth = 64, playerHeight = 64;
		batch.draw(playerTexture, (Gdx.graphics.getWidth() - playerWidth) / 2, 20, playerWidth, playerHeight);
		batch.end();
	}

	@Override
	public void resize(int width, int height) {}
	@Override
	public void show() {}
	@Override
	public void hide() {}
	@Override
	public void pause() {}
	@Override
	public void resume() {}
	@Override
	public void dispose() {
		batch.dispose();
		playerTexture.dispose();
		heartFullTexture.dispose();
		heartEmptyTexture.dispose();
		enemyTexture.dispose();
		bulletTexture.dispose();
		font.dispose();
	}
}
