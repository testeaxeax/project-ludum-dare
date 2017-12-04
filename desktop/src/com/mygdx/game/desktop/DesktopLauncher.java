package com.mygdx.game.desktop;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.mygdx.game.Project;

public class DesktopLauncher {
	public static void main(String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.height = Project.SCREEN_HEIGHT;
		config.width = Project.SCREEN_WIDTH;
		config.resizable = false;
		config.title = "Spaceballz";
		config.addIcon("graphics/icon32.png", Files.FileType.Internal);
		config.addIcon("graphics/icon16.png", Files.FileType.Internal);
		new LwjglApplication(new Project(), config);
	}
}
