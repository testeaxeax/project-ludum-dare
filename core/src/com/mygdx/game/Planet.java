package com.mygdx.game;

import java.util.ArrayList;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;

public class Planet{
	private float radius;
	private float x, y;
	
	private Texture texture;
	private GameScreen screen;
	
	public Planet(GameScreen screen, ArrayList<Planet> planets, Texture texture) {
		this.texture = texture;
		this.screen = screen;
		
		this.x = 0f;
		this.y = 0f;
		
		this.setup(planets);
	}
	
	private void setup(ArrayList<Planet> planets) {
		this.radius = 10 + Project.RAND.nextFloat() * 25;
		
		do {
			this.x = 20 + this.radius + Project.RAND.nextFloat() * (Project.SCREEN_WIDTH - 40 - this.radius * 2) ;
			this.y = 20 +  + this.radius + Project.RAND.nextFloat() * (Project.SCREEN_HEIGHT - 40 - this.radius * 2);
		} while (!this.checkLocation(planets));
	}
	
	private boolean checkLocation(ArrayList<Planet> planets) {
		Vector2 mid = new Vector2(Project.SCREEN_HEIGHT / 2f, Project.SCREEN_WIDTH / 2f);
		Vector2 pos = new Vector2(this.x, this.y);
		
		if(pos.dst(mid) < 250f + this.getRadius())
			return false;
		
		for(Planet p : planets) {
			Vector2 p1 = new Vector2(p.getX(), p.getY());
			if(pos.dst(p1) < p.getRadius() + 3f + this.getRadius())
				return false;
		}
		
		return true;
	}
	
	public float getRadius() {
		return this.radius;
	}

	public Texture getRegion() {
		return texture;
	}

	public float getX() {
		return x;
	}

	public float getY() {
		return y;
	}
	
	public void destroy() {
		this.screen.getGame().assetmanager.get("audio/sounds/planet_vanish.wav", Sound.class).play();
	}
}
