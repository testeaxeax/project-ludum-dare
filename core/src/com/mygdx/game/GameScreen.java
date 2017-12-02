package com.mygdx.game;

import java.util.ArrayList;

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
	
	private static final int PLANET_TEXTURES = 7;
	private Texture[] tAPlanets;
	private static final String PLANETS_TEXTURE_PATH = "graphics/planets/planetx.png";
	
	private ArrayList<Planet> planets;
	
	public GameScreen(Project game) {
		this.game = game;
		cam = new OrthographicCamera();
		cam.setToOrtho(false, CAM_WIDTH, CAM_HEIGHT);
		cam.update();

		this.tAPlanets = new Texture[PLANET_TEXTURES];
		
		for(int i = 0; i < PLANET_TEXTURES; i++)
			this.tAPlanets[i] = game.assetmanager.get(PLANETS_TEXTURE_PATH.replace("x", String.valueOf(i)), Texture.class);
		
		this.planets = PlanetManager.setupPlanets(10, this);
		
		game.spritebatch.setProjectionMatrix(cam.combined);
		Gdx.gl.glClearColor(0, 0, 0, 1);
	}

	@Override
	public void show() {
		
	}
	
	@Override
	public void render(float delta) {
		

		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		this.game.spritebatch.begin();
		for(Planet p : this.planets)
			this.game.spritebatch.draw(p.getRegion(), p.getX(), p.getY(), p.getRadius() * 2, p.getRadius() * 2);
		this.game.spritebatch.end();
	}

	public static void prefetch(AssetManager m) {
		for(int i = 0; i < PLANET_TEXTURES; i++)
			m.load(PLANETS_TEXTURE_PATH.replace("x", String.valueOf(i)), Texture.class);
	}
	
	public Texture[] getPlanetTextures() {
		return this.tAPlanets;
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
		for(int i = 0; i < PLANET_TEXTURES; i++)
			game.assetmanager.unload(PLANETS_TEXTURE_PATH.replace("x", String.valueOf(i)));
	}

	public ArrayList<Planet> getPlanets() {
		return planets;
	}
}
