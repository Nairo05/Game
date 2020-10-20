package me.nroffler.main;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import me.nroffler.Screens.SplashScreen;

//Hauptklasse des Spiels
public class JumpAndRun extends Game {

	//Diese Objekte werden im ganzen Spiel benötigt und sollten nur einmal pro Spiel existieren, da sie sehr viel Speicher brauchen. (VRAM und RAM)

	//Zieht sehr viel Speicher, da hiermit alle Bilder, … vorgeladen werden.
	public AssetManager assetManager;
	//Hiermit werden Leben, Level, ... permanent gespeichert.
	public PrefManager prefManager;
	public LevelStarProgressSafer levelStarProgressSafer;

	//Ein SpriteBatch fasst Bilder zusammen um sie dann zusammenhängend zeichnen zu können und ist damit sehr VRAM lastig.
	public SpriteBatch batch;

	@Override
	public void create () {

		//Assetmanager (LibGDX Klasse)
		assetManager = new AssetManager();

		//PrefManager (eigene Klassen)
		prefManager = new PrefManager();
		levelStarProgressSafer = new LevelStarProgressSafer();

		//Spritebatch
		batch = new SpriteBatch();

		//Die Fähigkeit das Bild zu zeichnen an einen Screen abgeben
		setScreen(new SplashScreen(this, SplashScreen.LoadState.CMD_LOAD_ASSETS));
	}

	@Override
	public void render () {
		//Mit dem Aufruf von super (Oberklasse) wird render in dem jeweiligen Screen ausheführt, nicht hier.
		super.render();
	}
	
	@Override
	public void dispose () {
		//Grafikspeicher freigeben
		assetManager.dispose();
		batch.dispose();
	}
}
