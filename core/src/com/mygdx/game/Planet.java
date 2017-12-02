package com.mygdx.game;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;

public class Planet extends Sprite{
	private static GameScreen screen;
	private static TextureAtlas tAPlanets;
	private static final String TEXTURE = "graphics/planets";
	
	public Body b2body;
	private World world;
	
	private float radius;
	private Vector2 startPos;
	
	private TextureRegion region;
	
	public Planet(GameScreen screen, ArrayList<Planet> planets, TextureRegion region) {
		super(region);
		
		this.region = region;
		Planet.screen = screen;
		
		this.world = screen.getWorld();
		
		this.startPos = new Vector2();
		
		this.setup(planets);
	}
	
	private void setup(ArrayList<Planet> planets) {
		this.radius = 10 + Project.RAND.nextFloat() * 25;
		
		do {
			this.startPos.set(20 + this.radius + Project.RAND.nextFloat() * (Project.SCREEN_WIDTH - 40 - this.radius * 2), 
					20 +  + this.radius + Project.RAND.nextFloat() * (Project.SCREEN_HEIGHT - 40 - this.radius * 2));
		} while (!this.checkLocation(planets));
	}
	
	protected Vector2 getStartLoc() {
		return this.startPos;
	}
	
	private boolean checkLocation(ArrayList<Planet> planets) {
		Vector2 mid = new Vector2(Project.SCREEN_HEIGHT / 2f, Project.SCREEN_WIDTH / 2f);
		
		if(this.startPos.dst(mid) < 250f + this.getRadius())
			return false;
		
		for(Planet p : planets) {
			Vector2 p1 = p.getStartLoc();
			if(this.startPos.dst(p1) < p.getRadius() + 3f + this.getRadius())
				return false;
		}
		
		return true;
	}
	
	public void define() {
		BodyDef bdef = new BodyDef();
		bdef.position.set(this.startPos);
		bdef.type = BodyDef.BodyType.DynamicBody;
		this.b2body = world.createBody(bdef);
		
		FixtureDef fdef = new FixtureDef();
		CircleShape shape = new CircleShape();
		shape.setRadius(this.radius);
		
		fdef.shape = shape;
		
		b2body.createFixture(fdef);
	}

	public void update(float delta) {
		this.setPosition(this.b2body.getPosition().x - this.getWidth() / 2, this.b2body.getPosition().y - this.getHeight() / 2);
	}
	
	public float getRadius() {
		return this.radius;
	}
	
	@Override
	public void draw(Batch batch) {
		this.setScale(this.radius / 64 * 2);
		super.draw(batch);
		this.setScale(64 * 2 / this.radius);
	}


	public static class PlanetManager {
		/* PLANET SETUP
		 * produces an Array of Planets that is randomly distributed on the Screen
		 */
		
		public static ArrayList<Planet> setupPlanets(int count, GameScreen screen) {
			tAPlanets = screen.game.assetmanager.get(TEXTURE + ".pack");
			
			ArrayList<Planet> planets = new ArrayList<Planet>();
			
			// Planet init, creates -count- planets
			planets  = new ArrayList<Planet>();
			do {
				planets.clear();
				for(int i = 0; i < count; i++) 
					planets.add(new Planet(screen, planets, tAPlanets.findRegion("planet" + ((i % 7) + 1))));
			} while (!checkPlanets(planets));
			
			for(Planet p : planets)
				p.define();
			
			return planets;
		}
		
		private static boolean checkPlanets(ArrayList<Planet> planets) {
			float x = 0f;
			float y = 0f;
			
			for(Planet p : planets) {
				x += p.getStartLoc().x;
				y += p.getStartLoc().y;
			}
			
			// Get the average position to prevent the planets from only being on one side of the screen
			
			x /= planets.size();
			y /= planets.size();
			
			Vector2 delta = new Vector2(x, y);
			Vector2 mid = new Vector2(Project.SCREEN_WIDTH / 2, Project.SCREEN_HEIGHT / 2);
			
			if(mid.dst(delta) > 100)
				return false;
			
			return true;
		}

		public static void loadGraphics(GameScreen screen) {
			Planet.screen = screen;
			screen.game.assetmanager.load(TEXTURE + ".pack", TextureAtlas.class);
		}
	}
}
