package com.thetriumvirate.spaceballz;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;

public class Explosion {
	private float x, y;
	private float radius;

	private static final String PATH = "graphics/explosions/explosion1.png";
	public static final float duration = 0.1f;
	
	private int reps;
	
	private float stateTimer;
	
	private static Texture[] textures;
	
	public Explosion(SpaceObject parent, int reps) {
		this.x = parent.getPos().x;
		this.y = parent .getPos().y;
		
		this.reps = 4 * reps;
		
		this.radius = parent.getRadius() * 2;
		
		this.stateTimer = 0.0f;
	}
	
	public float getTimer() {
		return this.stateTimer;
	}
	
	public void setTimer(float timer) {
		this.stateTimer = timer;
	}
	
	public Texture getTexture(float delta) {
		stateTimer += delta;
		
		if(stateTimer < reps * duration) {
			return textures[getIndex()];
		}

		return null;
	}
	
	private int getIndex() {
		int ret = 0;
		
		float f = this.stateTimer % (4 * duration);
		
		while(f > duration) {
			ret++;
			f -= duration;
		}
		
		return ret;
	}
	
	public static void load(AssetManager m) {
		for(int i = 1; i <= 4; i++)
			m.load(PATH.replace("1", String.valueOf(i)), Texture.class);
	}
	
	public static void setup(AssetManager m) {
		textures = new Texture[4];
		for(int i = 1; i <= textures.length; i++)
			textures[i - 1] = m.get(PATH.replace("1", String.valueOf(i)));
	}
	
	
	public float getX() {
		return x;
	}

	public float getY() {
		return y;
	}

	public float getRadius() {
		return radius;
	}

	public float getDuration() {
		return this.reps * duration;
	}
}
