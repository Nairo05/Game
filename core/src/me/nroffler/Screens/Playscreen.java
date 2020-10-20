package me.nroffler.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.ArrayList;

import me.nroffler.B2World.Coin;
import me.nroffler.B2World.MapCreator;
import me.nroffler.B2World.MovingTiledObject;
import me.nroffler.B2World.MyContactListener;
import me.nroffler.Enemys.Enemy;
import me.nroffler.Scene.Hud;
import me.nroffler.Scene.SceneSplash;
import me.nroffler.Sprites.Player;
import me.nroffler.main.JumpAndRun;
import me.nroffler.main.PrefManager;
import me.nroffler.main.Statics;
import me.nroffler.spawnable.Coins;
import me.nroffler.spawnable.DynamicCoin;
import me.nroffler.spawnable.Life;
import me.nroffler.spawnable.SpawnAble;
import me.nroffler.spawnable.ToSpawn;

//da ich diese Variable sehr oft benutze habe ich mich dazu entschieden sie zu importieren
import static me.nroffler.main.Statics.PPM;

//Hauptklasse des Spiels, hier läuft das eigentliche Spiel ab und hier "kommt alles zusammen"
public class Playscreen implements Screen {

    //Hauptklasse
    private JumpAndRun jumpAndRun;

    //damit man weiß, warum man Tod ist, gelangt man nicht direkt in den GameOverScreen sondern mit einer Verzögerung
    private int deathTimer;
    //derzeitiges Level
    private int level;
    //Debug, Hitboxen werden mitgezeichnet wenn auf true
    private boolean debug;

    //LibGDX Kamera und Viewport
    private OrthographicCamera gamecam;
    private Viewport gameport;

    //Tiled (XML)
    private TiledMap map;
    private OrthogonalTiledMapRenderer renderer; //rendert die in der XML-Datei gespeicherten Texturen

    //Box2D
    private World world;
    private Box2DDebugRenderer b2dr; //zeichnet Hitboxen

    //eigene Klassen
    private Hud hud; //Lebensanzeige, Zeit und Buttons für Android
    private SceneSplash sceneSplash; //schwarzes Bild mit "Level n" [wenn das Level gestartet wird]
    private Player player; //Der Spieler [also der Dino]
    private MapCreator mapCreator;  //Hier wird die XML-Datei in Box2D übersetzt
    private MyContactListener myContactListener; //Kollisionsabrage mit Hilfe von Bits (BitFilter.java)

    //in welchem Zustand bzw. in welcher Phase ist das Spiel gerade
    public State gamestat;
    public enum State{
        ALIVE_RUNNING,
        DEATH_NO_LIVES,
        DEATH_FALLEN,
        LEVEL_FINISHED,
        SPLASHING
    }

    //Schwierigkeit des Levels, betrifft Zeit und Anzahl an Gegnern
    private Difficulty difficulty;
    public enum Difficulty{
        Normal,
        Hard
    }

    //für die Warteschlange um "SpawnAble" während dem Spiel erschaffen zu können
    private ArrayList<SpawnAble> spawnAbles;
    private ArrayList<ToSpawn> tospawn;

