package me.nroffler.Enemys;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;

import me.nroffler.Screens.Playscreen;
import me.nroffler.main.BitFilter;
import me.nroffler.main.Statics;

public class EnemyShot {

    private Body b2body;
    private Playscreen playscreen;
    private float x, y;
    private int framecount;
    protected boolean setToDestroyed;
    protected boolean destroyed;
    private Texture texture;
    private TextureRegion region;

    public EnemyShot(float x, float y, Playscreen playscreen){
        this.x = x;
        this.y = y;
        this.playscreen = playscreen;

        texture = playscreen.getAssetManager().get("enemies-spritesheet.png", Texture.class);
        region = new TextureRegion(texture,2, 47, 8,8);

        framecount = 0;
        setToDestroyed = false;
        destroyed = false;

        defineBody();

    }

    private void defineBody(){
        BodyDef bdef = new BodyDef();

        bdef.position.set(x, y-0.01f);

        bdef.type = BodyDef.BodyType.DynamicBody;
        b2body = playscreen.getWorld().createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(3 / Statics.PPM);
        fdef.filter.categoryBits = BitFilter.ENEMYTHING;
        fdef.filter.maskBits = BitFilter.BRICK_BIT | BitFilter.DEFAULT_BIT | BitFilter.COIN_BIT | BitFilter.OBJEKT_BIT | BitFilter.MOVING_BIT | BitFilter.PLAYER_BIT;

        fdef.shape = shape;
        b2body.createFixture(fdef).setUserData(this);

        b2body.applyLinearImpulse(new Vector2(-2, 2.4f), b2body.getWorldCenter(), true);

        shape.dispose();
    }

    public boolean removed(){
        return destroyed;
    }


    public void update(float dt){
        if (setToDestroyed && !destroyed){
            playscreen.getWorld().destroyBody(b2body);
            destroyed = true;
        }
    }

    public void destroy(){
        setToDestroyed = true;
    }

    public void draw(Batch batch){
        if (!destroyed) {
            batch.draw(region, b2body.getPosition().x - region.getRegionWidth() / 2 / Statics.PPM, b2body.getPosition().y - region.getRegionHeight() / 2 / Statics.PPM, 0.08f, 0.08f);
        }
    }

    public void finsih(){
       destroy();
    }

}
