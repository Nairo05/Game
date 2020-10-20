package me.nroffler.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import me.nroffler.main.JumpAndRun;
import me.nroffler.main.PrefManager;
import me.nroffler.main.Statics;

import static me.nroffler.Screens.SplashScreen.LoadState.CMD_RESETT_DATA;

public class SplashScreen implements Screen {

    private JumpAndRun jumpAndRun;
    private Viewport viewport;
    private OrthographicCamera camera;
    private Texture splash01, splash02, splash03;
    private Sprite sprite, spriten, spriteIntro;
    private ShapeRenderer shapeRenderer;
    private float loadingbar;
    private boolean loadAssets;

    private int progress;

    //was soll gemacht werden in dieser Ladephase ? Nur Assets laden oder Daten resetten oder .....
    public LoadState gamestat;
    public enum LoadState{
        CMD_RESETT_DATA,
        CMD_LOAD_ASSETS,
        CMD_DEBUG_RESET,
        CMD_OVERWRITE_I,
        CMD_ERROR_RESET
    }

    public SplashScreen(JumpAndRun jumpAndRun, LoadState gamestate) {
        this.jumpAndRun = jumpAndRun;
        this.gamestat = gamestate;

        //Formenzeichner (für Ladebalken)
        shapeRenderer = new ShapeRenderer();

        //Zähler
        progress = 0;
        //die Breite des Ladebalken
        loadingbar = 0;
        loadAssets = false;

        //die drei Logos als Textur
        splash01 = new Texture("Splash/splash01.png");
        splash02 = new Texture("Splash/splash02.png");
        splash03 = new Texture("Splash/splash03.png");

        //----------------------------------------------------------------------------
        //aus den drei Logos Sprites machen, da nur diese einen Alpha-Kanal besitzen
        sprite = new Sprite(splash01);
        sprite.setBounds(0, 0, 400f, 208f);

        spriten = new Sprite(splash02);
        spriten.setBounds(0, 0, 400f, 208f);

        spriteIntro = new Sprite(splash03);
        spriteIntro.setBounds(-3, 0, 400f, 208f);
        //----------------------------------------------------------------------------

        //neue Kamera
        camera = new OrthographicCamera();

        //neuerViewport
        viewport = new FitViewport(Statics.V_WIDTH, Statics.V_HEIGHT, camera);

        //Kamera ausrichten
        camera.position.set(viewport.getWorldWidth() / 2, viewport.getWorldHeight() /2, 0);

        //----------------------------------------------------------------------------
        //was soll gemacht werden in dieser Ladephase ? Nur Assets laden oder Daten resetten oder .....
        if (gamestate == LoadState.CMD_LOAD_ASSETS) {
            jumpAndRun.assetManager.clear();
            loadAssets = true;
        } else if (gamestate == CMD_RESETT_DATA){
            jumpAndRun.prefManager.resetData();
            jumpAndRun.levelStarProgressSafer.resetData();
            flush();
        } else if (gamestate == LoadState.CMD_DEBUG_RESET){
            jumpAndRun.assetManager.clear();
        } else if (gamestate == LoadState.CMD_OVERWRITE_I){
            Statics.lifes = 2;
            Statics.unlock_level = 1;
        } else if (gamestate == LoadState.CMD_ERROR_RESET){
            jumpAndRun.assetManager.clear();
            loadAssets = true;
            flush();
        }
        //----------------------------------------------------------------------------
    }

    private void queueLoading(){
        //Werte aus Textdatei auslesen. Diese Werte werden im ganzen Spiel gebraucht und werden deswegen vorgeladen
        Statics.lifes = jumpAndRun.prefManager.getInteger(PrefManager.PREF_LIFE_TOTAL, 2);
        Statics.unlock_level = jumpAndRun.prefManager.getInteger(PrefManager.PREF_UNLOCKLEVEL, 1);
        Statics.coins = jumpAndRun.prefManager.getInteger(PrefManager.PREF_COINS_TOTAL, 0);

        Statics.hud = jumpAndRun.prefManager.getBoolean(PrefManager.PREF_HUD_ENABLED);
        Statics.effects = jumpAndRun.prefManager.getBoolean(PrefManager.PREF_EFFEKT_ENABLED);
        Statics.musik = jumpAndRun.prefManager.getBoolean(PrefManager.PREF_MUSIC_ENABLED);
        Statics.sounds = jumpAndRun.prefManager.getBoolean(PrefManager.PREF_SOUND_ENABLED);
    }

