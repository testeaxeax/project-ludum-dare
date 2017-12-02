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
	
	private ProgressBar pb;
	private Texture pbBorder;
	private Texture pbInfill;
	private final String pbTextureBoderPath = "graphics/pbBordertest.png";
	private final String pbTextureInfillPath = "graphics/pbInfilltest.png";
	
	
	public GameScreen(Project game) {
		this.game = game;
		cam = new OrthographicCamera();
		cam.setToOrtho(false, CAM_WIDTH, CAM_HEIGHT);
		cam.update();
		game.spritebatch.setProjectionMatrix(cam.combined);
		Gdx.gl.glClearColor(1, 0, 0, 1);
		
		pbBorder = game.assetmanager.easyget(pbTextureBoderPath, Texture.class);
		pbInfill = game.assetmanager.easyget(pbTextureInfillPath, Texture.class);
		
		pb = new ProgressBar(50, 200, 50, 50, pbBorder, pbInfill);
		pb.setPercentage(0.5f);
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
		game.spritebatch.draw(pb.getInfillTexture(), pb.getPosX(), pb.getPosY(), pb.getWidth(), pb.getHeight() * pb.getPercentage());
		game.spritebatch.draw(pb.getBorderTexture(),pb.getPosX(), pb.getPosY(), pb.getWidth(), pb.getHeight());
		game.spritebatch.end();
	}

	public static void prefetch(AssetManager m) {
		
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
		game.assetmanager.unload(pbTextureBoderPath);
		game.assetmanager.unload(pbTextureInfillPath);
	}
}