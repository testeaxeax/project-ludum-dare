package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.TimeUtils;

public final class CreditsScreen implements Screen {
	
	private static final int CAM_WIDTH = Project.SCREEN_WIDTH;
	private static final int CAM_HEIGHT = Project.SCREEN_HEIGHT;
	private static final String CREDITS_BACKGROUND_ASSET_PATH = "graphics/gameBackground.png";
	private static final String CREDITS = "Game developed by:\nInzenhofer Tobias\nPoellinger Maximilian\nBrunner Moritz";
	
	private Project game;
	private OrthographicCamera cam;
	private Texture background;
	private GlyphLayout layout;
	private long last_touched;
	
	public CreditsScreen(Project game) {
		this.game = game;
		background = game.assetmanager.get(CREDITS_BACKGROUND_ASSET_PATH);
		cam = new OrthographicCamera();
		cam.setToOrtho(false, CAM_WIDTH, CAM_HEIGHT);
		cam.update();
		game.spritebatch.setProjectionMatrix(cam.combined);
		layout = new GlyphLayout();
		Gdx.gl.glClearColor(1, 0, 0, 1);
		last_touched = TimeUtils.millis();
	}

	@Override
	public void show() {
		
	}

	@Override
	public void render(float delta) {
		float x = Gdx.input.getX();
		float y = Project.SCREEN_HEIGHT - Gdx.input.getY();
		
		MainMenuScreen.mesh.update(delta, x, y);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		if(Gdx.input.justTouched() && TimeUtils.timeSinceMillis(last_touched) > 500) {
			last_touched = TimeUtils.millis();
			game.screenmanager.pop();
			dispose();
		}
		layout.setText(game.font, CREDITS);
		Vector2 position = new Vector2(Project.SCREEN_WIDTH / 2 - layout.width / 2, Project.SCREEN_HEIGHT / 2 + layout.height / 2);
		game.spritebatch.begin();
		game.spritebatch.draw(background, 0, 0 , Project.SCREEN_WIDTH, Project.SCREEN_HEIGHT);
		game.spritebatch.end();
		MainMenuScreen.mesh.render(delta, cam);
		game.spritebatch.begin();
		game.font.draw(game.spritebatch, layout, position.x, position.y);
		game.spritebatch.end();
	}

	public static void prefetch(AssetManager m) {
		m.load(CREDITS_BACKGROUND_ASSET_PATH, Texture.class);
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
		game.assetmanager.unload(CREDITS_BACKGROUND_ASSET_PATH);
	}
}
