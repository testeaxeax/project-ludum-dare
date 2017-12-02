package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;

public final class MainMenuScreen implements Screen, InputProcessor {
	
	private static final int CAM_WIDTH = Project.SCREEN_WIDTH;
	private static final int CAM_HEIGHT = Project.SCREEN_HEIGHT;
	private static final String STARTBUTTON_TEXTURE_ASSET_PATH = "graphics/startbutton.png";
	private static final String CREDITSBUTTON_TEXTURE_ASSET_PATH = "graphics/creditsbutton.png";
	private static final String BACKGROUND_ASSET_PATH = "graphics/gamebackground.png";
	
	private Project game;
	private OrthographicCamera cam;
	private Button bstart;
	private Button bcredits;
	private Texture background;
	
	public MainMenuScreen(Project game) {
		this.game = game;
		background = game.assetmanager.get(BACKGROUND_ASSET_PATH, Texture.class);
		bstart = new Button(CAM_WIDTH / 2, CAM_HEIGHT / 2, 200, 100, game.assetmanager.get(STARTBUTTON_TEXTURE_ASSET_PATH, Texture.class));
		bcredits = new Button(CAM_WIDTH - CAM_WIDTH / 4, CAM_HEIGHT / 8, 200, 100, game.assetmanager.get(CREDITSBUTTON_TEXTURE_ASSET_PATH, Texture.class));
		cam = new OrthographicCamera();
		cam.setToOrtho(false, CAM_WIDTH, CAM_HEIGHT);
		cam.update();
		game.spritebatch.setProjectionMatrix(cam.combined);
		Gdx.gl.glClearColor(1, 0, 0, 1);
		Gdx.input.setInputProcessor(this);
	}

	@Override
	public void show() {
		
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		game.spritebatch.begin();
		game.spritebatch.draw(background, 0, 0, CAM_WIDTH, CAM_HEIGHT);
		game.spritebatch.draw(bstart.getTexture(),
				bstart.getX() - bstart.getWidth() / 2, bstart.getY() - bstart.getHeight() / 2,
				bstart.getWidth(), bstart.getHeight());
		game.spritebatch.draw(bcredits.getTexture(),
				bcredits.getX() - bcredits.getWidth() / 2, bcredits.getY() - bcredits.getHeight() / 2,
				bcredits.getWidth(), bcredits.getHeight());
		game.spritebatch.end();
	}

	public static void prefetch(AssetManager m) {
		m.load(STARTBUTTON_TEXTURE_ASSET_PATH, Texture.class);
		m.load(CREDITSBUTTON_TEXTURE_ASSET_PATH, Texture.class);
		m.load(BACKGROUND_ASSET_PATH, Texture.class);
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
		game.assetmanager.unload(STARTBUTTON_TEXTURE_ASSET_PATH);
		game.assetmanager.unload(CREDITSBUTTON_TEXTURE_ASSET_PATH);
		game.assetmanager.unload(BACKGROUND_ASSET_PATH);
		Gdx.input.setInputProcessor(null);
	}

	@Override
	public boolean keyDown(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		if (button == Input.Buttons.LEFT) {
	          if(bstart.onPress(screenX, screenY)) {
	        	game.screenmanager.set(new GameScreen(game));
	  			dispose();
	          }
	          if(bcredits.onPress(screenX, screenY)) {
		        	game.screenmanager.set(new CreditsScreen(game));
		  			dispose();
		          }
	          return true;     
	      }
	      return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		// TODO Auto-generated method stub
		return false;
	}
}
