package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Project extends Game {
	
	// Globally used constants
	public static final int SCREEN_HEIGHT = 800;
	public static final int SCREEN_WIDTH = 1024;
	
	// Globally used variables required for management and rendering
	public SpriteBatch spritebatch;
	public AdvancedAssetManager assetmanager;
	public ScreenManager screenmanager;
	
	// Default font
	public BitmapFont font;
	
	// Required to trigger rendering of active screen
	@Override
	public void render() {
		super.render();
	}
	
	@Override
	public void create() {
		spritebatch = new SpriteBatch();
		assetmanager = new AdvancedAssetManager();
		screenmanager = new ScreenManager(this);
		// Load default font
		font = assetmanager.easyget("fonts/LiberationSans-Regular.fnt", BitmapFont.class);
		screenmanager.push(new SplashScreen(this));
	}
	
	@Override
	public void dispose() {
		screenmanager.dispose();
		spritebatch.dispose();
		assetmanager.dispose();
	}
}
