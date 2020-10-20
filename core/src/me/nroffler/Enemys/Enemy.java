package me.nroffler.Enemys;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Disposable;

import me.nroffler.Screens.Playscreen;

public abstract class Enemy extends Sprite implements Disposable {

    protected World world;
    protected Playscreen playscreen;
    public Body b2body;
    public Vector2 velocity;
    protected boolean boss;

    protected Texture texture;
    protected TextureRegion[][] regions;

    protected int frame, framecount, row;
    protected boolean setToDestroyed;
    protected boolean destroyed;

    public Enemy(Playscreen playscreen, float x, float y, boolean boss){
        this.playscreen = playscreen;
        this.boss = boss;
        this.world = playscreen.getWorld();

        frame = 0;
        framecount = 0;

        destroyed = false;
        setToDestroyed = false;

        texture = playscreen.getAssetManager().get("Sprite/enemies-spritesheet.png", Texture.class);
        regions = TextureRegion.split(texture, 20,20);

        setPosition(x,y);
        defineEnemy();
        b2body.setActive(false);

        velocity = new Vector2(-1,0);
    }

    protected abstract void defineEnemy();
    public abstract void update(float dt);
    public abstract void hitOnHead();
    public boolean getdestroyed(){
        return destroyed;
    }

    public void reverseVelocity(boolean x, boolean y){
        if (x){
            velocity.x = -velocity.x;
        } else if (y){
            velocity.y = -velocity.y;
        }
    }


}
