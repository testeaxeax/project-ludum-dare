package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.TimeUtils;

public final class SplashScreen implements Screen {

	private static final int CAM_WIDTH = Project.SCREEN_WIDTH;
	private static final int CAM_HEIGHT = Project.SCREEN_HEIGHT;
	// SplashScreen will be displayed for at least 5 seconds
	private static final int MIN_SHOWTIME = 1000;

	private Project game;
	private OrthographicCamera cam;
	private Texture t;
	// Used to center the text
	private GlyphLayout layout;
	private long showtime;

	public SplashScreen(Project game) {
		this.game = game;
		showtime = 0;
		cam = new OrthographicCamera();
		cam.setToOrtho(false, CAM_WIDTH, CAM_HEIGHT);
		cam.update();
		game.spritebatch.setProjectionMatrix(cam.combined);
		Gdx.gl.glClearColor(1, 0, 0, 1);
		t = game.assetmanager.easyget("graphics/splash.jpg", Texture.class);
		layout = new GlyphLayout();
	}

	@Override
	public void show() {
		GameScreen.prefetch(game.assetmanager);
		MainMenuScreen.prefetch(game.assetmanager);
		showtime = TimeUtils.millis();
	}

	@Override
	public void render(float delta) {
		checkprogress();
		String text = "Progress: " + game.assetmanager.getProgress() * 100 + '%';
		layout.setText(game.font, text);
		final Vector2 pos = new Vector2((CAM_WIDTH / 2) - (layout.width / 2), (CAM_HEIGHT / 4) - (layout.height / 2));
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		game.spritebatch.begin();
		game.spritebatch.draw(t, 0, 0, Project.SCREEN_WIDTH, Project.SCREEN_HEIGHT);
		game.font.draw(game.spritebatch, layout, pos.x, pos.y);
		game.spritebatch.end();
	}

	private void checkprogress() {
		boolean loaded = game.assetmanager.update();
		if (TimeUtils.timeSinceMillis(showtime) >= MIN_SHOWTIME && loaded) {
			// TODO replace ScreenTemplate with actual game/menu screen
			game.screenmanager.set(new MainMenuScreen(game));
			dispose();
		}
	}

	@Override
	public void resize(int width, int height) {

	}

	@Override
	public void pause() {

	}

	@Override
	public void resume() {

	}

	@Override
	public void hide() {

	}

	@Override
	public void dispose() {
		game.assetmanager.unload("graphics/splash.jpg");
	}
}
