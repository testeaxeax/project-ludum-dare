package com.mygdx.game;

import java.util.ArrayList;

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
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.utils.ScissorStack;

public final class GameScreen implements Screen {
	
	private static final int CAM_WIDTH = Project.SCREEN_WIDTH;
	private static final int CAM_HEIGHT = Project.SCREEN_HEIGHT;
	
	private Project game;
	private OrthographicCamera cam;
	
	public Sun sun;
	private TextureRegion texture_sunray_shaft;
	private static final String SR_SHAFT = "graphics/rayshaft.png";
	
//	private float pbAnimationTimer;
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
	
	private int level;
	
	private int score = 0;
	private float scorePosX = 0.8f * Project.SCREEN_WIDTH, scorePosY = 0.9f * Project.SCREEN_HEIGHT;
	private GlyphLayout scoreLayout;
	
	private Music music;
	
	private Sound alarmSound;
	private static final String ALARM_WAV_PATH = "audio/sounds/alarm.wav";
	
	private Texture warning;
	private static final String TEX_WARNING = "graphics/warning.png";
	private float warning_sin;
	private boolean warned_before;
	
	private boolean shoot;
	private int misses;
	
	private Polygon rayPol;
	private boolean drawTutorial;
	private Texture tutorialOverlay;
	private static final String TUTORIAL_OVERLAY_PATH = "graphics/tutOverlay.png";
	
	public GameScreen(Project game) {
		this.game = game;
		
		cam = new OrthographicCamera();
		cam.setToOrtho(false, CAM_WIDTH, CAM_HEIGHT);
		cam.update();
		
		game.spritebatch.setProjectionMatrix(cam.combined);
		Gdx.gl.glClearColor(0, 0, 1, 1);
		
		this.tutorialOverlay = game.assetmanager.get(TUTORIAL_OVERLAY_PATH);
		
//		pbAnimationTimer = 0f;
		pbBorder = game.assetmanager.get(PB_BORDER_TEXTURE_PATH);
		pbInfill = game.assetmanager.get(PB_INFILL_TEXTURE_PATH);
		
		background = game.assetmanager.get(BACKGROUND_TEXTURE_PATH);
		
		warning = game.assetmanager.get(TEX_WARNING, Texture.class);
		this.warning_sin = 0f;
		this.warned_before = false;
		
		
		pb = new ProgressBar(30, Project.SCREEN_HEIGHT, Project.SCREEN_WIDTH - 30, 0, pbBorder, pbInfill);
		pb.setPercentage(0.2f);
		
		sun = new Sun(this, Project.SCREEN_WIDTH / 2, Project.SCREEN_HEIGHT / 2, 0, 100, 30, 2);
		texture_sunray_shaft = new TextureRegion((Texture) game.assetmanager.get(SR_SHAFT));

		this.planetTextures = new Texture[PLANET_TEXTURES];
		
		for(int i = 0; i < PLANET_TEXTURES; i++)
			this.planetTextures[i] = game.assetmanager.get(PLANETS_TEXTURE_PATH.replace("x", String.valueOf(i)), Texture.class);

		this.planets = PlanetManager.setupPlanets(MAX_PLANETS, this);
		
		
		this.raydelta = 250d;
		
		scoreLayout = new GlyphLayout();
		
		Explosion.setup(game.assetmanager);
		this.explosions = new ArrayList<Explosion>();
		
		
		alarmSound = game.assetmanager.get(ALARM_WAV_PATH, Sound.class);
		
		
//		music = game.assetmanager.get("audio/music/music.ogg", Music.class);
//		music.setLooping(true);
//		music.setVolume(0.1f);		
//		music.play();
		
		this.rayPol = createPolygon();
		this.shoot = false;
		this.misses = 0;
	
		this.drawTutorial = true;
		
		this.level = 0;
	}

	@Override
	public void render(float delta) {
		if(planets.isEmpty()) {
			this.planets = PlanetManager.setupPlanets(MAX_PLANETS, this);
			level++;
			sun.levelUp(1);
		}
		
		for(Planet p : this.planets)
			p.update(delta);
		
		if(this.raydelta < 250d)
			this.raydelta += 7;
		
		if(Gdx.input.justTouched() && this.raydelta >= 250d && sun.getTemp() > 3) {
			this.raydelta = 0d;
			
			shoot = true;
			this.drawTutorial = false;
		} else
			shoot = false;
		
		
		sun.update(delta, this.raydelta <= 180);
		pb.update(sun);
		
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		game.spritebatch.begin();
		
		//BACKGROUND has to be drawn FIRST!!
		game.spritebatch.draw(background, 0, 0, Project.SCREEN_WIDTH, Project.SCREEN_HEIGHT);
		
		// Render planets
		for(Planet p : this.planets)
			this.game.spritebatch.draw(p.getRegion(), p.getPos().x, p.getPos().y, p.getRadius() * 2, p.getRadius() * 2);
		

		int sun_width = sun.getSunRadius();
		int sun_height = sun.getSunRadius();
		
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
					1f, 1f, 360f - sun.getRotation());
			
