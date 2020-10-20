package me.nroffler.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import me.nroffler.main.JumpAndRun;
import me.nroffler.main.PrefManager;
import me.nroffler.main.Statics;

public class MainMenu implements Screen {

    private JumpAndRun jumpAndRun;

    private Stage stage;
    private Table container, optionscontainer;

    private Viewport viewport;
    private OrthographicCamera camera;

    private Texture bgtexture;
    private Texture roundtexture;
    private Texture mainmenulabel, mainmenulabel1;
    private Texture uibg;
    private Texture unlocked, locked, play;
    private Texture checkboxout, checkboxin;
    private Texture resetbtn;

    //um das Einstellungsmenü zu öffnen / schließen
    private boolean settings = false;
    private boolean options = false;

    private boolean sound;
    private boolean musik;
    private boolean effekte;
    private boolean hud;

    //Schwierigkeit des Spiels
    private Difficulty difficulty;
    public enum Difficulty{
        Normal,
        Hard
    }

    public MainMenu(JumpAndRun jumpAndRun) {
        this.jumpAndRun = jumpAndRun;

        difficulty = Difficulty.Normal;

        //Werte auslesen (da nicht im Spiel sondern nur im Menü wurden sie auch nicht vorgeladen)
        musik = jumpAndRun.prefManager.getBoolean(PrefManager.PREF_MUSIC_ENABLED);
        sound = jumpAndRun.prefManager.getBoolean(PrefManager.PREF_SOUND_ENABLED);
        effekte = jumpAndRun.prefManager.getBoolean(PrefManager.PREF_EFFEKT_ENABLED);
        hud = jumpAndRun.prefManager.getBoolean(PrefManager.PREF_HUD_ENABLED);

        //neue Kamera und Viewport
        camera = new OrthographicCamera();
        viewport = new FitViewport(Statics.V_WIDTH, Statics.V_HEIGHT, camera);

        //die Table werden dazu verwendet, um die Elemente anzuordnen
        container = new Table();
        optionscontainer = new Table();

        //2D Menü = Stage
        stage = new Stage(viewport, jumpAndRun.batch);

        //Texturen holen
        bgtexture = jumpAndRun.assetManager.get("Backgrounds/bg0.png");
        mainmenulabel = jumpAndRun.assetManager.get("Font/mainmenu.png");
        unlocked = jumpAndRun.assetManager.get("UI/BTN_PLAIN5.png");
        locked = jumpAndRun.assetManager.get("UI/BTN_PLAIN6.png");
        roundtexture = jumpAndRun.assetManager.get("UI/BTN_GREEN_CIRCLE_OUT.png");
        Texture roundtexturein = jumpAndRun.assetManager.get("UI/BTN_GREEN_CIRCLE_IN.png");
        mainmenulabel1 = jumpAndRun.assetManager.get("Font/mainmenu1.png");
        uibg = jumpAndRun.assetManager.get("UI/uibg.png");
        play = jumpAndRun.assetManager.get("UI/BTN_PLAY.png");
        checkboxout = jumpAndRun.assetManager.get("UI/BTN_CHECKBOX_OUT.png");
        checkboxin = jumpAndRun.assetManager.get("UI/BTN_CHECKBOX_IN.png");
        resetbtn = jumpAndRun.assetManager.get("UI/UI_BTN_RESET.png", Texture.class);

        //Input wird von dem 2D Menü verarbeitet
        Gdx.input.setInputProcessor(stage);

        swbuttonMode();
        swoptions();
        swbuttonMode();
    }

    private void swSettings(){
        //Menü offen oder nicht
        settings = !settings;

        //Menü öffnen / schließen
        swoptions();

    }

