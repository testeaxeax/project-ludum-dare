package com.thetriumvirate.spaceballz;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;

public class PlanetManager {
	/* PLANET SETUP
	 * produces an Array of Planets that is randomly distributed on the Screen
	 */
	
	public static ArrayList<Planet> setupPlanets(int count, GameScreen screen) {
		Texture[] textures = screen.getPlanetTextures();
		
		ArrayList<Planet> planets;
		
		// Planet init, creates -count- planets
		planets  = new ArrayList<Planet>();
		do {
			planets.clear();
			for(int i = 0; i < count; i++) 
				planets.add(new Planet(screen, planets, textures[i % 7]));
		} while (!checkPlanets(planets, screen));
		
		return planets;
	}
	
	private static boolean checkPlanets(ArrayList<Planet> planets, GameScreen screen) {
		float x = 0f;
		float y = 0f;
		
		for(Planet p : planets) {
			x += p.getPos().x;
			y += p.getPos().y;
		}
		
		// Get the average position to prevent the planets from only being on one side of the screen
		
		x /= planets.size();
		y /= planets.size();
		
		Vector2 delta = new Vector2(x, y);
		
		if(screen.sun.getOriginPos().dst(delta) > 80)
			return false;
		
		return true;
	}
}
