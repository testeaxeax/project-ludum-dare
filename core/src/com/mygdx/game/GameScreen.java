package com.mygdx.game;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

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
	
	private double raydelta;
	
	
	private int score = 0;
	private float scorePosX = 0.8f * Project.SCREEN_WIDTH, scorePosY = 0.9f * Project.SCREEN_HEIGHT;
	private GlyphLayout scoreLayout;
	
	public GameScreen(Project game) {
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
		
		this.planets = PlanetManager.setupPlanets(10, this);
		
		sun = new Sun(this, CAM_WIDTH / 2, CAM_HEIGHT / 2, 0, 100, 30, 1);
		texture_sunray_shaft = new TextureRegion((Texture) game.assetmanager.get(SR_SHAFT));
		
		this.raydelta = 250d;
		
		scoreLayout = new GlyphLayout();
	}

	@Override
	public void render(float delta) {
		if(this.raydelta < 250d)
			this.raydelta += 7;
		
		if(Gdx.input.justTouched() && this.raydelta >= 250d && sun.getTemp() > 3) {
			this.raydelta = 0d;
			sun.decreasetemp(3);
			rayHitsPlanets();
		}
		
		
		sun.update(delta, this.raydelta <= 180);
		pb.update(sun);
		
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		game.spritebatch.begin();
		
		//BACKGROUND has to be drawn FIRST!!
		game.spritebatch.draw(background, 0, 0, Project.SCREEN_WIDTH, Project.SCREEN_HEIGHT);
		// Render progress bar
		game.spritebatch.draw(pb.getInfillTexture(), pb.getPosX(), pb.getPosY(), pb.getWidth(), pb.getHeight() * pb.getPercentage());
		game.spritebatch.draw(pb.getBorderTexture(),pb.getPosX(), pb.getPosY(), pb.getWidth(), pb.getHeight());
		
		// Render planets
		for(Planet p : this.planets)
			this.game.spritebatch.draw(p.getRegion(), p.getX(), p.getY(), p.getRadius() * 2, p.getRadius() * 2);
		

		int sun_width = sun.getSun_texture().getWidth();
		int sun_height = sun.getSun_texture().getHeight();
		
		// Render rays
		if(this.raydelta < 180) {
			float size_shaft = 3 + 7f * (float) Math.sin(this.raydelta / 180d * Math.PI);
			game.spritebatch.draw(texture_sunray_shaft, (float) -Project.SCREEN_WIDTH / 2 - sun_width / 2, sun.getPos().y - size_shaft / 2f + sun_height / 4f, 
					Project.SCREEN_WIDTH / 2 + sun.getPos().x + sun_width / 2, size_shaft / 2f, 
					 2f * (float)Project.SCREEN_WIDTH, size_shaft, 
					1f, 1f, sun.getRotation());
			
			game.spritebatch.draw(texture_sunray_shaft, (float) -Project.SCREEN_WIDTH / 2 - sun_width / 2, sun.getPos().y - size_shaft / 2f + sun_height / 4f, 
					Project.SCREEN_WIDTH / 2 + sun.getPos().x + sun_width / 2, size_shaft / 2f, 
					 2f * (float)Project.SCREEN_WIDTH, size_shaft, 
					1f, 1f, sun.getRotation() + 90f);
		}
		
		// Render Sun
		game.spritebatch.draw (sun.getSun_texture(), 
				(float) sun.getPos().x - sun_width / 2, (float) sun.getPos().y - sun_height / 4, 
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
		
		game.spritebatch.end();
	}

	
	private void rayHitsPlanets() {
		ArrayList<Planet> toRemove = new ArrayList<Planet>();
		for(int i = 0; i < 4; i++) {
			for(Planet p : planets) {
				if(sun.getRotation() + 90*i > p.getAngleRelative(sun.getPos().x, sun.getPos().y) -p.getAngularWidth(sun.getPos().x, sun.getPos().y) &&
				   sun.getRotation() + 90*i < p.getAngleRelative(sun.getPos().x, sun.getPos().y) +p.getAngularWidth(sun.getPos().x, sun.getPos().y )) {
					toRemove.add(p);
				}
			}
		}
		for(Planet p : toRemove) {
			planets.remove(p);
		}
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
	}

	public static void prefetch(AssetManager m) {
		Sun.prefetch(m);
		m.load(PB_BORDER_TEXTURE_PATH, Texture.class);
		m.load(PB_INFILL_TEXTURE_PATH, Texture.class);
		
		m.load(BACKGROUND_TEXTURE_PATH, Texture.class);
		
		for(int i = 0; i < PLANET_TEXTURES; i++)
			m.load(PLANETS_TEXTURE_PATH.replace("x", String.valueOf(i)), Texture.class);
		
		m.load(SR_SHAFT, Texture.class);
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
