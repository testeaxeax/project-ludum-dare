package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;

public final class SplashScreen implements Screen {

	private static final int CAM_WIDTH = Project.SCREEN_WIDTH;
	private static final int CAM_HEIGHT = Project.SCREEN_HEIGHT;
	// SplashScreen will be displayed for at least 5 seconds
	private static final int MIN_SHOWTIME = 4 * 1000;

	private Project game;
	private OrthographicCamera cam;
	private long showtime;
	
	private Animation<TextureRegion> animation;
	
	private float stateTimer;

	public SplashScreen(Project game) {
		this.game = game;
		
		for(int i = 1; i <= 13; i++)
			game.assetmanager.load(("graphics/lavalamp/LavaLamp-x.png").replace("x", String.valueOf(i)), Texture.class);
		
		game.assetmanager.finishLoading();
		
		Array<TextureRegion> array = new Array<TextureRegion>();
		
		for(int i = 1; i <= 13; i++)
			array.add(new TextureRegion(game.assetmanager.get(("graphics/lavalamp/LavaLamp-x.png").replace("x", String.valueOf(i)), Texture.class)));
		
		this.animation = new Animation<TextureRegion>(0.08f, array);
		
		this.stateTimer = 0f;
		array.clear();
		
		showtime = 0;
		cam = new OrthographicCamera();
		cam.setToOrtho(false, CAM_WIDTH, CAM_HEIGHT);
		cam.update();
		game.spritebatch.setProjectionMatrix(cam.combined);
		Gdx.gl.glClearColor(200f / 255f, 200f / 255f, 200f / 255f, 1f);
	}

	@Override
	public void show() {
		GameScreen.prefetch(game.assetmanager);
		MainMenuScreen.prefetch(game.assetmanager);
		CreditsScreen.prefetch(game.assetmanager);
		GameoverMenuScreen.prefetch(game.assetmanager);
		showtime = TimeUtils.millis();
	}

	@Override
	public void render(float delta) {
		checkprogress();
		
		float size = 256;
		
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		game.spritebatch.begin();
		
		game.spritebatch.draw(this.animation.getKeyFrame(this.stateTimer, true), CAM_WIDTH / 2 - size / 2, CAM_HEIGHT / 2 - size / 2, size, size);
		
		game.spritebatch.end();
		
		this.stateTimer += delta;
	}

	private void checkprogress() {
		boolean loaded = game.assetmanager.update();
		if (TimeUtils.timeSinceMillis(showtime) >= MIN_SHOWTIME && loaded) {
			// TODO replace ScreenTemplate with actual game/menu screen
			game.screenmanager.set(new MainMenuScreen(game));
		}
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
		for(int i = 1; i <= 13; i++)
			game.assetmanager.unload(("graphics/lavalamp/LavaLamp-x.png").replace("x", String.valueOf(i)));
	}
}