    private void swoptions(){
        //----------------------------------------------------------------------------------------------------
        //neues Bild erstellen, Form geben und anordnen im 2D Menü (Stage)
        Image imageoption = new Image(roundtexture);

        optionscontainer.clear();

        optionscontainer.right();
        optionscontainer.bottom();
        optionscontainer.padRight(5f);
        optionscontainer.padBottom(5f);
        optionscontainer.setFillParent(true);

        imageoption.setSize(18f*1.3f, 18.9f*1.3f);
        imageoption.setName("options");
        imageoption.addListener(myclickListener);
        imageoption.setPosition(Statics.V_WIDTH- imageoption.getWidth()-10, 10);
        imageoption.setOrigin(imageoption.getWidth() / 2, imageoption.getHeight() / 2);
        if (options) {
            imageoption.addAction(Actions.rotateBy(45, 0.5f));
        } else {
            imageoption.addAction(Actions.rotateBy(-45, 0.5f));
        }

        Table placeholder = new Table();
        placeholder.addActor(imageoption);

        optionscontainer.addActor(placeholder);

        stage.addActor(optionscontainer);
        //----------------------------------------------------------------------------------------------------
    }

    private void swBoxes(){
        //----------------------------------------------------------------------------------------------------
        //neue Bilder erstellen, Formen geben und anordnen im 2D Menü (Stage)
        if (settings){
            Image image;

            if (sound) {
                image = new Image(checkboxin);
            } else {
                image = new Image(checkboxout);
            }

            image.setSize(17.1f, 17.5f);
            image.setName("sound");
            image.addListener(myclickListener);
            image.setPosition(282, 124);

            optionscontainer.addActor(image);




            Image image2;

            if (musik) {
                image2 = new Image(checkboxin);
            } else {
                image2 = new Image(checkboxout);
            }

            image2.setSize(17.1f, 17.5f);
            image2.setName("musik");
            image2.addListener(myclickListener);
            image2.setPosition(282, 102);

            optionscontainer.addActor(image2);




            Image image3;

            if (effekte) {
                image3 = new Image(checkboxin);
            } else {
                image3 = new Image(checkboxout);
            }

            image3.setSize(17.1f, 17.5f);
            image3.setName("effekte");
            image3.addListener(myclickListener);
            image3.setPosition(282, 80);

            optionscontainer.addActor(image3);




            Image image4;

            if (hud) {
                image4 = new Image(checkboxin);
            } else {
                image4 = new Image(checkboxout);
            }

            image4.setSize(17.1f, 17.5f);
            image4.setName("hud");
            image4.addListener(myclickListener);
            image4.setPosition(282, 58);

            optionscontainer.addActor(image4);




            Image image5;

            image5 = new Image(resetbtn);

            image5.setSize(17.1f*3, 17.5f);
            image5.setName("reset");
            image5.addListener(myclickListener);
            image5.setPosition(295, 28);

            optionscontainer.addActor(image5);
            //----------------------------------------------------------------------------------------------------
        }
    }

    private void swbuttonMode(){
        //----------------------------------------------------------------------------------------------------
        //neue Bilder erstellen, Formen geben und anordnen im 2D Menü (Stage)
        container.clear();

        if (difficulty == Difficulty.Normal){
            difficulty = Difficulty.Hard;
        } else if (difficulty == Difficulty.Hard){
            difficulty = Difficulty.Normal;
        }

        container.top();
        container.padTop(90);
        container.left();
        container.setFillParent(true);
        container.padLeft(73f);

        Image image;
        if (difficulty == Difficulty.Normal) {
            image = new Image(unlocked);
        } else {
            image = new Image(locked);
        }
        image.setSize(28.5f, 23.5f);

        image.setName("easy");
        image.addListener(myclickListener);

        Table placeholder = new Table();
        placeholder.addActor(image);

        container.add(placeholder);

        Image image2;
        if (difficulty == Difficulty.Normal){
            image2 = new Image(locked);
        } else {
            image2 = new Image(unlocked);
        }

        image2.setSize(28.5f, 23.5f);

        image2.setName("original");
        image2.addListener(myclickListener);

        Table placeholder2 = new Table();
        placeholder2.addActor(image2);

        container.add(placeholder2).pad(35);

        Image imageplay = new Image(play);
        imageplay.setName("start");
        imageplay.addListener(myclickListener);
        imageplay.setSize(18f*1.2f, 18.9f*1.2f);
        imageplay.setPosition(145f, 84f);

        Table placeholder3 = new Table();
        placeholder3.addActor(imageplay);

        container.addActor(placeholder3);

        stage.addActor(container);
        //----------------------------------------------------------------------------------------------------

        //Übergangseffekt (SplashScreen -> MainMenu)
        stage.addAction(Actions.sequence(Actions.fadeIn(0.3f), Actions.run(new Runnable() {
            @Override
            public void run() {

            }
        })));

    }