    public Playscreen(JumpAndRun jumpAndRun, TiledMap tiledMap, int level, Difficulty difficulty){
        //übergebene Werte
        this.jumpAndRun = jumpAndRun;
        this.level = level;
        this.difficulty = difficulty;
        this.map = tiledMap;

        //Neue Kamera erstellen [Für das eigentliche Spiel, nicht für das HUD]
        gamecam = new OrthographicCamera();
        //Neuen Viewport erstellen [Für das eigentliche Spiel, nicht für das HUD], virtuelle Größe übergeben, sowie die Kamera
        gameport = new ExtendViewport(Statics.V_WIDTH / PPM, Statics.V_HEIGHT / PPM, gamecam);

        //Zeichnet später die Texturen, die in der XML-Datei gespeichert sind [nur statische !!!]
        renderer = new OrthogonalTiledMapRenderer(map, 1 / PPM);

        //Kamera Position setzen (entspricht Position des Spielers)
        gamecam.position.set(gameport.getWorldWidth() /2 ,gameport.getWorldHeight() / 2, 0);

        //Welt mit einer Schwerkraft von 9.8G, es dürfen Objekte in Ruhe versetzt werden
        world = new World(new Vector2(0,-9.8f), true);
        //DebugRenderer um Hitboxen zu zeichnen, wenn (private boolean debug) auf true
        b2dr = new Box2DDebugRenderer();

        //Objekte (eigene Klassen) initialisieren
        player = new Player(this);
        hud = new Hud(this, jumpAndRun.batch, difficulty.ordinal());
        mapCreator = new MapCreator(this);

        //Eigenen ContactListener setzen und an Welt binden
        myContactListener = new MyContactListener(this);
        world.setContactListener(myContactListener);

        //Status des Spiels ist der SplashScreen mit "Level n", Splashscreen starten
        gamestat = State.SPLASHING;
        sceneSplash = new SceneSplash(this);

        //Damit nach dem Tod wie oben beschrieben gewartet wird
        deathTimer = 0;

        //für die Warteschlange um "SpawnAble" während dem Spiel erschaffen zu können
        spawnAbles = new ArrayList<SpawnAble>();
        tospawn = new ArrayList<ToSpawn>();

        //Debug auf true, Hitboxen werden gezeichnet
        debug = true;
    }

    //Wird z.B im ContactListener aufgerufe, hiermit wird ein neues Objekt eingereiht in die Warteschlange
    public void spawn(ToSpawn toSpawn){
        tospawn.add(toSpawn);
    }

    //in der Warteschlange heraus Objekte erschaffen
    public void handleSpawning(){
        //nur wenn etwas eingereiht ist
        if (!tospawn.isEmpty()){
            ToSpawn toSpawn = tospawn.get(tospawn.size()-1);
            //verschiedene Objekte erschaffen, abhänhig von der Klasse (zwischengespeichert in toSpawn, also in der Warteschlange)
            if (toSpawn.type == Coins.class){
                spawnAbles.add(new Coins(Playscreen.this, toSpawn.position.x, toSpawn.position.y, toSpawn.item_meta));
                tospawn.remove(tospawn.size()-1);
            } else if (toSpawn.type == Life.class){
                spawnAbles.add(new Life(Playscreen.this, toSpawn.position.x, toSpawn.position.y, toSpawn.item_meta));
                tospawn.remove(tospawn.size()-1);
            } else if (toSpawn.type == DynamicCoin.class){
                spawnAbles.add(new DynamicCoin(Playscreen.this, toSpawn.position.x, toSpawn.position.y, toSpawn.item_meta));
                tospawn.remove(tospawn.size()-1);
            }
        }
    }

    @Override
    public void show() { }

