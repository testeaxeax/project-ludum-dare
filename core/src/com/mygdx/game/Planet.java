package com.mygdx.game;

import java.util.ArrayList;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;

public class Planet{
	private float radius;
	private Vector2 pos;
	
	private Texture texture;
	private GameScreen screen;
	
	private Circle shape;
	
	public Planet(GameScreen screen, ArrayList<Planet> planets, Texture texture) {
		this.texture = texture;
		this.screen = screen;
		
		this.pos = new Vector2();
		
		this.setup(planets);
		
		this.shape = new Circle(this.pos.x + this.radius, this.pos.y + this.radius, this.radius);
	}
	
	private void setup(ArrayList<Planet> planets) {
		this.radius = 10 + Project.RAND.nextFloat() * 25;
		float x = 0f;
		float y = 0f;
		
		do {
			x = 20 + this.radius + Project.RAND.nextFloat() * (Project.SCREEN_WIDTH - 40 - this.radius * 2) ;
			y = 20 +  + this.radius + Project.RAND.nextFloat() * (Project.SCREEN_HEIGHT - 40 - this.radius * 2);
			this.pos.set(x, y);
		} while (!this.checkLocation(planets));
		
	}
	
	private boolean checkLocation(ArrayList<Planet> planets) {
		Vector2 mid = new Vector2(Project.SCREEN_HEIGHT / 2f, Project.SCREEN_WIDTH / 2f);
		
		if(this.pos.dst(mid) < 250f + this.getRadius())
			return false;
		
		for(Planet p : planets) {
			if(this.pos.dst(p.pos) < p.getRadius() + 3f + this.getRadius())
				return false;
		}
		
		return true;
	}
	
	public boolean contains(float x, float y) {
		return this.pos.dst(x, y) < this.radius;
	}
	
	public float getRadius() {
		return this.radius;
	}

	public Texture getRegion() {
		return texture;
	}

	public Vector2 getPos() {
		return this.pos;
	}
	
	public Circle getShape() {
		return this.shape;
	}
}
