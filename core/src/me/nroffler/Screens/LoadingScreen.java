package me.nroffler.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;

import me.nroffler.main.JumpAndRun;

public class LoadingScreen implements Screen {

    private ShapeRenderer shapeRenderer;
    private JumpAndRun jumpAndRun;

    private float progress = 0;
    private int level;

    //da die Schwierigkeit übergeben wurde
    private Difficulty difficulty;
    public enum Difficulty{
        Normal,
        Hard
    }

    //das Level laden
    private TmxMapLoader mapLoader;
    private TiledMap map;

    public LoadingScreen(JumpAndRun jumpAndRun, int level, LoadingScreen.Difficulty difficulty) {
        this.jumpAndRun = jumpAndRun;
        this.level = level;
        this.difficulty = difficulty;

        shapeRenderer = new ShapeRenderer();

        //Level aus der XML laden
        mapLoader = new TmxMapLoader();
    }

    @Override
    public void show() { }


    private void update(float delta){
        if (progress == 0){
            //Level in den Grafikspeicher vorladen
            map = mapLoader.load("TMX/1-"+level+".tmx");
        }

        //Verzögerung
        progress += 0.7 * delta;

        //Spiel starten wenn:
        //
        //  -Zeit um ist
        //  -alle Assets geladen sind
        //  -die Map geladen wurde
        //
        //sonst:
        //
        //  -Fallback
        //  +alles neu Laden
        //
        if (progress > 1 && jumpAndRun.assetManager.update() && map != null) {
            if (difficulty == Difficulty.Normal){
                jumpAndRun.setScreen(new Playscreen(jumpAndRun, map ,level, Playscreen.Difficulty.Normal));
            } else if (difficulty == Difficulty.Hard){
                jumpAndRun.setScreen(new Playscreen(jumpAndRun, map ,level, Playscreen.Difficulty.Hard));
            }
        } else if (progress > 2){
            //Fallback bei einem Fehler, alles neu laden
            jumpAndRun.setScreen(new SplashScreen(jumpAndRun, SplashScreen.LoadState.CMD_ERROR_RESET));
        }
    }

    @Override
    public void render(float delta) {
        update(delta);

        Gdx.gl.glClearColor(0,0,0,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        //-----------------------------------------------------------------------------------------------------------
        //Ladebalken zeichnen
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        shapeRenderer.setColor(Color.DARK_GRAY);
        shapeRenderer.rect(10,10 ,Gdx.graphics.getWidth()/2, 10);

        if (progress <= 1) {
            shapeRenderer.setColor(Color.WHITE);
            shapeRenderer.rect(10, 10, Gdx.graphics.getWidth() / 2f * progress, 10);
        } else {
            shapeRenderer.setColor(Color.WHITE);
            shapeRenderer.rect(10, 10, Gdx.graphics.getWidth() / 2f, 10);
        }

        shapeRenderer.end();
        //-----------------------------------------------------------------------------------------------------------
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        //Grafikspeicher freigeben
        shapeRenderer.dispose();
    }
}
