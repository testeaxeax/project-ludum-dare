package com.thetriumvirate.spaceballz;

import java.util.Random;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Project extends Game {
	
	// Globally used constants
	public static final int SCREEN_HEIGHT = 800;
	public static final int SCREEN_WIDTH = 1024;
	public static final float RATIO = (float) SCREEN_WIDTH / (float) SCREEN_HEIGHT;
	
	public static final Random RAND = new Random();
	
	// Globally used variables required for management and rendering
	public SpriteBatch spritebatch;
	public AdvancedAssetManager assetmanager;
	public ScreenManager screenmanager;
	
	// Default music
	public Music music;
	
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
		font = assetmanager.easyget("fonts/IHateComicSans.fnt", BitmapFont.class);
		
		// Load sounds
		loadSounds();
		screenmanager.push(new SplashScreen(this));
	}
	
	public void loadSounds() {
		assetmanager.load("audio/sounds/planet_vanish.wav", Sound.class);
		assetmanager.load("audio/sounds/beam.wav", Sound.class);
		assetmanager.load("audio/sounds/click.wav", Sound.class);
		assetmanager.load("audio/music/music.wav", Music.class);
	}
	
	@Override
	public void dispose() {
		screenmanager.dispose();
		spritebatch.dispose();
		assetmanager.dispose();
	}
}
