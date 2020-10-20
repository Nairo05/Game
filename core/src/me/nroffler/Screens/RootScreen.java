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

public class RootScreen implements Screen {

    private JumpAndRun jumpAndRun;

    private Difficulty difficulty;
    public enum Difficulty{
        Normal,
        Hard
    }

    private Stage stage;

    private Table container;
    private Table skipTable;
    private Table skip2Table;

    private Viewport viewport;
    private OrthographicCamera camera;
    private Texture unlocked, locked;

    private Texture bgtexture, bgtexture2;
    private Texture arrow_left, arrow_right, star_emtpy, star_filled, nostarslocked;

    private int level;
    private int world;

    public RootScreen(JumpAndRun jumpAndRun, Difficulty difficulty) {
        this.jumpAndRun = jumpAndRun;
        this.world = Statics.current_world;
        this.difficulty = difficulty;

        level = jumpAndRun.prefManager.getInteger(PrefManager.PREF_UNLOCKLEVEL, 1);

        unlocked = jumpAndRun.assetManager.get("UI/BTN_PLAIN5.png", Texture.class);
        locked = jumpAndRun.assetManager.get("UI/BTN_PLAIN6.png", Texture.class);
        bgtexture2 = jumpAndRun.assetManager.get("Backgrounds/bg0.png", Texture.class);
        arrow_left = jumpAndRun.assetManager.get("UI/SYMB_RR.png", Texture.class);
        arrow_right = jumpAndRun.assetManager.get("UI/SYMB_FF.png", Texture.class);
        star_emtpy = jumpAndRun.assetManager.get("UI/UI_STAR_EMTPY.png", Texture.class);
        star_filled = jumpAndRun.assetManager.get("UI/UI_STAR_NORMAL.png", Texture.class);
        nostarslocked = jumpAndRun.assetManager.get("UI/UI_LOCK.png", Texture.class);

        camera = new OrthographicCamera();
        viewport = new FitViewport(Statics.V_WIDTH, Statics.V_HEIGHT, camera);

        stage = new Stage(viewport, jumpAndRun.batch);
        Gdx.input.setInputProcessor(stage);

        container = new Table();
        skipTable = new Table();
        skip2Table = new Table();

        switchStage();

    }

    private void createSkipLeftTable() {
        skip2Table.bottom();
        skip2Table.left();
        skip2Table.padLeft(10);
        skip2Table.padBottom(10);
        skip2Table.setFillParent(true);

        Image image = new Image(unlocked);
        image.setSize(28.5f, 23.5f);

        image.setName("left");
        image.addListener(myclickListener);

        Table placeholder = new Table();
        placeholder.addActor(image);

        skip2Table.add(placeholder);

        Image imagesymbol = new Image(arrow_left);
        imagesymbol.setSize(28.5f*0.6f, 23.5f*0.6f);
        imagesymbol.setName("left");
        imagesymbol.addListener(myclickListener);

        Table placeholder2 = new Table();
        placeholder2.addActor(imagesymbol);

        skip2Table.add(placeholder2).padLeft(6f).padBottom(10f);

        stage.addActor(skip2Table);
    }

    private void createSkipRightTable() {
        skipTable.bottom();
        skipTable.right();
        skipTable.padRight(40f);
        skipTable.padBottom(10f);
        skipTable.setFillParent(true);

        Image image;

        if (world == 1 && level > 12) {
            image = new Image(unlocked);
        } else if (world == 2 && level > 24){
            image = new Image(unlocked);
        } else {
            image = new Image(locked);
        }
        image.setSize(28.5f, 23.5f);

        image.setName("right");
        image.addListener(myclickListener);

        Table placeholder = new Table();
        placeholder.addActor(image);

        skipTable.add(placeholder);

        if (world == 1 && level > 12) {
            Image imagesymbol = new Image(arrow_right);
            imagesymbol.setSize(28.5f * 0.6f, 23.5f * 0.6f);
            imagesymbol.setName("right");
            imagesymbol.addListener(myclickListener);


            Table placeholder2 = new Table();
            placeholder2.addActor(imagesymbol);


            skipTable.add(placeholder2).padLeft(6f).padBottom(10f);
        }

        stage.addActor(skipTable);
    }