    private void queueAssets1(){
        //Bilder vorladen

        //Sprite
        jumpAndRun.assetManager.load("Sprite/enemies-spritesheet.png", Texture.class);
        jumpAndRun.assetManager.load("Sprite/spr_coin_strip4.png", Texture.class);
        jumpAndRun.assetManager.load("Sprite/dino_1.png", Texture.class);
        jumpAndRun.assetManager.load("Sprite/dino_2.png", Texture.class);
        jumpAndRun.assetManager.load("Sprite/dino_3.png", Texture.class);
        jumpAndRun.assetManager.load("Sprite/dragon_hit.png", Texture.class);
        jumpAndRun.assetManager.load("Sprite/moving1.png", Texture.class);
        jumpAndRun.assetManager.load("Sprite/moving2.png", Texture.class);
        jumpAndRun.assetManager.load("Sprite/movinglong.png", Texture.class);
        jumpAndRun.assetManager.load("Sprite/slimehit.png", Texture.class);
        jumpAndRun.assetManager.load("Sprite/rolling1.png", Texture.class);
        jumpAndRun.assetManager.load("Sprite/spr_heart.png", Texture.class);
        jumpAndRun.assetManager.load("Sprite/hearts16x16.png", Texture.class);
        jumpAndRun.assetManager.load("Sprite/green_death.png", Texture.class);
        jumpAndRun.assetManager.load("Sprite/blue_death.png", Texture.class);

        //Splash (heavy)
        jumpAndRun.assetManager.load("Splash/1.png", Texture.class);
        jumpAndRun.assetManager.load("Splash/2.png", Texture.class);
        jumpAndRun.assetManager.load("Splash/3.png", Texture.class);
        jumpAndRun.assetManager.load("Splash/4.png", Texture.class);
        jumpAndRun.assetManager.load("Splash/5.png", Texture.class);
        jumpAndRun.assetManager.load("Splash/6.png", Texture.class);
        jumpAndRun.assetManager.load("Splash/7.png", Texture.class);
        jumpAndRun.assetManager.load("Splash/8.png", Texture.class);
        jumpAndRun.assetManager.load("Splash/9.png", Texture.class);
        jumpAndRun.assetManager.load("Splash/10.png", Texture.class);
        jumpAndRun.assetManager.load("Splash/11.png", Texture.class);
        jumpAndRun.assetManager.load("Splash/12.png", Texture.class);
    }

    private void queueAssets2(){

        //UI
        jumpAndRun.assetManager.load("UI/BTN_PLAIN5.png", Texture.class);
        jumpAndRun.assetManager.load("UI/BTN_PLAIN6.png", Texture.class);
        jumpAndRun.assetManager.load("Backgrounds/bg0.png", Texture.class);
        jumpAndRun.assetManager.load("UI/SYMB_RR.png", Texture.class);
        jumpAndRun.assetManager.load("UI/SYMB_FF.png", Texture.class);
        jumpAndRun.assetManager.load("UI/UI_STAR_EMTPY.png", Texture.class);
        jumpAndRun.assetManager.load("UI/UI_STAR_NORMAL.png", Texture.class);
        jumpAndRun.assetManager.load("UI/UI_LOCK.png", Texture.class);
        jumpAndRun.assetManager.load("UI/UI_BTN_RESET.png", Texture.class);
        jumpAndRun.assetManager.load("UI/BTN_GREEN_CIRCLE_OUT.png", Texture.class);
        jumpAndRun.assetManager.load("UI/BTN_GREEN_CIRCLE_IN.png", Texture.class);
        jumpAndRun.assetManager.load("UI/uibg.png", Texture.class);
        jumpAndRun.assetManager.load("UI/BTN_PLAY.png", Texture.class);
        jumpAndRun.assetManager.load("UI/BTN_CHECKBOX_OUT.png", Texture.class);
        jumpAndRun.assetManager.load("UI/BTN_CHECKBOX_IN.png", Texture.class);

        //FONT (medium)
        jumpAndRun.assetManager.load("Font/number_font.png", Texture.class);
        jumpAndRun.assetManager.load("Font/welt1.png", Texture.class);
        jumpAndRun.assetManager.load("Font/welt2.png", Texture.class);
        jumpAndRun.assetManager.load("Font/welt3.png", Texture.class);
        jumpAndRun.assetManager.load("Font/mainmenu.png", Texture.class);
        jumpAndRun.assetManager.load("Font/mainmenu1.png", Texture.class);

        //BACKGROUNDS (heavy)
        jumpAndRun.assetManager.load("Backgrounds/bg0.png", Texture.class);
        jumpAndRun.assetManager.load("Backgrounds/bg1.png", Texture.class);
        jumpAndRun.assetManager.load("Backgrounds/bg2.png", Texture.class);
        jumpAndRun.assetManager.load("Backgrounds/bg3.png", Texture.class);
    }

    private void finishAssets(){
        //finish
        jumpAndRun.assetManager.finishLoading();
        jumpAndRun.assetManager.update();
    }

    private void flush(){
        //Zur Sicherheit nochmal Preferenzen leeren (durch schreiben)
        jumpAndRun.prefManager.writeData();
    }

    @Override
    public void show() { }

