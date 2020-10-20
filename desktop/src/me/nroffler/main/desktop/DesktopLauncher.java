package me.nroffler.main.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

import me.nroffler.main.JumpAndRun;
import me.nroffler.main.Statics;

public class DesktopLauncher {

	public static void main (String[] arg) {


		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();

		config.width = Statics.WIDTH;
		config.height = Statics.HEIGHT;
		config.foregroundFPS = Statics.FRAMES_PER_SECOND;
		config.backgroundFPS = Statics.FRAMES_PER_SECOND;

		new LwjglApplication(new JumpAndRun(), config);
	}
}
