package com.mygdx.game;

import com.badlogic.gdx.graphics.Texture;

public class Button {
	private float x, y, width, height;
	private Texture texture;
	
	
	public Button(float x, float y, float width, float height, Texture texture) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.texture = texture;
	}
	
	
	
	public boolean onPress(float mouseX, float mouseY) {
		mouseY = Project.SCREEN_HEIGHT - mouseY;
		return mouseX > this.x && mouseX < this.x + this.width && mouseY > this.y && mouseY < this.y + this.height;
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
}
