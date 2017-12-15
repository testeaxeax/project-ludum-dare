package com.thetriumvirate.spaceballz;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.utils.ScissorStack;
import com.badlogic.gdx.utils.TimeUtils;

public final class MainMenuScreen implements Screen, InputProcessor {
	
	private static final String STARTBUTTON_TEXTURE_ASSET_PATH = "graphics/startbutton.png";
	private static final String CREDITSBUTTON_TEXTURE_ASSET_PATH = "graphics/creditsbutton.png";
	private static final String BACKGROUND_ASSET_PATH = "graphics/gameBackground.png";
	private static final String TEXTURE_BLACK_PATH = "graphics/black.png";
	private static final String TITLE_TEXTURE_PATH = "graphics/title.png";
	private static final long CLICK_TIMEOUT = 1000;
	
	private Project game;
	private OrthographicCamera cam;
	private Button bstart;
	private Button bcredits;
	private Texture background;
	private Texture tBlack;
	private Texture title;
	private long last_click;
	private int CAM_WIDTH;
	private int CAM_HEIGHT;
	
	public static BackgroundMesh mesh;
	
	public MainMenuScreen(Project game) {
		CAM_WIDTH = Project.SCREEN_WIDTH;
		CAM_HEIGHT = Project.SCREEN_HEIGHT;
		this.game = game;
		
		prefetch(game.assetmanager);
		game.assetmanager.finishLoading();
		
		background = game.assetmanager.get(BACKGROUND_ASSET_PATH, Texture.class);
		
		tBlack = game.assetmanager.get(TEXTURE_BLACK_PATH, Texture.class);
		
		bstart = new Button(CAM_WIDTH / 2, CAM_HEIGHT / 2, Project.DEFAULT_BUTTON_WIDTH, Project.DEFAULT_BUTTON_HEIGHT, game.assetmanager.get(STARTBUTTON_TEXTURE_ASSET_PATH, Texture.class), game);
		bcredits = new Button(CAM_WIDTH - CAM_WIDTH / 4, CAM_HEIGHT / 8, Project.DEFAULT_BUTTON_WIDTH, Project.DEFAULT_BUTTON_HEIGHT, game.assetmanager.get(CREDITSBUTTON_TEXTURE_ASSET_PATH, Texture.class), game);
		
		title = game.assetmanager.get(TITLE_TEXTURE_PATH, Texture.class);
		
		bstart.setX(bstart.getX() - bstart.getWidth() / 2);
		bstart.setY(bstart.getY() - bstart.getHeight() / 2);
		
		bcredits.setX(bcredits.getX() - bcredits.getWidth() / 2);
		bcredits.setY(bcredits.getY() - bcredits.getHeight() / 2);
		
		cam = new OrthographicCamera();
		cam.setToOrtho(false, CAM_WIDTH, CAM_HEIGHT);
		cam.update();
		
		mesh = new BackgroundMesh(game);
		
		game.spritebatch.setProjectionMatrix(cam.combined);
		Gdx.gl.glClearColor(200f / 255f, 200f / 255f, 200f / 255f, 1f);
	}

	@Override
	public void show() {		
		game.music.play();
		last_click = TimeUtils.millis();
		Gdx.input.setInputProcessor(this);
	}

	@Override
	public void render(float delta) {		
		float x = Gdx.input.getX();
		float y = Project.SCREEN_HEIGHT - Gdx.input.getY();
		
		mesh.update(delta, x, y);
		
		
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		
		game.spritebatch.begin();
		
		game.spritebatch.draw(background, 0, 0, CAM_WIDTH, CAM_HEIGHT);
		
		game.spritebatch.end();
		
		mesh.render(delta, cam);

		game.spritebatch.begin();
		
		if(!bstart.isPressed())
			game.spritebatch.draw(bstart.getTexture(), bstart.getX(), bstart.getY(), bstart.getWidth(), bstart.getHeight());
		else {
			game.spritebatch.draw(tBlack, bstart.getX() - 1, bstart.getY() - 2, bstart.getWidth() + 2, bstart.getHeight() + 4);
			game.spritebatch.draw(bstart.getTexture(), bstart.getX() + 2, bstart.getY() + 1, bstart.getWidth() - 4, bstart.getHeight() - 2);
		}
		
		
		if(!bcredits.isPressed())
			game.spritebatch.draw(bcredits.getTexture(), bcredits.getX(), bcredits.getY(), bcredits.getWidth(), bcredits.getHeight());
		else {
			game.spritebatch.draw(tBlack, bcredits.getX() - 1, bcredits.getY() - 2, bcredits.getWidth() + 2, bcredits.getHeight() + 4);
			game.spritebatch.draw(bcredits.getTexture(), bcredits.getX() + 2, bcredits.getY() + 1, bcredits.getWidth() - 4, bcredits.getHeight() - 2);
		}
		
		game.spritebatch.draw(title, Project.SCREEN_WIDTH/2 - Project.SCREEN_WIDTH * 0.390625f, Project.SCREEN_HEIGHT - Project.SCREEN_HEIGHT * 0.4f, CAM_WIDTH * 0.78125f, CAM_WIDTH * 0.29296875f);
		
		
		game.spritebatch.end();
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		if(button == Input.Buttons.LEFT && TimeUtils.timeSinceMillis(last_click) > CLICK_TIMEOUT) {
			if(bcredits.onPress(screenX, screenY, true))
				bcredits.setPressed(true);
			    last_click = TimeUtils.millis();
			if(bstart.onPress(screenX, screenY, true))
				bstart.setPressed(true);
			    last_click = TimeUtils.millis();
			
			return true;
		}
		
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		if (button == Input.Buttons.LEFT) {
	      if(bcredits.onPress(screenX, screenY, false)) {
		        game.screenmanager.push(new CreditsScreen(game));
	      }
	      if(bstart.onPress(screenX, screenY, false)) {
	        	game.screenmanager.push(new GameScreen(game));
	      }
	          bstart.setPressed(false);
	          bcredits.setPressed(false);
	          return true;     
	      }
	      return false;
	}

	public static void prefetch(AssetManager m) {
		m.load(STARTBUTTON_TEXTURE_ASSET_PATH, Texture.class);
		m.load(CREDITSBUTTON_TEXTURE_ASSET_PATH, Texture.class);
		m.load(BACKGROUND_ASSET_PATH, Texture.class);
		m.load(TEXTURE_BLACK_PATH, Texture.class);
		m.load(TITLE_TEXTURE_PATH, Texture.class);
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
		Gdx.input.setInputProcessor(null);
	}

	@Override
	public void dispose() {
		game.assetmanager.unload(STARTBUTTON_TEXTURE_ASSET_PATH);
		game.assetmanager.unload(CREDITSBUTTON_TEXTURE_ASSET_PATH);
		game.assetmanager.unload(BACKGROUND_ASSET_PATH);
		game.assetmanager.unload(TEXTURE_BLACK_PATH);
		game.assetmanager.unload(TITLE_TEXTURE_PATH);
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

	public void render(float delta, float animationTimer) {
		if(animationTimer >= 1f) {
			Rectangle scissors = new Rectangle();
			Rectangle clipBounds = new Rectangle(0, 0, Project.SCREEN_WIDTH, animationTimer);
			ScissorStack.calculateScissors(cam, game.spritebatch.getTransformMatrix(), clipBounds, scissors);
			ScissorStack.pushScissors(scissors);
			
			this.render(delta);
			
			ScissorStack.popScissors();
		}
	}
}