    //Input verarbeiten
    public ClickListener myclickListener = new ClickListener() {
        @Override
        public void clicked(final InputEvent event, float x, float y) {
            //Buttons durch Namen unterscheiden und Menüs durchschalten
            switch (event.getListenerActor().getName()) {
                case "easy":
                case "original":
                    swbuttonMode();
                    break;
                case "options":
                    options = !options;
                    swoptions();
                    swSettings();
                    swBoxes();
                    break;
                case "start":
                    stage.addAction(Actions.sequence(Actions.fadeOut(0.3f), Actions.run(new Runnable() {
                        @Override
                        public void run() {
                            if (difficulty == Difficulty.Normal) {
                                jumpAndRun.setScreen(new RootScreen(jumpAndRun, RootScreen.Difficulty.Normal));
                            } else if (difficulty == Difficulty.Hard){
                                jumpAndRun.setScreen(new RootScreen(jumpAndRun, RootScreen.Difficulty.Hard));
                            }
                        }
                    })));
                    break;
                case "sound":
                    sound = !sound;
                    swBoxes();
                    jumpAndRun.prefManager.saveBooleanState(PrefManager.PREF_SOUND_ENABLED, sound);
                    break;
                case "musik":
                    musik = !musik;
                    swBoxes();
                    jumpAndRun.prefManager.saveBooleanState(PrefManager.PREF_MUSIC_ENABLED, musik);
                    break;
                case "effekte":
                    effekte = !effekte;
                    swBoxes();
                    jumpAndRun.prefManager.saveBooleanState(PrefManager.PREF_EFFEKT_ENABLED, effekte);
                    break;
                case "hud":
                    hud = !hud;
                    swBoxes();
                    jumpAndRun.prefManager.saveBooleanState(PrefManager.PREF_HUD_ENABLED, hud);

                    Statics.hud = hud;
                    break;
                case "reset":
                    jumpAndRun.setScreen(new SplashScreen(jumpAndRun, SplashScreen.LoadState.CMD_RESETT_DATA));
                    break;
            }
        }
    };

    @Override
    public void show() { }


    public void update(){

    }

    @Override
    public void render(float delta) {
        update();

        Gdx.gl.glClearColor(0,0,0,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        jumpAndRun.batch.setProjectionMatrix(camera.combined);
        jumpAndRun.batch.begin();

        jumpAndRun.batch.draw(bgtexture, 0, 0, Statics.V_WIDTH, Statics.V_HEIGHT);

        if (difficulty == Difficulty.Normal) {
            jumpAndRun.batch.draw(mainmenulabel, 0, 0, Statics.V_WIDTH, Statics.V_HEIGHT);
        } else {
            jumpAndRun.batch.draw(mainmenulabel1, 0, 0, Statics.V_WIDTH, Statics.V_HEIGHT);
        }

        if (settings){
            float sclf = 0.8f;
            jumpAndRun.batch.draw(uibg, Statics.V_WIDTH / 60 * 46,10, 113 * sclf, 204 * sclf);
        }

        jumpAndRun.batch.end();

        //2D Menü zeichnen
        stage.act(delta);
        stage.draw();

    }

    @Override
    public void resize(int width, int height) {
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

    }
}
