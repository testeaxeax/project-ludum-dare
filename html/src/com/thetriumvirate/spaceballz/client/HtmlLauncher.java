package com.thetriumvirate.spaceballz.client;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.gwt.GwtApplication;
import com.badlogic.gdx.backends.gwt.GwtApplicationConfiguration;
import com.google.gwt.user.client.Window;
import com.thetriumvirate.spaceballz.Project;

public class HtmlLauncher extends GwtApplication {

        @Override
        public GwtApplicationConfiguration getConfig () {
                return new GwtApplicationConfiguration(Project.SCREEN_WIDTH, Project.SCREEN_HEIGHT);
        }

        @Override
        public ApplicationListener createApplicationListener () {
        	int height, width;
        	float ratio = 1024f / 800f;
        	if(Window.getClientHeight() < Window.getClientWidth()) {
        		height = (int) (Window.getClientHeight() * 0.95f);
        		width = (int) (ratio * height);
        	}else {
        		width = Window.getClientWidth();
        		height = (int) (width / ratio);
        	}
        	Project.SCREEN_WIDTH = width;
        	Project.SCREEN_HEIGHT = height;
                return new Project(new HtmlFontLoader());
        }
}