package com.thetriumvirate.spaceballz;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;

public class Button {
	private float x, y, width, height;
	private Texture texture;
	
	private boolean pressed;
	private Project game;
	
	public Button(float x, float y, float width, float height, Texture texture, Project game) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.texture = texture;
		
		this.pressed = false;
		
		this.game = game;
	}
	
	public boolean onPress(float mouseX, float mouseY, boolean down) {
		mouseY = Project.SCREEN_HEIGHT - mouseY;
		boolean ret = mouseX > this.x && mouseX < this.x + this.width && mouseY > this.y && mouseY < this.y + this.height;
		
		if(down && ret)
			game.assetmanager.get("audio/sounds/click.wav", Sound.class).play(0.1f);
		
		return ret;
	}

	public float getX() {
		return x;
	}

	public float getY() {
		return y;
	}

	public void setX(float x) {
		this.x = x;
	}

	public void setY(float y) {
		this.y = y;
	}

	public Texture getTexture() {
		return texture;
	}

	public float getWidth() {
		return width;
	}

	public float getHeight() {
		return height;
	}

	public boolean isPressed() {
		return pressed;
	}

	public void setPressed(boolean pressed) {
		this.pressed = pressed;
	}
}