    //Input verarbeiten
    private void handleInput(float dt){

        //für Desktop und HTML
        if ((Gdx.input.isKeyJustPressed(Input.Keys.W))){
            //nur wenn der Spieler den Boden berührt
            if (myContactListener.PlayerIsOnGorund()) {
                //das Springen ist ausgelagert, da dies ein komplexerer Mechanismus ist, als lediglich eine Kraft anzulegen
                player.jump();
            }
        }
        if (((Gdx.input.isKeyPressed(Input.Keys.D) && player.b2body.getLinearVelocity().x <= 1.5f))){
                player.b2body.applyLinearImpulse(new Vector2(0.1f, 0), player.b2body.getWorldCenter(), true);
        }
        if ((Gdx.input.isKeyPressed(Input.Keys.A) && player.b2body.getLinearVelocity().x >= -1.5f)){
            player.b2body.applyLinearImpulse(new Vector2(-0.1f, 0), player.b2body.getWorldCenter(), true);
        }

        //für Android und iOS
        for (int i = 0 ; i < 3; i++){
            //Bis zu 3 Berührungen gleichzeitig pro Bild (60 pro Sekunde)
            if (Gdx.input.isTouched(i)){
                //abhängig, wo man berührt
                if (Gdx.input.getX(i) > 0 && Gdx.input.getX(i) < gameport.getScreenWidth() / 7 && player.b2body.getLinearVelocity().x >= -1.4f) {
                    player.b2body.applyLinearImpulse(new Vector2(-0.1f, 0), player.b2body.getWorldCenter(), true);
                }else if (Gdx.input.getX(i) > gameport.getScreenWidth() / 7 && Gdx.input.getX(i) < gameport.getScreenWidth() / 2 && player.b2body.getLinearVelocity().x <= 1.4f) {
                    player.b2body.applyLinearImpulse(new Vector2(0.1f, 0), player.b2body.getWorldCenter(), true);
                } else if (Gdx.input.getX(i) > gameport.getScreenWidth() / 2){
                    //nur wenn der Spieler den Boden berührt
                    if (myContactListener.PlayerIsOnGorund()) {
                        //das Springen ist ausgelagert, da dies ein komplexerer Mechanismus ist, als lediglich eine Kraft anzulegen
                        player.jump();
                    }
                }
            }
        }
    }

