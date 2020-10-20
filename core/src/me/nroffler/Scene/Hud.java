package me.nroffler.Scene;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import me.nroffler.Screens.Playscreen;
import me.nroffler.main.PrefManager;
import me.nroffler.main.Statics;

public class Hud implements Disposable {

    public Stage stage;
    private Viewport viewport;
    private Playscreen playscreen;
    private Table tablelives, tablescore, tablecontrol;
    private TextureRegion[][] numbers;
    private TextureRegion[][] hearts;
    private Texture livet, numberst, arrowtexture;
    private Image image;
    private Image digitimg1, digitimg2;

    private int rlives;
    private int time;
    private int digit1, digit2;
    private int framecount = 0;
    private boolean finished = false;

    public Hud (Playscreen playscreen, SpriteBatch batch, int state) {
        this.playscreen = playscreen;
        rlives = playscreen.getprefManager().getInteger(PrefManager.PREF_LIFE_TOTAL, 2);

        //1 = normal
        if (state == 1){
            time = 60;
        } else {
            time = 99;
        }

        viewport = new FitViewport(Statics.V_WIDTH, Statics.V_HEIGHT, new OrthographicCamera());
        stage = new Stage(viewport, batch);

        // ----- Score -----
        arrowtexture =new Texture("scene_control_1.png");

        tablecontrol = new Table();
        tablecontrol.bottom();
        tablecontrol.left();
        tablecontrol.setFillParent(true);

        if (Statics.hud) {
            initHud();
        }

        // ----- Score -----
        numberst = playscreen.getAssetManager().get("Font/number_font.png", Texture.class);
        numbers = TextureRegion.split(numberst,16,16);

        tablescore = new Table();
        tablescore.top();
        tablescore.right();
        tablescore.setFillParent(true);


        // ----- Leben ------
        livet = playscreen.getAssetManager().get("Sprite/hearts16x16.png", Texture.class);
        hearts = TextureRegion.split(livet, 16, 16);

        tablelives = new Table();
        tablelives.top();
        tablelives.left();
        tablelives.setFillParent(true);


       updateLive();


        tablelives.add(new Actor()).expandX();

        Table tablebuttons = new Table();
        tablebuttons.bottom();
        tablebuttons.setFillParent(true);

        stage.addActor(tablelives);
        // ----- Leben ------


        stage.addActor(tablebuttons);

        System.out.println("hud crearted");
    }

    private void initHud(){
        Image image = new Image(arrowtexture);
        image.setBounds(10 ,2, 48, 48);
        image.setOrigin(image.getWidth() / 2, image.getHeight() / 2);
        image.rotateBy(180);

        Table placeholder = new Table();
        placeholder.addActor(image);

        stage.addActor(placeholder);

        Image image2 = new Image(arrowtexture);
        image2.setBounds(48+10+10, 2, 48, 48);
        image2.setOrigin(image.getWidth() / 2, image.getHeight() / 2);

        Table placeholder2 = new Table();
        placeholder2.addActor(image2);

        stage.addActor(placeholder2);

        Image image3 = new Image(arrowtexture);
        image3.setBounds(Statics.V_WIDTH - 48 - 20, 2, 48, 48);
        image3.setOrigin(image.getWidth() / 2, image.getHeight() / 2);
        image3.rotateBy(90);

        Table placeholder3 = new Table();
        placeholder3.addActor(image3);

        stage.addActor(placeholder3);
    }

    public void finish(){
        finished = true;
    }

    public void update(float dt){
        framecount++;
        if ((framecount % 55) == 0){
            if (!finished) {
                time--;
            }
        }

        if (rlives != playscreen.getprefManager().getInteger(PrefManager.PREF_LIFE_TOTAL, 2)){
            rlives = playscreen.getprefManager().getInteger(PrefManager.PREF_LIFE_TOTAL, 2);

            tablelives.clear();

            updateLive();

            tablelives.add(new Actor()).expandX();

            stage.addActor(tablelives);
        }

        digit1 = (time / 10) % 10;
        digit2 = time%10;

        if (time >= 0){
            tablescore.clear();

            digitimg1 = new Image(numbers[0][translatetosprite(digit1)]);
            digitimg2 = new Image(numbers[0][translatetosprite(digit2)]);

            tablescore.add(digitimg1).padTop(4).padRight(-4);
            tablescore.add(digitimg2).padTop(4).padRight(3);
        } else {
            playscreen.setRunning(Playscreen.State.DEATH_NO_LIVES);
        }
        stage.addActor(tablescore);

        stage.act(dt);
    }


    private int translatetosprite(int digit){
        if (digit == 9){
            return 8;
        } else if (digit == 0){
            return 9;
        } else {
            return digit-1;
        }
    }

    private void updateLive(){
        if (rlives <= 3) {

            for (int i = 1; i < rlives+1; i++){
                image = new Image(hearts[0][0]);
                image.setBounds(i * 20, 0, 16, 16);
                tablelives.add(image).padTop(4).padLeft(3);
            }

        } else if (rlives == 4){
            image = new Image(hearts[1][0]);
            image.setBounds(1 * 20, 0, 16, 16);
            tablelives.add(image).padTop(4).padLeft(3);

            image = new Image(hearts[0][0]);
            image.setBounds(2 * 20, 0, 16, 16);
            tablelives.add(image).padTop(4).padLeft(3);

            image = new Image(hearts[0][0]);
            image.setBounds(3 * 20, 0, 16, 16);
            tablelives.add(image).padTop(4).padLeft(3);
        } else if (rlives == 5){
            image = new Image(hearts[1][0]);
            image.setBounds(1 * 20, 0, 16, 16);
            tablelives.add(image).padTop(4).padLeft(3);

            image = new Image(hearts[1][0]);
            image.setBounds(2 * 20, 0, 16, 16);
            tablelives.add(image).padTop(4).padLeft(3);

            image = new Image(hearts[0][0]);
            image.setBounds(3 * 20, 0, 16, 16);
            tablelives.add(image).padTop(4).padLeft(3);
        } else if (rlives >= 6){
            image = new Image(hearts[1][0]);
            image.setBounds(1 * 20, 0, 16, 16);
            tablelives.add(image).padTop(4).padLeft(3);

            image = new Image(hearts[1][0]);
            image.setBounds(2 * 20, 0, 16, 16);
            tablelives.add(image).padTop(4).padLeft(3);

            image = new Image(hearts[1][0]);
            image.setBounds(3 * 20, 0, 16, 16);
            tablelives.add(image).padTop(4).padLeft(3);
        }
    }

    @Override
    public void dispose() {
        stage.dispose();
        livet.dispose();
        numberst.dispose();
    }
}
