
package com.mygame.jettypist;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.InputMultiplexer;
import java.util.ArrayList;
import java.util.List;

public class GameScreen implements Screen {
	private final JetTypist game;
	private SpriteBatch batch;
	private ParallaxBackground background;
	private List<Explosion> explosions;
	private SoundManager soundManager;
	private Texture playerTexture;
	private Texture heartFullTexture;
	private Texture heartEmptyTexture;
	private Texture enemyTexture;
	private Texture bulletTexture;
	private Texture fireballTexture;
	private BitmapFont font;
	private GlyphLayout layout;
	private SpawnManager spawnManager;
	private TypingController typingController;
	private List<Bullet> bullets;
	private List<Fireball> fireballs;
	private int lives = 5;
	private static final int MAX_LIVES = 5;
	private float elapsedTime = 0f;
	private int wpm = 0;
	private int hitRecord = 0;
	private int killCount = 0;
	private boolean gameOver = false;
	private int lastLifeRewardKillCount = 0;

	public GameScreen(JetTypist game) {
		this.game = game;
		batch = new SpriteBatch();
		background = new ParallaxBackground("images/clouds.png", 30f, 3);
		explosions = new ArrayList<>();
		soundManager = new SoundManager();
		playerTexture = new Texture("images/player.png");
		heartFullTexture = new Texture("images/heart_full.png");
		heartEmptyTexture = new Texture("images/heart_empty.png");
		enemyTexture = new Texture("images/enemy.png");
		bulletTexture = new Texture("images/bullet.png");
		fireballTexture = new Texture("images/fireball.png");
		Bullet.setTexture(bulletTexture);
		Fireball.setTexture(fireballTexture);
		font = new BitmapFont();
		layout = new GlyphLayout();
		spawnManager = new SpawnManager(enemyTexture, font);
		bullets = new ArrayList<>();
		fireballs = new ArrayList<>();
		typingController = new TypingController(spawnManager, bullets, soundManager);
		InputMultiplexer multiplexer = new InputMultiplexer();
		multiplexer.addProcessor(typingController);
		Gdx.input.setInputProcessor(multiplexer);
	}

	@Override
	public void render(float delta) {
		elapsedTime += delta;
		if (gameOver) {
			int finalWpm = wpm;
			int finalHit = hitRecord;
			int finalSeconds = (int)elapsedTime;
			game.showGameOver(finalWpm, finalHit, finalSeconds);
			return;
		}
		ScreenUtils.clear(0.15f, 0.15f, 0.2f, 1f);
		spawnManager.update(delta);
		// Update bullets
		for (Bullet bullet : bullets) {
			bullet.update(delta);
		}
		bullets.removeIf(b -> !b.active);

		// Update fireballs
		for (Fireball fireball : fireballs) {
			fireball.update(delta);
		}
		fireballs.removeIf(f -> !f.active);

		// Enemies fire
		float playerX = (Gdx.graphics.getWidth() - 64) / 2f;
		float playerY = 20;
		for (EnemyJet enemy : spawnManager.getEnemies()) {
			if (enemy.shouldFire(delta)) {
				float ex = enemy.x + EnemyJet.WIDTH / 2f;
				float ey = enemy.y;
				float px = playerX + 32;
				float py = playerY + 32;
				fireballs.add(new Fireball(ex, ey, px, py));
			}
		}

		// Check fireball collision with player
		for (Fireball fireball : fireballs) {
			if (fireball.collidesWithPlayer(playerX, playerY, 64, 64)) {
				fireball.active = false;
				if (lives > 0) {
					lives--;
					soundManager.playPlayerHit();
					if (lives <= 0) gameOver = true;
				}
			}
		}

		// Check enemy collision with player (remove and lose life)
		for (int i = spawnManager.getEnemies().size() - 1; i >= 0; i--) {
			EnemyJet enemy = spawnManager.getEnemies().get(i);
			if (enemy.y <= playerY) {
				spawnManager.getEnemies().remove(i);
				if (lives > 0) {
					lives--;
					if (lives <= 0) gameOver = true;
				}
			}
		}

		// Sync hit record, kill count from TypingController
		hitRecord = typingController.getHitRecord();
		killCount = typingController.getKillCount();

		// WPM calculation: (completedWords / elapsedSeconds) * 60
		if (elapsedTime > 0) {
			wpm = (int)((killCount / elapsedTime) * 60f);
		} else {
			wpm = 0;
		}

		// Reward: every 2 kills, add 1 life (max 5), only once per threshold
		if (killCount / 2 > lastLifeRewardKillCount / 2 && killCount % 2 == 0 && lives < MAX_LIVES) {
			lives++;
		}
		lastLifeRewardKillCount = killCount;

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
		// Draw fireballs
		for (Fireball fireball : fireballs) {
			fireball.render(batch);
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
		fireballTexture.dispose();
		font.dispose();
	}
}
