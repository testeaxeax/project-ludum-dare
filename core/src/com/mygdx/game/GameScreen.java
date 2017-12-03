package com.mygdx.game;

import java.util.ArrayList;
import java.util.Vector;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;

public final class GameScreen implements Screen {
	
	private static final int CAM_WIDTH = Project.SCREEN_WIDTH;
	private static final int CAM_HEIGHT = Project.SCREEN_HEIGHT;
	
	private Project game;
	private OrthographicCamera cam;
	
	private Sun sun;
	private TextureRegion texture_sunray_shaft;
	private static final String SR_SHAFT = "graphics/rayshaft.png";
	
	private ProgressBar pb;
	private Texture pbBorder;
	private Texture pbInfill;
	private static final String PB_BORDER_TEXTURE_PATH = "graphics/pbBordertest.png";
	private static final String PB_INFILL_TEXTURE_PATH = "graphics/pbInfilltest.png";
	
	private Texture background;
	private static final String BACKGROUND_TEXTURE_PATH = "graphics/gameBackground.png";
	

	private Texture[] planetTextures;
	private ArrayList<Planet> planets;
	private static final int PLANET_TEXTURES = 7;
	private static final String PLANETS_TEXTURE_PATH = "graphics/planets/planetx.png";
	private static final int MAX_PLANETS = 10;
	
	private ArrayList<Explosion> explosions;
	
	private double raydelta;
	
	private int score = 0;
	private float scorePosX = 0.8f * Project.SCREEN_WIDTH, scorePosY = 0.9f * Project.SCREEN_HEIGHT;
	private GlyphLayout scoreLayout;
	
	private Music music;
	
	private long start;
	private boolean started;
	
	public GameScreen(Project game) {
		this.started = false;
		
		this.game = game;
		
		cam = new OrthographicCamera();
		cam.setToOrtho(false, CAM_WIDTH, CAM_HEIGHT);
		cam.update();
		
		game.spritebatch.setProjectionMatrix(cam.combined);
		Gdx.gl.glClearColor(0, 0, 1, 1);
		
		pbBorder = game.assetmanager.get(PB_BORDER_TEXTURE_PATH);
		pbInfill = game.assetmanager.get(PB_INFILL_TEXTURE_PATH);
		
		background = game.assetmanager.get(BACKGROUND_TEXTURE_PATH);
		
		
		pb = new ProgressBar(50, 200, 50, 50, pbBorder, pbInfill);
		pb.setPercentage(0.2f);
		
		this.planetTextures = new Texture[PLANET_TEXTURES];
		
		for(int i = 0; i < PLANET_TEXTURES; i++)
			this.planetTextures[i] = game.assetmanager.get(PLANETS_TEXTURE_PATH.replace("x", String.valueOf(i)), Texture.class);
		
		this.planets = PlanetManager.setupPlanets(MAX_PLANETS, this);
		
		sun = new Sun(this, Project.SCREEN_WIDTH / 2, Project.SCREEN_HEIGHT / 2, 0, 100, 30, 1);
		texture_sunray_shaft = new TextureRegion((Texture) game.assetmanager.get(SR_SHAFT));
		
		this.raydelta = 250d;
		
		scoreLayout = new GlyphLayout();
		
		Explosion.setup(game.assetmanager);
		this.explosions = new ArrayList<Explosion>();
		
//		music = game.assetmanager.get("audio/music/music.ogg", Music.class);
//		music.setLooping(true);
//		music.setVolume(0.1f);		
//		music.play();
		
		this.start = 0l;
	}

