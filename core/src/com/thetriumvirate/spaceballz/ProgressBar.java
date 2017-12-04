package com.thetriumvirate.spaceballz;

import com.badlogic.gdx.graphics.Texture;

public class ProgressBar {

	
	private float posX, posY, width, height;
	
	private Texture border;
	private Texture infill;
	
	
	private float percentage = 0f;
	private float animationProgress = 0f;
	
	public float getAnimationProgress(float delta) {
		float speed = 0.5f;
		
		if(this.animationProgress < this.percentage) {
			this.animationProgress += speed * delta;
			if(this.animationProgress > this.percentage)
				this.animationProgress = this.percentage;
		} else if(this.animationProgress > this.percentage) {
			this.animationProgress -= speed * delta;
			
			if(this.animationProgress < this.percentage)
				this.animationProgress = this.percentage;
		}
		return animationProgress;
	}

	public void setAnimationProgress(float animationProgress) {
		this.animationProgress = animationProgress;
	}

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
		if(percentage > 1)percentage = 1;
	}
	
	public void setPercentage(float percentage) {
		if(percentage > 1)percentage = 1;
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

	public float getAnimationProgress() {
		return this.animationProgress;
	}
}
