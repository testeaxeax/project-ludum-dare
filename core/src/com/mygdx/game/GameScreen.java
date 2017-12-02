package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;

public final class GameScreen implements Screen {
	
	private static final int CAM_WIDTH = Project.SCREEN_WIDTH;
	private static final int CAM_HEIGHT = Project.SCREEN_HEIGHT;
	
	private Project game;
	private OrthographicCamera cam;
	
	private Sun sun;
	private ProgressBar pb;
	private Texture pbBorder;
	private Texture pbInfill;
	private static final String PB_BORDER_TEXTURE_PATH = "graphics/pbBordertest.png";
	private static final String PB_INFILL_TEXTURE_PATH = "graphics/pbInfilltest.png";
	
	
	public GameScreen(Project game) {
		this.game = game;
		cam = new OrthographicCamera();
		cam.setToOrtho(false, CAM_WIDTH, CAM_HEIGHT);
		cam.update();
		game.spritebatch.setProjectionMatrix(cam.combined);
		Gdx.gl.glClearColor(1, 0, 0, 1);
		
		pbBorder = game.assetmanager.get(PB_BORDER_TEXTURE_PATH);
		pbInfill = game.assetmanager.get(PB_INFILL_TEXTURE_PATH);
		
		
		pb = new ProgressBar(50, 200, 50, 50, pbBorder, pbInfill);
		sun = new Sun(this, 400, 400, 0, 100, 0, 30);

	}

	public Project getGame() {
		return game;
	}

	@Override
	public void show() {
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		game.spritebatch.begin();
		// Render progress bar
		game.spritebatch.draw(pb.getInfillTexture(), pb.getPosX(), pb.getPosY(), pb.getWidth(), pb.getHeight() * pb.getPercentage());
		game.spritebatch.draw(pb.getBorderTexture(),pb.getPosX(), pb.getPosY(), pb.getWidth(), pb.getHeight());
		// Render Sun
		int sun_width = sun.getSun_texture().getWidth();
		int sun_height = sun.getSun_texture().getHeight();
		sun.update(delta);
		game.spritebatch.draw (sun.getSun_texture(), (float) sun.getPos().x - sun_width / 2,
				(float) sun.getPos().y - sun_height / 4, (float) sun_width / 2, (float) sun_height / 2,
				(float) sun_width, (float) sun_height, 1, 1, (float) sun.getRotation(), 0, 0, (int) sun_width,
				(int) sun_height, false, false);
		game.spritebatch.end();
	}

	public static void prefetch(AssetManager m) {
		Sun.prefetch(m);
		m.load(PB_BORDER_TEXTURE_PATH, Texture.class);
		m.load(PB_INFILL_TEXTURE_PATH, Texture.class);
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
		game.assetmanager.unload(PB_BORDER_TEXTURE_PATH);
		game.assetmanager.unload(PB_INFILL_TEXTURE_PATH);
	}
}
