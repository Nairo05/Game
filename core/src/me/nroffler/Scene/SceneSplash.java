package me.nroffler.Scene;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import me.nroffler.Screens.Playscreen;
import me.nroffler.main.Statics;

public class SceneSplash {

    private int progress;
    private Playscreen playscreen;
    private Texture texture;
    private Sprite sprite;
    private int level;

    public SceneSplash(Playscreen playscreen){
        this.playscreen = playscreen;

        level = playscreen.getLevel();
        if (Statics.current_world == 2){
            level -= 8;
        } else if (Statics.current_world == 3){
            level -= 16;
        }

        progress = 0;

        texture = playscreen.getAssetManager().get("Splash/"+ level +".png", Texture.class);
        sprite = new Sprite(texture);

        sprite.setBounds(0.32f, 0, 4.16f, 2.32f);
    }

    public void render(SpriteBatch batch){
        sprite.draw(batch);
    }

    public void update(float dt) {
        progress++;

        if (progress >= 115) {
            playscreen.setRunning(Playscreen.State.ALIVE_RUNNING);
        }

    }

    public void dispose(){
        texture.dispose();
    }

}
