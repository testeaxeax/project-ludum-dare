package com.mygdx.game;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;

public class Sun {
	
	private static final String SUN_TEXTURE_PATH = "graphics/sun_texture.png";
	
	private static int MAX_TEMP = 100;
	public static int WIDTH = 400;
	public static int HEIGHT = 400;
	
	private Vector2 pos;
	private Vector2 origin;
	private float degree;
	private float speed;
	private int radius;
	private int temp;
	private int deltatemp;
	private int sun_radius;
	private int rotation;
	private int deltarotation;
	private Texture sun_texture;
	

	public Sun(GameScreen gamescreen, int x_origin, int y_origin, int radius, int sun_radius, int deltatemp, int deltarotation) {
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
		temp = 0;
		sun_texture = gamescreen.getGame().assetmanager.get(SUN_TEXTURE_PATH, Texture.class);
	}
	
	public void update(float delta) {
		temp += deltatemp * delta;
		if(temp > MAX_TEMP) {
			// TODO uncomment once implemented
			//game.gameover();
		} else {
			rotation = (int) ((rotation + deltarotation * delta) % 360);
			degree = (degree + speed * delta) % 360;
			pos.x = (int) (origin.x + Math.cos(degree) * radius);
			pos.y = (int) (origin.y + Math.sin(degree) * radius);
		}
	}
	
	public void increasetemp(int delta) {
		temp += delta;
	}
	
	public void decreasetemp(int delta) {
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
		
		switch (blast_index){
		case 0:
			blast_degree = 0 + rotation;
			break;
		case 1:
			blast_degree = 90 + rotation;
			break;
		case 2:
			blast_degree = 180 + rotation;
			break;
		case 3:
			blast_degree = 270 + rotation;
		}
		blast_degree += rotation;
		blast_pos.x = (int) (pos.x + Math.cos(blast_degree) * sun_radius);
		blast_pos.y = (int) (pos.y + Math.cos(blast_degree) * sun_radius);
		return blast_pos;
	}
	
	public int getRadius() {
		return radius;
	}

	public void setRadius(int radius) {
		this.radius = radius;
	}

	public int getRotation() {
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

	public Vector2 getPos() {
		return pos;
	}
	
	public static void prefetch(AssetManager m) {
		m.load(SUN_TEXTURE_PATH, Texture.class);
	}
	
	public static void dispose(AssetManager m) {
		m.unload(SUN_TEXTURE_PATH);
	}
}
