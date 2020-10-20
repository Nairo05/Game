package me.nroffler.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import me.nroffler.main.JumpAndRun;
import me.nroffler.main.PrefManager;
import me.nroffler.main.Statics;

public class GameOverScreen implements Screen {

    private JumpAndRun jumpAndRun;

    private int state, statemode;
    private int coins;
    private int framecount, frame, neededframes;
    private int digit1, digit2;
    private int rcoins;
    int divider = 6;

    private Texture texture, texture2, texttexture;
    private Texture heartTexture;
    private TextureRegion[][] hearts;
    private TextureRegion[][] numbers;
    private TextureRegion[][] coinTextures;

    private ParticleEffect particleEffect;

    public GameOverScreen(JumpAndRun jumpAndRun, int stategame, int coins, int statemode, int level){
        this.jumpAndRun = jumpAndRun;
        this.state = stategame;
        this.statemode = statemode;
        this.coins = coins;

        rcoins = Statics.coins;
        framecount = 0;
        frame = 0;
        neededframes = 0;

        //Partikeleffekt auslesen aus Datei
        particleEffect = new ParticleEffect();
        particleEffect.load(Gdx.files.internal("Particle/coins.pe"), Gdx.files.internal(""));

        //--------------------------------------------------------------------------------------------------------
        //Sheets laden und ausschneiden
        heartTexture = jumpAndRun.assetManager.get("Sprite/hearts16x16.png", Texture.class);
        hearts = TextureRegion.split(heartTexture, 16, 16);

        texture = jumpAndRun.assetManager.get("Font/number_font.png", Texture.class);
        numbers = TextureRegion.split(texture, 16,16);

        texture2 = jumpAndRun.assetManager.get("Sprite/spr_coin_strip4.png", Texture.class);
        coinTextures = TextureRegion.split(texture2, 16, 16);
        //--------------------------------------------------------------------------------------------------------


        if(state == 3){
            texttexture = new Texture("Font/clear.png");

            //Sterne speichern
            if (statemode == -1){ //normal
               jumpAndRun.levelStarProgressSafer.wirteLeveldata(level+"",2);
            } else if (statemode == 1){ //easy
                jumpAndRun.levelStarProgressSafer.wirteLeveldata(level+"",1);
            }

        } else {
            texttexture = new Texture("Font/failed.png");
        }


    }

    @Override
    public void show() { }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0,0,0,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        //Partikeleffekt updaten (grüne Coins die auseinander fliegen)
        particleEffect.update(delta);


        //Zähler, um Zahl (Coins) zu erhöhen und Animation der Coins zu durchlaufen
        framecount++;

        if (framecount % 15 == 0){
            frame++;
            if (frame > 3){
                frame = 0;
            }
        }

        //Zahl geht schneller höher
        if (divider > 2){
            if (framecount % (15 - divider) == 0){
                divider--;
            }
        }

        if ((framecount % divider) == 0){
            if (rcoins < coins+Statics.coins) {
                rcoins++;

                //100 Coins = 1 Leben
                if ((rcoins % 100) == 0){
                    if (jumpAndRun.prefManager.getInteger(PrefManager.PREF_LIFE_TOTAL, 2) < 6) {
                        jumpAndRun.prefManager.saveIntegerValue(PrefManager.PREF_LIFE_TOTAL,
                                jumpAndRun.prefManager.getInteger(PrefManager.PREF_LIFE_TOTAL, 2)+1);
                    }
                }
            }
        }

        if (rcoins == coins+Statics.coins && neededframes == 0){
            neededframes = framecount + 160;
            particleEffect.setPosition(Statics.V_WIDTH / 2, Statics.V_HEIGHT / 2);
            particleEffect.scaleEffect(0.50f);

            //wenn man mehr als 10 Coins gesammelt hat Effekt starten
            if (coins > 10) {
                particleEffect.start();
            }
        }

        //die Zahl in 2 Ziffern spalten
        digit1 = ( rcoins / 10) % 10;
        digit2 = rcoins%10;


        jumpAndRun.batch.begin();

        jumpAndRun.batch.draw(texttexture,Statics.V_WIDTH / 2 - texttexture.getWidth() / 2,Statics.V_HEIGHT / 2 + texttexture.getHeight() / 2 + texture.getHeight() / 2);

        jumpAndRun.batch.draw(numbers[0][translatetosprite(digit1)], Statics.V_WIDTH / 2 -16, Statics.V_HEIGHT / 2 - texture.getHeight() / 2);
        jumpAndRun.batch.draw(numbers[0][translatetosprite(digit2)], Statics.V_WIDTH / 2 + 0, Statics.V_HEIGHT / 2 - texture.getHeight() / 2);

        for (int i = 0 ; i < jumpAndRun.prefManager.getInteger(PrefManager.PREF_LIFE_TOTAL, 2); i++){
            if (i < 3) {
                jumpAndRun.batch.draw(hearts[0][0], 21*i+3, Statics.V_HEIGHT -20, 16,16);
            }
        }

        if (jumpAndRun.prefManager.getInteger(PrefManager.PREF_LIFE_TOTAL, 2) > 3) {
            for (int i = 3; i < jumpAndRun.prefManager.getInteger(PrefManager.PREF_LIFE_TOTAL, 2); i++) {
                if (i > 6){
                    return;
                }
                jumpAndRun.batch.draw(hearts[1][0], 21*(i-3)+3, Statics.V_HEIGHT -20, 16,16);
            }
        }

        jumpAndRun.batch.draw(coinTextures[0][frame], Statics.V_WIDTH / 2 -32, Statics.V_HEIGHT / 2 - texture.getHeight() / 2);

        if (neededframes > 0) {
            particleEffect.draw(jumpAndRun.batch);
        }

        jumpAndRun.batch.end();


        if (framecount == neededframes) {
            Statics.coins += coins;
            System.out.println("Du hast "+ Statics.coins);

            if (jumpAndRun.prefManager.getInteger(PrefManager.PREF_LIFE_TOTAL, 2) == 0){
                if (jumpAndRun.prefManager.getInteger(PrefManager.PREF_UNLOCKLEVEL, 1) < 13){
                    jumpAndRun.prefManager.saveIntegerValue(PrefManager.PREF_UNLOCKLEVEL, 1);
                } else if (jumpAndRun.prefManager.getInteger(PrefManager.PREF_UNLOCKLEVEL, 1) >= 13){
                    jumpAndRun.prefManager.saveIntegerValue(PrefManager.PREF_UNLOCKLEVEL, 13);
                }
                jumpAndRun.prefManager.saveIntegerValue(PrefManager.PREF_LIFE_TOTAL, 3);
            }

            jumpAndRun.setScreen(new RootScreen(jumpAndRun, RootScreen.Difficulty.Normal));
        }
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
        texture.dispose();
        texttexture.dispose();
        texture2.dispose();
        heartTexture.dispose();
        particleEffect.dispose();
    }
}