    //Schritt 1 und 2 des 3 schrittigen Systems, sowie Vorbereitung für Schritt 3  //TODO Backup with mistake https://pastebin.com/DKuB5Ne7
    private void update(float dt){
        //------------------------------------------------------------------------------------- Schritt 1 ---------------------------------------------
        // Neue Objekte erschaffen
        handleSpawning();

        //Betshende Objekte updaten
        for (SpawnAble spawnAble : spawnAbles){
            spawnAble.update(dt);
        }

        //wenn man Tod ist, (20/60) 1/3 Sekunde warten, dann in den GameOverScreen wechseln
        if (gamestat == State.DEATH_FALLEN || gamestat == State.DEATH_NO_LIVES){
            deathTimer++;
            if (deathTimer > 20) {
                jumpAndRun.setScreen(new GameOverScreen(jumpAndRun, gamestat.ordinal(), player.getGems(), 0, level)); //TODO
            }

        //wenn das Spiel gerade gestartet wurde wird noch der SplashScreen mit "Level n" geupdatet
        } else if (gamestat == State.SPLASHING){
            sceneSplash.update(dt);

        //wenn das Level abgeschlossen wurde nach rechts weiter laufen, das freigeschlatete Level speichern und in den GameOverScreen wechseln
        } else if (gamestat == State.LEVEL_FINISHED){
            deathTimer++;
            if (player.b2body.getLinearVelocity().x <= 1.4f) {
                player.b2body.applyLinearImpulse(new Vector2(0.1f, 0), player.b2body.getWorldCenter(), true); //Spieler läuft weiter nach rechts
            }
            //(sobald man nach rechts aus dem Bild raus gelaufen ist (120 = 2s))
            if (deathTimer > 120) {
                //wird das freigeschaltete Level gespeichert
                if (level == jumpAndRun.prefManager.getInteger(PrefManager.PREF_UNLOCKLEVEL, 1)){
                    jumpAndRun.prefManager.saveIntegerValue(PrefManager.PREF_UNLOCKLEVEL,
                            (jumpAndRun.prefManager.getInteger(PrefManager.PREF_UNLOCKLEVEL, 1)+1));

                }
                //und der Screen wird zum GameOverScreen geändert, übergeben wird als Parameter eine 1, die für "Level geschafft" im GameOverScreen steht
                jumpAndRun.setScreen(new GameOverScreen(jumpAndRun, gamestat.ordinal(), player.getGems(), 1, level)); //TODO
            }
        } else {
            //Sonst wenn das Spiel läuft (ALIVE_RUNNING) den Input bearbeiten
            handleInput(dt);
        }

        //Kamera dem Spieler in x-Richtung hinterher bewegen, y-Richtung bleibt gleich
        //aber nur solange die Kamera noch komplett die Map zeigt (die Map ist <41f lang).
        if (player.b2body.getPosition().x < 39f && player.b2body.getPosition().x > 2.32f) {
            gamecam.position.x = player.b2body.getPosition().x;
            gamecam.position.y = gameport.getWorldHeight()/2;
        }

        //Spieler updaten
        player.update();

        //alle Gegner wenn nötig aufwecken und updaten
        for (Enemy enemy : mapCreator.getEnemys()){
            //Gegner updatens
            enemy.update(dt);
            //wenn der Spieler den Gegner sehen kann
            if (player.getX()+3f > enemy.getX() && player.getX() < enemy.getX()+3f) {
                //und der Gegner noch nicht zerstört ist (Sicherheitsabfrage)
                if (!enemy.getdestroyed()){
                    //und der Gegner noch nicht bereuts aktiv ist
                    if (!enemy.b2body.isActive()) {
                        //--> wird der Gegner aufgeweckt
                        enemy.b2body.setActive(true);
                    }
                }
            }
            //wenn der Spieler den Gegner nicht mehr sieht
            if (player.getX() > enemy.getX()+3f){
                //und der Gegner noch aktiv ist
                if (enemy.b2body.isActive()){
                    //wird er angehalten
                    enemy.b2body.setActive(false);
                }
            }
        }

        //alle Coins wenn nötig aufwecken und updaten
        for (Coin coin : mapCreator.getCoins()){
            //coin updaten
            coin.update(dt);
            //wenn der Spieler den Coin sehen kann
            if (player.getX()+3f > coin.getX() && player.getX() < coin.getX()+3f){
                //und der Coin nicht bereits aktiv ist
                if (!coin.b2body.isActive()){
                    //wird der Coin aufgeweckt
                    coin.b2body.setActive(true);
                }
            }
            //wenn der Spieler den Coin nicht mehr sieht
            if (player.getX() > coin.getX()+3f){
                //und der Coin aber noch aktiv ist
                if (coin.b2body.isActive()){
                    //wird er angehalten
                    coin.b2body.setActive(false);
                }
            }
        }

        //alle sich bewegenden Objekte updaten
        //da sich Plattformen nicht mit konstanter Geschwindigkeit bewegen, kann es passieren das durch das Anhalten die Plattform "verutschen" kann wird diese nicht in Ruhe versetzt
        for (MovingTiledObject movingTiledObject : mapCreator.getMovingBlocks()){
            movingTiledObject.update(dt);
        }

        //------------------------------------------------------------------------------------- Schritt 2 ---------------------------------------------


        //Physik berechnen
        world.step(1 / 60f, 6,2);


        //-------------------------------------------------------- Sontige Updates (Schritt 2-2.5) ---------------------------------------------
        //Vorbereitung für Schritt 3
        //Hud updaten, hat keine Auswirkung auf Physik, deswegen nach Physikberechnung
        hud.update(dt);

        if (gamestat == State.LEVEL_FINISHED){
            hud.finish();
        }

        //Kamera bewegen, rendern der Karte vorbereiten, hat keine Auswirkung auf Physik, deswegen nach Physikberechnung
        gamecam.update();
        renderer.setView(gamecam);

    }

