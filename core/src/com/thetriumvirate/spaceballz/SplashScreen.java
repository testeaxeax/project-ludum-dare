package com.thetriumvirate.spaceballz;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
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
	private static final int MIN_SHOWTIME = 2 * 1000;
	public static final float ANIMATION_DURATION = 4000f;
	private static final float SIZE = 256f;
	
	private Project game;
	private OrthographicCamera cam;
	private long showtime;
	
	private Animation<TextureRegion> animation;
	
	private float stateTimer;
	private float animationTimer;
	private float posY;
	
	private MainMenuScreen nextScreen;

	public SplashScreen(Project game) {
		this.game = game;
		
		for(int i = 1; i <= 13; i++)
			game.assetmanager.load(("graphics/lavalamp/LavaLamp-x.png").replace("x", String.valueOf(i)), Texture.class);
		
		game.assetmanager.finishLoading();
		
		Array<TextureRegion> array = new Array<TextureRegion>();
		
		for(int i = 1; i <= 13; i++)
			array.add(new TextureRegion(game.assetmanager.get(("graphics/lavalamp/LavaLamp-x.png").replace("x", String.valueOf(i)), Texture.class)));
		
		this.animation = new Animation<TextureRegion>(0.08f, array);
		
		this.animationTimer = 0f;
		this.posY = 0f;
		
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
		
		if(this.posY > CAM_HEIGHT) {
			this.dispose();
			game.screenmanager.set(this.nextScreen);
		} else {
			Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
			game.spritebatch.begin();
			if(this.checkprogress()) {
				if(this.nextScreen == null) {
						game.music = game.assetmanager.get("audio/music/music.wav", Music.class);
						game.music.setLooping(true);
						game.music.setVolume(0.1f);
						this.nextScreen = new MainMenuScreen(game);
					}
				
				this.posY += (-(float) Math.cos(Math.PI / 1.5f * this.animationTimer / ANIMATION_DURATION) + 1f) * CAM_HEIGHT * 1.5f;
				
				this.animationTimer += delta * 1000f;
				
				game.spritebatch.end();
				this.nextScreen.render(delta, (float) posY);
				game.spritebatch.begin();
			}
			
			game.spritebatch.draw(this.animation.getKeyFrame(this.stateTimer, true), CAM_WIDTH / 2f - SIZE / 2f, posY, SIZE, SIZE);

			game.spritebatch.end();
			
			this.stateTimer += delta;
		}
	}

	private boolean checkprogress() {
		return TimeUtils.timeSinceMillis(showtime) >= MIN_SHOWTIME && game.assetmanager.update();
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
