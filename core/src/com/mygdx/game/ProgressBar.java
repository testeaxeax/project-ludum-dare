package com.mygdx.game;

import com.badlogic.gdx.graphics.Texture;

public class ProgressBar {

	
	private float posX, posY, width, height;
	
	private Texture border;
	private Texture infill;
	
	
	private float percentage = 0f;
	
	public ProgressBar(float width, float height, float posX, float posY, Texture border, Texture infill) {
		this.border = border;
		this.infill = infill;
		
		this.posX = posX;
		this.posY = posY;
		this.width = width;
		this.height = height;
	}
	
	public void update(Sun sun) {
		this.percentage = sun.getTemp() / Sun.MAX_TEMP;
	}
	
	public void setPercentage(float percentage) {
		this.percentage = percentage;
	}
	
	public float getPercentage() {
		return percentage;
	}
	
	public Texture getBorderTexture() {
		return border;
	}
	
	public Texture getInfillTexture() {
		return infill;
	}
	
	public float getPosX() {
		return posX;
	}

	public void setPosX(float posX) {
		this.posX = posX;
	}

	public float getPosY() {
		return posY;
	}

	public void setPosY(float posY) {
		this.posY = posY;
	}

	public float getWidth() {
		return width;
	}

	public void setWidth(float width) {
		this.width = width;
	}

	public float getHeight() {
		return height;
	}

	public void setHeight(float height) {
		this.height = height;
	}
}