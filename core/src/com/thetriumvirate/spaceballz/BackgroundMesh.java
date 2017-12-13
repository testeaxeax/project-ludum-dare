package com.thetriumvirate.spaceballz;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;

public class BackgroundMesh {
	private static final int PARTICLES = (int) (Project.SCREEN_WIDTH * 0.146484375f);
	private static final int RANGE = (int) (Project.SCREEN_WIDTH * 0.146484375f);
	
	private ArrayList<Particle> particles;
	private ArrayList<Particle> inRange;
	
	public BackgroundMesh(Project game) {
		
		this.particles = new ArrayList<Particle>();
		this.inRange = new ArrayList<Particle>();
		
		for(int i = 0; i < PARTICLES; i++) {
			this.particles.add(new Particle(Project.SCREEN_WIDTH / PARTICLES, Project.SCREEN_HEIGHT / PARTICLES));
		}
		
		for(int i = 0; i < 100; i++)
			update(1f, 0f, 0f);
	}

	public void render(float delta, OrthographicCamera cam) {
		ShapeRenderer sr = new ShapeRenderer();
		sr.setColor(Color.WHITE);
		sr.setProjectionMatrix(cam.combined);

		sr.begin(ShapeType.Line);
		
		for(int i = 0; i < this.inRange.size() - 1; i++) {
			Vector2 v1 = this.inRange.get(i).pos;
			Vector2 v2 = this.inRange.get(i + 1).pos;
			sr.line(v1.x, v1.y, v2.x, v2.y);
		}
		
		sr.end();
		
		Color nCol = Color.valueOf("ffe95b");
		nCol.a = 0.85f;
		sr.setColor(nCol);
		sr.begin(ShapeType.Filled);
		
		for(Particle p : this.particles) {
			sr.circle(p.pos.x, p.pos.y, p.size);;
		}
		
		sr.end();
	}
	
	public void update(float delta, float mouseX, float mouseY) {
		for(Particle p : this.particles)
			p.update(delta);
		
		Vector2 mouse = new Vector2(mouseX, mouseY);
		
		this.inRange.clear();
		
		for(Particle p : this.particles)
			if(p.pos.dst(mouse) < RANGE)
				this.inRange.add(p);
	}
	
	public class Particle{
		private float size;
		private Vector2 vel, pos;
		private boolean check = true;
		
		public Particle(float x, float y) {
			this.pos = new Vector2(x, y);
			
			this.size = Project.RAND.nextFloat() + 2;
			
			this.vel = new Vector2(Project.RAND.nextFloat() * 50 + 20, Project.RAND.nextFloat() * 30 - 15);
		}
		
		public void update(float delta) {
			this.pos.x += delta * vel.x;
			this.pos.y += delta * vel.y;
			
			if(this.pos.x > Project.SCREEN_WIDTH)
				this.pos.x = -5;
			
			if(check == false && this.pos.y > 0 && this.pos.y < Project.SCREEN_HEIGHT) {
				check = true;
			}
			if(this.pos.y < 0 && check) {
					this.pos.y = Project.SCREEN_HEIGHT + 5;
					check = false;
			    }
			else if(this.pos.y > Project.SCREEN_HEIGHT && check) {
					this.pos.y = -5;
					check = false;
				}
		}
	}
}