			game.spritebatch.draw(texture_sunray_shaft, (float) Project.SCREEN_WIDTH / 2 - length / 2f - sun_width / 2, sun.getPos().y - size_shaft / 2f, 
					length / 2f + sun_width / 2, size_shaft / 2f, 
					length, size_shaft, 
					1f, 1f, 360f - sun.getRotation() + 90f);
			
		}
		
		// Render Sun
		game.spritebatch.draw (sun.getSun_texture(), 
				(float) sun.getPos().x - sun.getSun_texture().getWidth() / 2, (float) sun.getPos().y - sun.getSun_texture().getHeight() / 2, 
				(float) sun.getSun_texture().getWidth() / 2, (float) sun.getSun_texture().getHeight() / 2, (float) sun.getSun_texture().getWidth(), (float) sun.getSun_texture().getHeight(), sun.getSun_texture().getWidth() / sun_width, sun.getSun_texture().getHeight() / sun_height, 
				360f - sun.getRotation(), 
				0, 0, (int) sun.getSun_texture().getWidth(), (int) sun.getSun_texture().getHeight(), 
				false, false);
		
		// Render warning
		if(this.sun.getTemp() / Sun.MAX_TEMP > 0.8f) {

			Color c = game.spritebatch.getColor();
			c = game.spritebatch.getColor();
			game.spritebatch.setColor(c.r, c.g, c.b, (float) (Math.sin(warning_sin)  + 1f ) / 3f);
			
			if(!this.warned_before) {
				warning_sin = 0f;
				warned_before = true;
				alarmSound.loop(0.5f);
			}
			else
				warning_sin += delta * 4.5f;
			
			game.spritebatch.draw(warning, 0, 0, Project.SCREEN_WIDTH, Project.SCREEN_HEIGHT);
			
			c = game.spritebatch.getColor();
			game.spritebatch.setColor(c.r, c.g, c.b, 1f);
			
			
		} else {
			warned_before = false;
			alarmSound.stop();
		}
		
		
		// Render progress bar
		float x = 6f;
		Rectangle scissors = new Rectangle();
		Rectangle clipBounds = new Rectangle(pb.getPosX(), pb.getPosY(), pb.getWidth(), pb.getHeight() * pb.getAnimationProgress(delta));
		ScissorStack.calculateScissors(cam, game.spritebatch.getTransformMatrix(), clipBounds, scissors);

		game.spritebatch.end();
		ScissorStack.pushScissors(scissors);
		game.spritebatch.begin();
		
		game.spritebatch.draw(pb.getInfillTexture(), pb.getPosX() + pb.getWidth() / 10, pb.getPosY() + x, pb.getWidth() - 14f/60f * pb.getWidth(), pb.getHeight());
		game.spritebatch.flush();
		ScissorStack.popScissors();
		