    @Override
    public void render(float delta) {
        //Schritt 1 und 2 ausführen
        update(delta);

        //------------------------------------------------------------------------------------- Schritt 3 ---------------------------------------------
        //Bild "leeren"
        Gdx.gl.glClearColor(0,0,0,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        //Schichten festlegen der XML
        //Vordergrund
        int[] flayers = new int[1];
        flayers[0] = 1;
        //Hintergrund
        int[] blayers = new int[1];
        blayers[0] = 0;

        //Wenn der SplashScreen mit "Level n" (Scene) fertig ist
        if (!(gamestat == State.SPLASHING)) {

            //Hintergrund zeichnen
            renderer.render(blayers);

            //Kamera an Batch binden, um alle Grafiken innerhalb des Sichtfeldes zusammenhängend zeichnen zu können
            jumpAndRun.batch.setProjectionMatrix(gamecam.combined);
            //Grafikzeichnen anfangen
            jumpAndRun.batch.begin();

            //alle sich bewegenden Objekte zeichnen
            for (MovingTiledObject movingTiledObject : mapCreator.getMovingBlocks()){
                movingTiledObject.draw(jumpAndRun.batch);
            }

            //Grafikzeichnen vorerst beenden
            jumpAndRun.batch.end();

            //Vordergrund zeichnen
            //darf nicht während dem Grafikzeichnen gezeichnet werden
            renderer.render(flayers);

            //Grafikzeichnen wieder aufnehmen
            jumpAndRun.batch.begin();

            //Alle während dem Spiel erschaffenen Objekte zeichnen
            for (SpawnAble spawnAble : spawnAbles){
                spawnAble.draw(jumpAndRun.batch);
            }

            //den Spieler zeichnen
            player.draw(jumpAndRun.batch);

            //alle Gegner zeichnen
            for (Enemy enemy : mapCreator.getEnemys()){
                //nur zeichnen, wenn der Spieler den Gegner sieht, bzw. wenn er aktiv ist
                if (enemy.b2body.isActive()) {
                    enemy.draw(jumpAndRun.batch);
                }
            }

            //alle Coins (aus der XML-Datei ausgelsene, nicht während dem Spiel erschaffene) zeichnen
            for (Coin coin : mapCreator.getCoins()){
                //nur zeichnen wenn der Spieler den Coin sieht, bzw. er aktiv ist
                if (coin.b2body.isActive()) {
                    coin.draw(jumpAndRun.batch);
                }
            }

        //sonst den SplashScreen (Scene) zeichnen
        } else {

            jumpAndRun.batch.setProjectionMatrix(gamecam.combined);
            jumpAndRun.batch.begin();

            sceneSplash.render(jumpAndRun.batch);
        }

        //in jedem Fall das Grafikzichnen wieder beenden
        jumpAndRun.batch.end();

        //neue Kamera binden, da das HUD sich nicht mitbewegen soll
        jumpAndRun.batch.setProjectionMatrix(hud.stage.getCamera().combined);
        hud.stage.draw();

        //Hitboxen zeichnen, wenn debug == true
        if (debug) {
            b2dr.render(world, gamecam.combined);
        }

    }

    //um den Status des Spiels ändern zu können
    public void setRunning(State state){
        gamestat = state;
    }

    @Override
    public void resize(int width, int height) {
        //wenn sich das Fenster ändert, Bild skalieren (Desktop)
        gameport.update(width, height);
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
        //Grafikspeier freigeben
        world.dispose();
        map.dispose();
        renderer.dispose();
        b2dr.dispose();
        sceneSplash.dispose();
        hud.dispose();
        player.dispose();

        for (Enemy enemy : mapCreator.getEnemys()){
            enemy.dispose();
        }

        for (SpawnAble spawnAble : spawnAbles){
            spawnAble.dispose();
        }
    }

    //Getter
    public World getWorld() {
        return world;
    }

    public TiledMap getMap() {
        return map;
    }

    public Player getPlayer() {
        return player;
    }

    public AssetManager getAssetManager(){
        return jumpAndRun.assetManager;
    }

    public SpriteBatch getBatch(){
        return jumpAndRun.batch;
    }

    public int getLevel(){
        return level;
    }

    public PrefManager getprefManager(){return jumpAndRun.prefManager;}
}