    private void createLevelMenu() {
        container.top();
        int infooffset = 80;
        container.padTop(infooffset);
        container.left();
        container.setFillParent(true);

        container.padLeft(78f);
        container.padRight(30f);

        bgtexture2 = jumpAndRun.assetManager.get("Backgrounds/bg"+world+".png", Texture.class);

        int rlevel;

        if (world == 1) {
            rlevel = 1;
        } else if (world == 2){
            rlevel = 13;
        } else {
            rlevel = 25;
        }

        for (int i = 1; i < 3 ; i++) {
            for (int j = 1; j < 7; j++) {

                Image image = null;
                boolean lvlstate;

                if (rlevel <= jumpAndRun.prefManager.getInteger(PrefManager.PREF_UNLOCKLEVEL, 1)) {
                    image = new Image(unlocked);
                    lvlstate = true;
                } else {
                    image = new Image(locked);
                    lvlstate = false;
                }
                image.addListener(myclickListener);
                image.setName(Integer.toString(rlevel));

                image.setSize(28.5f, 23.5f);
                Table placeholder = new Table();
                placeholder.addActor(image);

                if (lvlstate) {
                    placeholder.addActor(buildstarTable(image, false));
                } else {
                    placeholder.addActor(buildstarTable(image, true));
                }

                container.add(placeholder).padRight(10f).padBottom(10f).width(32).height(32);

                rlevel++;
            }
            container.row();
        }

        stage.addActor(container);
    }

    private Table buildstarTable(Image imagesource, boolean locked){

        Table table = new Table();
        int stars = jumpAndRun.levelStarProgressSafer.getLevelData(imagesource.getName());

        if (!locked) {
            for (int i = 0; i < stars; i++) {
                Image image = new Image(star_filled);
                image.setBounds(imagesource.getX() + i * 10, imagesource.getY(), 10, 10);
                image.setName(imagesource.getName());
                image.addListener(myclickListener);
                table.addActor(image);
            }

            for (int i = 3; i > stars; i--) {
                Image image = new Image(star_emtpy);
                image.setBounds(imagesource.getX() + i * 10 - 10, imagesource.getY(), 10, 10);
                image.setName(imagesource.getName());
                image.addListener(myclickListener);
                table.addActor(image);
            }

        } else {
            Image image = new Image(nostarslocked);
            image.setBounds(imagesource.getX(), imagesource.getY(), 10, 10);
            image.setName(imagesource.getName());
            image.addListener(myclickListener);
            table.addActor(image);
        }
        return table;
    }

    private void switchStage(){
        stage.clear();

        skipTable.clear();
        skip2Table.clear();

        container.clear();

        System.out.println(world);
        bgtexture = jumpAndRun.assetManager.get("Font/welt"+world+".png", Texture.class);

        if (world > 0) {
            createLevelMenu();
        }

        if (world > 0) {
            createSkipLeftTable();
        }
        if (world < 4 ) {
            createSkipRightTable();
        }

        stage.addAction(Actions.sequence(Actions.fadeIn(0.3f), Actions.run(new Runnable() {
            @Override
            public void run() {

            }
        })));
    }

    public ClickListener myclickListener = new ClickListener(){
        @Override
        public void clicked(final InputEvent event, float x, float y) {
            if (event.getListenerActor().getName().equals("right")){
                if (world < 4){
                    if (jumpAndRun.prefManager.getInteger(PrefManager.PREF_UNLOCKLEVEL, 1) / 12 >= world) {
                        stage.addAction(Actions.sequence(Actions.fadeOut(0.3f), Actions.run(new Runnable() {
                            @Override
                            public void run() {
                                world++;
                                switchStage();
                            }
                        })));
                    }
                }
            } else if (event.getListenerActor().getName().equals("left")){
                stage.addAction(Actions.sequence(Actions.fadeOut(0.3f), Actions.run(new Runnable() {
                    @Override
                    public void run() {
                        if (world == 1){
                            jumpAndRun.setScreen(new MainMenu(jumpAndRun));
                            return;
                        } else if (world > 1) {
                            world--;
                        }
                        switchStage();
                    }
                })));
            } else {
                level = Integer.parseInt(event.getListenerActor().getName());
                System.out.println(level);
                if (level <= jumpAndRun.prefManager.getInteger(PrefManager.PREF_UNLOCKLEVEL, 1)) {

                    stage.addAction(Actions.sequence(Actions.fadeOut(0.3f), Actions.run(new Runnable() {
                        @Override
                        public void run() {
                            Statics.current_world = world;
                            if (difficulty == RootScreen.Difficulty.Normal) {
                                jumpAndRun.setScreen(new LoadingScreen(jumpAndRun, level, LoadingScreen.Difficulty.Normal));
                            } else if (difficulty == RootScreen.Difficulty.Hard){
                                jumpAndRun.setScreen(new LoadingScreen(jumpAndRun, level, LoadingScreen.Difficulty.Hard));
                            }
                        }
                    })));
                }
            }
        }
    };


    @Override
    public void show() {
        System.out.println("-- navigate: Root --");
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0,0,0,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        jumpAndRun.batch.begin();

        jumpAndRun.batch.draw(bgtexture2, 0,0, Statics.V_WIDTH, Statics.V_HEIGHT);
        jumpAndRun.batch.draw(bgtexture, Statics.V_WIDTH / 2 - bgtexture.getWidth() / 2,Statics.V_HEIGHT - bgtexture.getHeight() - 10f);

        jumpAndRun.batch.end();

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
