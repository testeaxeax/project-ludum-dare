package com.mygdx.game;

import java.util.Random;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Project extends Game {
	
	// Globally used constants
	public static final int SCREEN_HEIGHT = 800;
	public static final int SCREEN_WIDTH = 1024;
	
	public static final Random RAND = new Random();
	
	// Globally used variables required for management and rendering
	public SpriteBatch spritebatch;
	public AdvancedAssetManager assetmanager;
	public ScreenManager screenmanager;
	
	// Default font
	public BitmapFont font;
	
	// Filter-Mask-Bits
	public static final short SUN_BIT = 1;
	public static final short PLANET_BIT = 2;
	public static final short SUNRAY_BIT = 4;
	
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
//		screenmanager.push(new SplashScreen(this));
		screenmanager.push(new GameScreen(this));
	}
	
	@Override
	public void dispose() {
		screenmanager.dispose();
		spritebatch.dispose();
		assetmanager.dispose();
	}
}
