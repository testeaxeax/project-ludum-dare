package com.thetriumvirate.spaceballz.client;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.thetriumvirate.spaceballz.FontLoader;
import com.thetriumvirate.spaceballz.Project;

public class HtmlFontLoader implements FontLoader {
	
	private Project game = null;

	@Override
	public BitmapFont load(String assetpath, int font_size, Color color) {
		return load(assetpath);
	}

	@Override
	public BitmapFont load(String assetpath, int font_size) {
		float scaleXY;
		assetpath = "html/" + assetpath + ".fnt";
		BitmapFont font = game.assetmanager.easyget(assetpath, BitmapFont.class);
		scaleXY = font_size / font.getLineHeight();
		font.getData().setScale(scaleXY);
		return font;
	}

	@Override
	public BitmapFont load(String assetpath) {
		return load(assetpath, Project.DEFAULT_FONTSIZE);
	}

	@Override
	public BitmapFont load() {
		return load(Project.DEFAULT_FONT_ASSET_PATH);
	}

	@Override
	public void setGame(Project p) {
		game = p;
	}

}
