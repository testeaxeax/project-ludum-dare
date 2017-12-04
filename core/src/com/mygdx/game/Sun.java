package com.mygdx.game;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;

public class Sun implements SpaceObject {
	
	private static final String SUN_TEXTURE_PATH = "graphics/sun_texture.png";
	
	public static int MAX_TEMP = 100;

	private Vector2 pos;
	private Vector2 origin;
	private float degree;
	private float speed;
	private int radius;
	private float temp;
	private float deltatemp;
	private int sun_radius;
	private float rotation;
	private float deltarotation;
	private Texture sun_texture;
	private GameScreen gamescreen;
	private Explosion explosion = null;
	private Sound sound;
	
	private int level;
	

	public Sun(GameScreen gamescreen, int x_origin, int y_origin, int radius, int sun_radius, int deltarotation, int deltatemp) {
		int x;
		int y;
		
		this.radius = radius;
		this.sun_radius = sun_radius;
		this.rotation = 0;
		this.deltarotation = deltarotation;
		this.deltatemp = deltatemp;
		this.origin = new Vector2(x_origin, y_origin);
		x = (int) (origin.x + Math.cos(degree) * this.radius);
		y = (int) (origin.y + Math.sin(degree) * this.radius);
		this.pos = new Vector2(x, y);
		degree = 0;
		temp = 50;
		this.gamescreen = gamescreen;
		sun_texture = gamescreen.getGame().assetmanager.get(SUN_TEXTURE_PATH, Texture.class);
	}
	
	public void update(float delta, boolean touched) {
		temp += deltatemp * delta * 1f;
		deltarotation = temp * (2 + level / 3f);
		if(temp > MAX_TEMP) {
			if(explosion == null) {
				explosion = new Explosion(this);
				gamescreen.getGame().assetmanager.get(GameScreen.EXPLOSION_SUN_ASSET_PATH, Sound.class).play(0.1f);
			}
			gamescreen.getExplosions().add(explosion);
			if(explosion.getTimer() > Explosion.duration) {
				gamescreen.gameover();
			}
		} else if(!touched){
			rotation = ((rotation + deltarotation * delta) % 360);
			degree = (degree + speed * delta) % 360;
		}
	}

	public void levelUp(int i) {
		this.level++;
		
		this.decreasetemp(this.temp - 20f);
		
		i--;
		if(i > 0)
			levelUp(i);
	}
	
	public void increasetemp(int delta) {
		if(temp < MAX_TEMP) {
			temp += delta;
			deltarotation += 2 * delta;
		}
		else {
			temp = MAX_TEMP;
			
		}
	}
	
	public void decreasetemp(float delta) {
		if(delta <= temp) {
			temp -= delta;
		} else {
			temp = 0;
		}
	}
	
	
	
	// blast is the index of the wanted blast, so something between 0 and 3
	public Vector2 getNewblastposition(int blast_index) {
		Vector2 blast_pos = new Vector2();
		int blast_degree = 0;
		
		rotation = rotation % 360;
		
		switch (blast_index){
		case 0:
			blast_degree = (int) (0 + rotation);
			break;
		case 1:
			blast_degree = (int) (90 + rotation);
			break;
		case 2:
			blast_degree = (int) (180 + rotation);
			break;
		case 3:
			blast_degree = (int) (270 + rotation);
		}

		blast_pos.x = (float) (Math.cos(2 * Math.PI - Math.toRadians(blast_degree)) * sun_radius);
		blast_pos.y = (float) (Math.sin(2 * Math.PI - Math.toRadians(blast_degree)) * sun_radius);
		return blast_pos;
	}
	
	public float getOrbitRadius() {
		return radius;
	}
	
	public float getRadius() {
		return sun_radius;
	}
	
	public void setRadius(int radius) {
		this.radius = radius;
	}

	public float getRotation() {
		return rotation;
	}

	public void setRotation(int rotation) {
		this.rotation = rotation;
	}

	public float getSpeed() {
		return speed;
	}

	public void setSpeed(float speed) {
		this.speed = speed;
	}
	
	public Texture getSun_texture() {
		return sun_texture;
	}

	public Vector2 getOriginPos() {
		return pos;
	}
	
	public Vector2 getPos() {
		float x = pos.x;
		float y = pos.y;
		
		return new Vector2(x - sun_texture.getWidth() / 2 + 10, y - sun_texture.getHeight() / 2 + 10);
	}
	
	public static void prefetch(AssetManager m) {
		m.load(SUN_TEXTURE_PATH, Texture.class);
	}
	
	public static void dispose(AssetManager m) {
		m.unload(SUN_TEXTURE_PATH);
	}

	public float getTemp() {
		return temp;
	}
}