	@Override
	public void render(float delta) {
		if(planets.isEmpty()) {
			this.planets = PlanetManager.setupPlanets(MAX_PLANETS, this);
		}
		if(this.raydelta < 250d)
			this.raydelta += 7;
		
		if(this.started) {
			if(Gdx.input.justTouched() && this.raydelta >= 250d && sun.getTemp() > 3) {
				this.raydelta = 0d;
				sun.decreasetemp(3);
				if(rayHitsPlanets())
					game.assetmanager.get("audio/sounds/planet_vanish.wav", Sound.class).play(0.1f);
				else
					game.assetmanager.get("audio/sounds/beam.wav", Sound.class).play(0.2f);
			}
		} else 
			this.started = System.currentTimeMillis() - this.start > 300;
		
		
		sun.update(delta, this.raydelta <= 180);
		pb.update(sun);
		
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		game.spritebatch.begin();
		//BACKGROUND has to be drawn FIRST!!
		game.spritebatch.draw(background, 0, 0, Project.SCREEN_WIDTH, Project.SCREEN_HEIGHT);
		
		// Render planets
		for(Planet p : this.planets)
			this.game.spritebatch.draw(p.getRegion(), p.getX(), p.getY(), p.getRadius() * 2, p.getRadius() * 2);
		

		int sun_width = sun.getSun_texture().getWidth();
		int sun_height = sun.getSun_texture().getHeight();
		
		// Render Explosions
		for(int i = this.explosions.size() - 1; i >= 0; i--) {
			Explosion e = this.explosions.get(i);
			Texture t = e.getTexture(delta);
			
			if(t != null)
				game.spritebatch.draw(t, e.getX(), e.getY(), e.getRadius(), e.getRadius());
			else
				this.explosions.remove(i);
		}
		
		
		// Render rays
		if(this.raydelta < 180) {
			float sin = (float) Math.sin(this.raydelta / 180d * Math.PI);
			
			float size_shaft = 3 + 7f * sin;
			float length = Project.SCREEN_WIDTH * 4f * sin;
			
			game.spritebatch.draw(texture_sunray_shaft, (float) Project.SCREEN_WIDTH / 2 - length / 2f - sun_width / 2, sun.getPos().y - size_shaft / 2f, 
					length / 2f + sun_width / 2, size_shaft / 2f, 
					length, size_shaft, 
					1f, 1f, sun.getRotation());
			
			game.spritebatch.draw(texture_sunray_shaft, (float) Project.SCREEN_WIDTH / 2 - length / 2f - sun_width / 2, sun.getPos().y - size_shaft / 2f, 
					length / 2f + sun_width / 2, size_shaft / 2f, 
					length, size_shaft, 
					1f, 1f, sun.getRotation() + 90f);
		}
		
		// Render Sun
		game.spritebatch.draw (sun.getSun_texture(), 
				(float) sun.getPos().x - sun_width / 2, (float) sun.getPos().y - sun_height / 2, 
				(float) sun_width / 2, (float) sun_height / 2, (float) sun_width, (float) sun_height, 1, 1, 
				(float) sun.getRotation(), 
				0, 0, (int) sun_width, (int) sun_height, 
				false, false);
		
		// Render progress bar
		float x = 6f;
		game.spritebatch.draw(pb.getInfillTexture(), pb.getPosX(), pb.getPosY() + x, pb.getWidth(), (pb.getHeight() - 2*x) * pb.getPercentage());
		game.spritebatch.draw(pb.getBorderTexture(),pb.getPosX(), pb.getPosY(), pb.getWidth(), pb.getHeight());
		
		scoreLayout.setText(game.font, "Score: "  + score);
		game.font.draw(game.spritebatch, scoreLayout, scorePosX, scorePosY);
		
		
		ShapeRenderer sr = new ShapeRenderer();
		sr.setColor(Color.WHITE);
		sr.setProjectionMatrix(cam.combined);
		//draws lines from sunpos to planets (calculated by angles)
		Vector2 rel = sun.getPos();
		for(Planet p : planets) {
			sr.begin(ShapeType.Line);
			sr.line(rel.x, rel.y, rel.x +  100 * (float) Math.cos(Math.toRadians(p.getAngleRelative(rel.x, rel.y))), rel.y + 100 * (float) Math.sin(Math.toRadians(p.getAngleRelative(rel.x, rel.y))));
			sr.end();
		}
		
		game.spritebatch.end();
	}

	
	private boolean rayHitsPlanets() {
		ArrayList<Planet> toRemove = new ArrayList<Planet>();
		Vector2 rel = sun.getPos();
		
		
		
		rel = new Vector2(rel.x + sun.getSunRadius()/2, rel.y + sun.getSunRadius()/2);
		for(int i = 0; i < 4; i++) {
			for(Planet p : planets) {
				
				if(sun.getRotation() + 90*i > p.getAngleRelative(rel.x, rel.y) -p.getAngularWidth(rel.x, rel.y) &&
				   sun.getRotation() + 90*i < p.getAngleRelative(rel.x, rel.y) +p.getAngularWidth(rel.x, rel.y )) {
					toRemove.add(p);
				}
			}
		}
		for(Planet p : toRemove) {
			this.explosions.add(new Explosion(p));
			planets.remove(p);
			score++;
		}
		
		return toRemove.size() > 0;
	}
	
	
	
	public ProgressBar getPB() {
		return this.pb;
	}
	
	public void gameover() {
		game.screenmanager.set(new GameoverMenuScreen(game));
	}
	
	public Project getGame() {
		return game;
	}

	@Override
	public void show() {
		this.start = System.currentTimeMillis();
	}

	public static void prefetch(AssetManager m) {
		Sun.prefetch(m);
		m.load(PB_BORDER_TEXTURE_PATH, Texture.class);
		m.load(PB_INFILL_TEXTURE_PATH, Texture.class);
		
		m.load(BACKGROUND_TEXTURE_PATH, Texture.class);
		
		for(int i = 0; i < PLANET_TEXTURES; i++)
			m.load(PLANETS_TEXTURE_PATH.replace("x", String.valueOf(i)), Texture.class);
		
		m.load(SR_SHAFT, Texture.class);
		
		Explosion.load(m);
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
		
		game.assetmanager.unload(BACKGROUND_TEXTURE_PATH);
		
		game.assetmanager.unload(SR_SHAFT);
		
		for(int i = 0; i < PLANET_TEXTURES; i++)
			game.assetmanager.unload(PLANETS_TEXTURE_PATH.replace("x", String.valueOf(i)));
	}

	public Texture[] getPlanetTextures() {
		return this.planetTextures;
	}
	
	public ArrayList<Planet> getPlanets() {
		return planets;
	}
}
