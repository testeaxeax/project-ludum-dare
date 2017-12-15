package com.thetriumvirate.spaceballz;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

public interface FontLoader {

	public BitmapFont load(String assetpath, int font_size, Color color);
	public BitmapFont load(String assetpath, int font_size);
	public BitmapFont load(String assetpath);
	public BitmapFont load();
	public void setGame(Project p);
}
