package com.thetriumvirate.spaceballz;

import java.util.ArrayList;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;

public class Planet implements SpaceObject{
	private float radius;
	private Vector2 origin;
	private Vector2 pos;
	private float dst;
	
	private Texture texture;
	private GameScreen screen;
	
	private Circle shape;
	
	private float vel;
	private float animationTimer;
	
	public Planet(GameScreen screen, ArrayList<Planet> planets, Texture texture) {
		this.texture = texture;
		this.screen = screen;
		
		this.origin = new Vector2();
		this.pos = new Vector2();
		
		this.animationTimer = 0f;
		this.vel = (Project.RAND.nextFloat() - 0.5f) * 14f;
		
		this.setup(planets);
		
		this.pos = this.origin;
		this.shape = new Circle(this.origin.x + this.radius, this.origin.y + this.radius, this.radius);
	}
	
	public void update(float delta) {
		this.animationTimer += delta * this.vel;
		
		float x = screen.sun.getOriginPos().x + (float) Math.cos(Math.toRadians(this.animationTimer)) * dst * Project.RATIO;
		float y = screen.sun.getOriginPos().y + (float) Math.sin(Math.toRadians(this.animationTimer)) * dst;
		
		this.pos.set(x, y);
		
//		this.pos.set(screen.sun.getPos().x + (float) Math.cos(Math.toRadians(this.animationTimer)) * dst * Project.RATIO, screen.sun.getPos().y + (float) Math.sin(Math.toRadians(this.animationTimer)) * dst);
//		
		this.shape.setPosition(this.pos.x + this.radius, this.pos.y + this.radius);
	}
	
	private void setup(ArrayList<Planet> planets) {
		this.radius = 10 + Project.RAND.nextFloat() * 25;
		float x;
		float y;
		
		Vector2 rel = screen.sun.getOriginPos();
		
		do {
			this.animationTimer = Project.RAND.nextFloat() * 360f;
			dst = Project.RAND.nextFloat() * (Project.SCREEN_HEIGHT / 2f - screen.sun.getRadius() - 50f) + screen.sun.getRadius() + 50f;
			
			x = rel.x + (float) Math.cos(Math.toRadians(this.animationTimer)) * dst * Project.RATIO;
			y = rel.y + (float) Math.sin(Math.toRadians(this.animationTimer)) * dst;
			
//			x = 20 + this.radius + Project.RAND.nextFloat() * (Project.SCREEN_WIDTH - 40 - this.radius * 2) ;
//			y = 20 +  + this.radius + Project.RAND.nextFloat() * (Project.SCREEN_HEIGHT - 40 - this.radius * 2);
			
			this.origin.set(x, y);
		} while (!this.checkLocation(planets));
		
	}
	
	private boolean checkLocation(ArrayList<Planet> planets) {
		if(this.origin.dst(screen.sun.getPos()) < 250f + this.getRadius())
			return false;
		
		for(Planet p : planets) {
			if(this.origin.dst(p.origin) < p.getRadius() + 3f + this.getRadius())
				return false;
		}
		
		return true;
	}
	
	public boolean contains(float x, float y) {
		return this.origin.dst(x, y) < this.radius;
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
