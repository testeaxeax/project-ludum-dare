package com.thetriumvirate.spaceballz;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;

public final class GameoverMenuScreen implements Screen, InputProcessor {
	
	private static final int CAM_WIDTH = Project.SCREEN_WIDTH;
	private static final int CAM_HEIGHT = Project.SCREEN_HEIGHT;
	private static final String RESTARTBUTTON_TEXTURE_ASSET_PATH = "graphics/restartbutton.png";
	private static final String MENUBUTTON_TEXTURE_ASSET_PATH = "graphics/menubutton.png";
	private static final String BACKGROUND_ASSET_PATH = "graphics/gameBackground.png";
	private static final String TEXTURE_BLACK_PATH = "graphics/black.png";
	
	private Project game;
	private OrthographicCamera cam;
	private Button brestart;
	private Button bmenu;
	private Texture background;
	private Texture tBlack;
	
	
	private int score, level;
	private GlyphLayout scoreLayout;
	
	public GameoverMenuScreen(Project game, int score, int level) {
		this.game = game;
		this.score = score;
		this.level = level;
		
		background = game.assetmanager.get(BACKGROUND_ASSET_PATH, Texture.class);
		
		tBlack = game.assetmanager.get(TEXTURE_BLACK_PATH, Texture.class);
		
		brestart = new Button(CAM_WIDTH / 4, CAM_HEIGHT / 2, 200, 100, game.assetmanager.get(RESTARTBUTTON_TEXTURE_ASSET_PATH, Texture.class), game);
		bmenu = new Button(CAM_WIDTH * 3 / 4, CAM_HEIGHT / 2, 200, 100, game.assetmanager.get(MENUBUTTON_TEXTURE_ASSET_PATH, Texture.class), game);
		
		brestart.setX(brestart.getX() - brestart.getWidth() / 2);
		brestart.setY(brestart.getY() - brestart.getHeight() / 2);
		
		bmenu.setX(bmenu.getX() - bmenu.getWidth() / 2);
		bmenu.setY(bmenu.getY() - bmenu.getHeight() / 2);
		
		cam = new OrthographicCamera();
		cam.setToOrtho(false, CAM_WIDTH, CAM_HEIGHT);
		cam.update();
		game.spritebatch.setProjectionMatrix(cam.combined);
		
		scoreLayout = new GlyphLayout();
		
		
		Gdx.gl.glClearColor(1, 0, 0, 1);
	}

	@Override
	public void show() {
		Gdx.input.setInputProcessor(this);
		game.music.stop();
	}

	@Override
	public void render(float delta) {
		float x = Gdx.input.getX();
		float y = Project.SCREEN_HEIGHT - Gdx.input.getY();
		
		MainMenuScreen.mesh.update(delta, x, y);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		game.spritebatch.begin();
		game.spritebatch.draw(background, 0, 0, CAM_WIDTH, CAM_HEIGHT);
		game.spritebatch.end();
		
		MainMenuScreen.mesh.render(delta, cam);
		
		game.spritebatch.begin();
		if(!brestart.isPressed())
			game.spritebatch.draw(brestart.getTexture(), brestart.getX(), brestart.getY(), brestart.getWidth(), brestart.getHeight());
		else {
			game.spritebatch.draw(tBlack, brestart.getX() - 1, brestart.getY() - 2, brestart.getWidth() + 2, brestart.getHeight() + 4);
			game.spritebatch.draw(brestart.getTexture(), brestart.getX() + 2, brestart.getY() + 1, brestart.getWidth() - 4, brestart.getHeight() - 2);
		}
		
		
		if(!bmenu.isPressed())
			game.spritebatch.draw(bmenu.getTexture(), bmenu.getX(), bmenu.getY(), bmenu.getWidth(), bmenu.getHeight());
		else {
			game.spritebatch.draw(tBlack, bmenu.getX() - 1, bmenu.getY() - 2, bmenu.getWidth() + 2, bmenu.getHeight() + 4);
			game.spritebatch.draw(bmenu.getTexture(), bmenu.getX() + 2, bmenu.getY() + 1, bmenu.getWidth() - 4, bmenu.getHeight() - 2);
		}
		
		
		scoreLayout.setText(game.font, "Score: " + score + "\n" + "Level: " + level); 
		game.font.draw(game.spritebatch, scoreLayout, Project.SCREEN_WIDTH/2 - scoreLayout.width/2, Project.SCREEN_HEIGHT/1.5f - scoreLayout.height/2);
		game.spritebatch.end();
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		if(button == Input.Buttons.LEFT) {
			if(bmenu.onPress(screenX, screenY, true))
				bmenu.setPressed(true);
			if(brestart.onPress(screenX, screenY,true))
				brestart.setPressed(true);
			
			
			return true;
		}
		
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		if (button == Input.Buttons.LEFT) {
			if(bmenu.onPress(screenX, screenY, false)) {
	        	bmenu.setPressed(false);
	        	game.screenmanager.pop();
		    }
	        if(brestart.onPress(screenX, screenY, false)) {
	        	brestart.setPressed(false);
	        	game.screenmanager.set(new GameScreen(game));
	          }
	          return true;     
	      }
	      return false;
	}

	public static void prefetch(AssetManager m) {
		m.load(RESTARTBUTTON_TEXTURE_ASSET_PATH, Texture.class);
		m.load(MENUBUTTON_TEXTURE_ASSET_PATH, Texture.class);
		m.load(BACKGROUND_ASSET_PATH, Texture.class);
		m.load(TEXTURE_BLACK_PATH, Texture.class);
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
		game.music.play();
	}

	@Override
	public void dispose() {
		game.assetmanager.unload(RESTARTBUTTON_TEXTURE_ASSET_PATH);
		game.assetmanager.unload(MENUBUTTON_TEXTURE_ASSET_PATH);
		game.assetmanager.unload(BACKGROUND_ASSET_PATH);
		game.assetmanager.unload(TEXTURE_BLACK_PATH);
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
}
