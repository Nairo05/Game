package me.nroffler.B2World;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.utils.Disposable;

import java.util.Random;

import me.nroffler.Screens.Playscreen;
import me.nroffler.main.BitFilter;
import me.nroffler.main.Statics;

public class Coin extends Sprite implements Disposable {

    private Texture texture;
    private Sprite sprite;
    private TextureRegion[][] regions;
    private int framecount, frame;
    public Body b2body;
    private Playscreen playscreen;

    private boolean collected = false;

    public Coin(Rectangle rectangle, Playscreen playscreen){
        this.playscreen = playscreen;

        texture = playscreen.getAssetManager().get("Sprite/spr_coin_strip4.png", Texture.class);
        regions = TextureRegion.split(texture, 16,16);
        sprite = new Sprite(regions[0][0]);

        setBounds(rectangle.getX() / Statics.PPM ,rectangle.getY() / Statics.PPM ,16 / Statics.PPM,16 / Statics.PPM);
        setRegion(sprite);

        frame = new Random().nextInt(3)+1;
        framecount = 0;

        defineCoin();
        b2body.setGravityScale(0.0f);
        b2body.setActive(false);
    }

    private void defineCoin(){
        BodyDef bdef = new BodyDef();
        bdef.position.set(getX() + getWidth() / 2, getY() + getHeight() / 2 + 0.01f);
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2body = playscreen.getWorld().createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(6 / Statics.PPM);
        fdef.filter.categoryBits = BitFilter.COIN_BIT;
        fdef.filter.maskBits = BitFilter.PLAYER_BIT;

        fdef.shape = shape;
        fdef.isSensor = true;
        b2body.createFixture(fdef).setUserData(this);

        shape.dispose();
    }

    public void onHit() {
        if (!collected){
            collected = true;
            playscreen.getPlayer().collectGem();
        }
    }

    @Override
    public void draw(Batch batch){
        if (!collected){
            super.draw(batch);
        }
    }

    public void update(float dt){
        if (!collected && b2body.isActive()) {
            framecount++;
            if ((framecount % 15) == 0) {
                frame++;
                if (frame > 3) {
                    frame = 0;
                }
            }
        }
        sprite.setRegion(regions[0][frame]);
        setRegion(sprite);
    }

    @Override
    public void dispose() {
        texture.dispose();
    }
}
