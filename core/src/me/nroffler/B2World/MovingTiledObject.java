package me.nroffler.B2World;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.utils.Disposable;

import me.nroffler.Screens.Playscreen;
import me.nroffler.main.Statics;

public abstract class MovingTiledObject implements Disposable {

    protected Playscreen playscreen;
    protected Sprite sprite;
    protected Body b2body;
    protected float height;
    protected Rectangle rectangle;

    public MovingTiledObject(Playscreen playscreen, Rectangle rect, Texture texture) {
        this.playscreen = playscreen;
        this.rectangle = rect;
        sprite = new Sprite(texture);
        height = rect.getHeight();
        sprite.setBounds(rect.getX() / Statics.PPM, rect.getY() /Statics.PPM, rect.getWidth() /Statics.PPM, 16 /Statics.PPM);
        defineObject();
    }

    protected abstract void defineObject();
    public abstract void draw(SpriteBatch batch);
    public abstract void update(float dt);
    public abstract void touch();
}
