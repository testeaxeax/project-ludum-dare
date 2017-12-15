package com.thetriumvirate.spaceballz;

import java.util.Random;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Project extends Game {
	
	// Globally used constants
	public static final Color DEFAULT_FONT_COLOR = Color.WHITE;
	// DO NOT append suffix
	public static final String DEFAULT_FONT_ASSET_PATH = "fonts/IHateComicSans";
	public static final Random RAND = new Random();
	
	// Globally used variables
	public static int SCREEN_HEIGHT = 800;
	public static int SCREEN_WIDTH = 1024;
	public static float RATIO;
	public static float DEFAULT_BUTTON_WIDTH;
	public static float DEFAULT_BUTTON_HEIGHT;
	public static int DEFAULT_FONTSIZE;
	
	// Globally used variables required for management and rendering
	public SpriteBatch spritebatch;
	public AdvancedAssetManager assetmanager;
	public ScreenManager screenmanager;
	
	// Default music
	public Music music;
	
	// Default font
	public BitmapFont font;
	
	FontLoader fontloader;
	
	public Project(FontLoader fl) {
		RATIO = (float) SCREEN_WIDTH / (float) SCREEN_HEIGHT;
		DEFAULT_BUTTON_WIDTH = SCREEN_WIDTH * 0.1953125f;
		DEFAULT_BUTTON_HEIGHT = SCREEN_WIDTH * 0.09765625f;
		DEFAULT_FONTSIZE = (int) (SCREEN_WIDTH * 0.029296875f);
		fontloader = fl;
		fontloader.setGame(this);
	}
	
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
		//font = assetmanager.easyget("fonts/IHateComicSans.fnt", BitmapFont.class);
		font = fontloader.load();
		
		// Load sounds
		loadSounds();
		screenmanager.push(new SplashScreen(this));
	}
	
	public void loadSounds() {
		assetmanager.load("audio/sounds/planet_vanish.wav", Sound.class);
		assetmanager.load("audio/sounds/beam.wav", Sound.class);
		assetmanager.load("audio/sounds/click.wav", Sound.class);
		assetmanager.load("audio/music/music.mp3", Music.class);
	}
	
	@Override
	public void dispose() {
		screenmanager.dispose();
		spritebatch.dispose();
		assetmanager.dispose();
	}
}