    private void update(float dt){
        progress += 1;
        loadingbar += 0.17 * dt;

        camera.update();

        //Wenn der Ladebalken durchgelaufen ist UND alle Assets geladen wurden
        if (progress >= 360){
            if (jumpAndRun.assetManager.isFinished()) {
                //Am Ende ins Hauptmenü gehen
                jumpAndRun.setScreen(new MainMenu(jumpAndRun));
            }
        }
    }

    @Override
    public void render(float delta) {
        update(delta);

        //Alles schwarz übermalen
        Gdx.gl.glClearColor(0f, 0f, 0f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        jumpAndRun.batch.enableBlending();

        //verzögerung einbauen, sonst braucht das Spiel lange zum öffnen
        if (loadAssets && progress == 40){
            queueAssets1();
            queueLoading();
            flush();
            //verzögerung einbauen, sonst hängt das Spiel
        } else if (loadAssets && progress == 140){
            //Assets laden
            queueAssets2();
        } else if (loadAssets && progress == 320){
            //Assets laden abschließen
            finishAssets();
        }

        //Logos einblenden und wieder ausblenden
        if (progress == 89){
            sprite.setAlpha(0.9f);
        } else if (progress == 92){
            sprite.setAlpha(0.8f);
        } else if (progress == 95){
            sprite.setAlpha(0.7f);
        } else if (progress == 98){
            sprite.setAlpha(0.6f);
        } else if (progress == 101){
            sprite.setAlpha(0.5f);
        } else if (progress == 103){
            sprite.setAlpha(0.4f);
        } else if (progress == 106){
            sprite.setAlpha(0.3f);
        } else if (progress == 109){
            sprite.setAlpha(0.2f);
        } else if (progress == 112){
            sprite.setAlpha(0.1f);
        } else if (progress == 115){
            sprite.setAlpha(0.0f);


        } else if (progress == 209){
            spriten.setAlpha(0.9f);
        } else if (progress == 212){
            spriten.setAlpha(0.8f);
        } else if (progress == 215){
            spriten.setAlpha(0.7f);
        } else if (progress == 218){
            spriten.setAlpha(0.6f);
        } else if (progress == 221){
            spriten.setAlpha(0.5f);
        } else if (progress == 223){
            spriten.setAlpha(0.4f);
        } else if (progress == 226){
            spriten.setAlpha(0.3f);
        } else if (progress == 229){
            spriten.setAlpha(0.2f);
        } else if (progress == 232){
            spriten.setAlpha(0.1f);
        } else if (progress == 236) {
            spriten.setAlpha(0.0f);
        }

         else if (progress == 320){
            spriteIntro.setAlpha(0.9f);
        } else if (progress == 323){
            spriteIntro.setAlpha(0.8f);
        } else if (progress == 326){
            spriteIntro.setAlpha(0.7f);
        } else if (progress == 329){
            spriteIntro.setAlpha(0.6f);
        } else if (progress == 332){
            spriteIntro.setAlpha(0.5f);
        } else if (progress == 335){
            spriteIntro.setAlpha(0.4f);
        } else if (progress == 338){
            spriteIntro.setAlpha(0.3f);
        } else if (progress == 341){
            spriteIntro.setAlpha(0.2f);
        } else if (progress == 344){
            spriteIntro.setAlpha(0.1f);
        } else if (progress == 347){
            spriteIntro.setAlpha(0.0f);
        }

         //Kamera an Batch binden
        jumpAndRun.batch.setProjectionMatrix(camera.combined);

         //Grafikzeichnen beginnen
        jumpAndRun.batch.begin();

        //Die drei Logos nach einander
        if (progress > 240){
            spriteIntro.draw(jumpAndRun.batch);
        } else if (progress > 120) {
            spriten.draw(jumpAndRun.batch);
        } else {
            sprite.draw(jumpAndRun.batch);
        }

        //Grafikzeichnen beenden
        jumpAndRun.batch.end();

        //Formen zeichnen beginnen
        //-------------------------------------------------------------------------------------------------------
        //Hiermit wird der Ladebalken gezeichnet (eine Form die immer breiter wird)
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        shapeRenderer.setColor(Color.DARK_GRAY);
        shapeRenderer.rect(10,10 ,Gdx.graphics.getWidth() -20, 10);

        if (loadingbar <= 1) {
            shapeRenderer.setColor(Color.WHITE);
            shapeRenderer.rect(10, 10, (Gdx.graphics.getWidth() -20) * loadingbar, 10);
        } else {
            shapeRenderer.setColor(Color.WHITE);
            shapeRenderer.rect(10, 10, Gdx.graphics.getWidth() - 20, 10);
        }

        shapeRenderer.end();
        //-------------------------------------------------------------------------------------------------------
    }

    @Override
    public void resize(int width, int height) {
        //Wenn sich die Bildschrimgröße ändert, Bild skalieren (Desktop)
        viewport.update(width, height);
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
        splash01.dispose();
        splash02.dispose();
        splash03.dispose();
    }
}