//		scissors = new Rectangle();
//		clipBounds = new Rectangle(pb.getPosX(), pb.getPosY() + pb.getHeight() * pb.getAnimationProgress(), pb.getWidth(), pbAnimation.getHeight() / 2f);
//		ScissorStack.calculateScissors(cam, game.spritebatch.getTransformMatrix(), clipBounds, scissors);
//		
//		game.spritebatch.end();
//		ScissorStack.pushScissors(scissors);
//		game.spritebatch.begin();
//		
//		game.spritebatch.draw(pbAnimation, clipBounds.getX() - this.pbAnimationTimer, clipBounds.getY(), pbAnimation.getWidth(), clipBounds.getHeight());
//		if(this.pbAnimationTimer > pbAnimation.getWidth() - pb.getWidth()) {
//			game.spritebatch.draw(pbAnimation, clipBounds.getX() + this.pbAnimation.getWidth() - this.pbAnimationTimer, clipBounds.getY(), pbAnimation.getWidth(), clipBounds.getHeight());
//		}
//		
//		game.spritebatch.flush();
//		ScissorStack.popScissors();
//		
//		this.pbAnimationTimer += delta * 80f;
		
		game.spritebatch.draw(pb.getBorderTexture(),pb.getPosX(), pb.getPosY(), pb.getWidth(), pb.getHeight());

		
		scoreLayout.setText(game.font, "Score: "  + score + "\n" + "Level: " + (level + 1));
		game.font.draw(game.spritebatch, scoreLayout, scorePosX, scorePosY);
		
		if(this.drawTutorial)
			game.spritebatch.draw(this.tutorialOverlay, 0, 0, Project.SCREEN_WIDTH, Project.SCREEN_HEIGHT);
		
		game.spritebatch.end();
		
		ShapeRenderer sr = new ShapeRenderer();
		
		sr.setColor(shoot ? Color.WHITE : Color.RED);
		sr.setProjectionMatrix(cam.combined);
		sr.begin(ShapeType.Line);

		
		if(rayHitsPlanets(sr) && shoot) {
			sun.decreasetemp(2);
			this.misses = 0;
			game.assetmanager.get("audio/sounds/planet_vanish.wav", Sound.class).play(0.1f);
		}
		else if(shoot){
			if(misses > 3)
				sun.increasetemp(2);
			game.assetmanager.get("audio/sounds/beam.wav", Sound.class).play(0.2f);
			misses++;
		}
		
		sr.end();
	}

	private Polygon createPolygon() {
		Vector2 rel = sun.getPos();
		
		float width = Project.SCREEN_WIDTH * 2f;
		float height = 10f;
		float x = rel.x - Project.SCREEN_WIDTH;
		float y = rel.y - height / 2f;
		
		Polygon ret = new Polygon(new float[]{x, y, x + width, y, x + width, y + height, x, y + height});
		ret.setOrigin(rel.x, rel.y);
		
		return ret;
	}

	
	private boolean rayHitsPlanets(ShapeRenderer r) {
		ArrayList<Planet> toRemove = new ArrayList<Planet>();
		
		rayPol.rotate(360 - sun.getRotation());
		
//		r.polygon(rayPol.getTransformedVertices());
		
		if(this.shoot) {
			for(Planet p : this.planets)
				if(overlaps(rayPol, p.getShape()) && !toRemove.contains(p)) {
						toRemove.add(p);
						score++;
					}
		}
		rayPol.rotate(90);
		
		if(this.shoot) {
			for(Planet p : this.planets)
				if(overlaps(rayPol, p.getShape()) && !toRemove.contains(p)) {
						toRemove.add(p);
						score++;
					}
			
		}

//		r.polygon(rayPol.getTransformedVertices());
//		
//		for(Planet p : this.planets)
//			r.circle(p.getShape().x, p.getShape().y, p.getShape().radius);
		

		if(shoot)
			for(Planet p : toRemove) {
				this.planets.remove(p);
				this.explosions.add(new Explosion(p));
			}
		
		rayPol.rotate(sun.getRotation() - 90);
		
		return toRemove.size() > 0;
	}
	
	public boolean overlaps(Polygon polygon, Circle circle) {
	    float []vertices=polygon.getTransformedVertices();
	    Vector2 center=new Vector2(circle.x, circle.y);
	    float squareRadius=circle.radius*circle.radius;
	    for (int i=0;i<vertices.length;i+=2){
	        if (i==0){
	            if (Intersector.intersectSegmentCircle(new Vector2(vertices[vertices.length-2], vertices[vertices.length-1]), new Vector2(vertices[i], vertices[i+1]), center, squareRadius))
	                return true;
	        } else {
	            if (Intersector.intersectSegmentCircle(new Vector2(vertices[i-2], vertices[i-1]), new Vector2(vertices[i], vertices[i+1]), center, squareRadius))
	                return true;
	        }
	    }
	    return false;
	}
	
	public ArrayList<Planet> checkPlanets(float x, float y, ArrayList<Planet> array) {
		for(Planet p : this.planets)
			if(p.contains(x, y) && !array.contains(p))
				array.add(p);
		
		return array;
	}
	
	public ProgressBar getPB() {
		return this.pb;
	}
	
	public void gameover() {
		alarmSound.stop();
		game.screenmanager.set(new GameoverMenuScreen(game, score, level + 1));
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
//		m.load(PB_ANIMATION_TEXTURE_PATH, Texture.class);
		
		m.load(BACKGROUND_TEXTURE_PATH, Texture.class);
		
		for(int i = 0; i < PLANET_TEXTURES; i++)
			m.load(PLANETS_TEXTURE_PATH.replace("x", String.valueOf(i)), Texture.class);
		
		m.load(SR_SHAFT, Texture.class);
		
		m.load(TEX_WARNING, Texture.class);
		
		Explosion.load(m);
		
		m.load(ALARM_WAV_PATH, Sound.class);
		
		m.load(TUTORIAL_OVERLAY_PATH, Texture.class);
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
		
		game.assetmanager.unload(ALARM_WAV_PATH);
		game.assetmanager.unload(TUTORIAL_OVERLAY_PATH);
	}

	public Texture[] getPlanetTextures() {
		return this.planetTextures;
	}
	
	public ArrayList<Planet> getPlanets() {
		return planets;
	}
}
