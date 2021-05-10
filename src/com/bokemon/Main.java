package com.bokemon;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class Main {
	public static void main(String[] args) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		
		config.title = "Bokemon";
		config.height = 1440;
		config.width = 2160;
		config.vSyncEnabled = true;
		
		new LwjglApplication(new Bokemon(), config);
	}
}
