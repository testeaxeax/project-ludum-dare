package com.thetriumvirate.spaceballz;

import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGeneratorLoader;
import com.badlogic.gdx.graphics.g2d.freetype.FreetypeFontLoader;
import com.badlogic.gdx.graphics.g2d.freetype.FreetypeFontLoader.FreeTypeFontLoaderParameter;

public final class DesktopFontLoader implements FontLoader {
	
	private Project game = null;

	@Override
	public BitmapFont load(String assetpath, int font_size, Color color) {
		FreeTypeFontLoaderParameter loaderparam = new FreeTypeFontLoaderParameter();
		FreeTypeFontParameter param = new FreeTypeFontParameter();
		assetpath = "desktop/" + assetpath + ".ttf";
		
		FileHandleResolver resolver = new InternalFileHandleResolver();
		game.assetmanager.setLoader(FreeTypeFontGenerator.class, new FreeTypeFontGeneratorLoader(resolver));
		game.assetmanager.setLoader(BitmapFont.class, ".ttf", new FreetypeFontLoader(resolver));
		param.size = font_size;
		param.color = color;
		loaderparam.fontFileName = assetpath;
		loaderparam.fontParameters = param;
		return game.assetmanager.easyget(assetpath, BitmapFont.class, loaderparam);
	}
	
	@Override
	public BitmapFont load(String assetpath, int font_size) {
		return load(assetpath, font_size, Project.DEFAULT_FONT_COLOR);
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
