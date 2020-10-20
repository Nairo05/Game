package me.nroffler.spawnable;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;

import java.util.Random;

import me.nroffler.Screens.Playscreen;
import me.nroffler.main.BitFilter;
import me.nroffler.main.Statics;

public class DynamicCoin extends SpawnAble {

    private TextureRegion textureRegion[][];
    private Texture texture;
    private int framecount = 0;
    private int frame = 0;
    private int startcount;

    public DynamicCoin(Playscreen playscreen, float x, float y, int meta) {
        super(playscreen, x, y, meta);

        texture = playscreen.getAssetManager().get("Sprite/spr_coin_strip4.png", Texture.class);
        textureRegion = TextureRegion.split(texture, 16, 16);

        setRegion(textureRegion[0][frame]);
        startcount = meta*10;
    }

    @Override
    public void defineSpawnAble() {
        BodyDef bdef = new BodyDef();
        bdef.type = BodyDef.BodyType.DynamicBody;
        bdef.position.set(getX() + getWidth() / 2, getY() + getHeight() / 2 + 16/ Statics.PPM);
        if (body == null && !world.isLocked()) {
            body = world.createBody(bdef);
        }

        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(6 / Statics.PPM);
        fdef.shape = shape;
        fdef.filter.categoryBits = BitFilter.SPAWNABLE_BIT;
        fdef.filter.maskBits = BitFilter.PLAYER_BIT | BitFilter.BRICK_BIT | BitFilter.DEFAULT_BIT | BitFilter.OBJEKT_BIT | BitFilter.MOVING_BIT;
        body.createFixture(fdef).setUserData(this);

        FixtureDef fdefhit = new FixtureDef();
        PolygonShape shapehit = new PolygonShape();
        shapehit.setAsBox(10 / Statics.PPM,10 / Statics.PPM);
        fdefhit.shape = shapehit;
        fdefhit.filter.categoryBits = BitFilter.SPAWNABLE_BIT;
        fdef.filter.maskBits = BitFilter.PLAYER_BIT;
        fdefhit.isSensor = true;
        body.createFixture(fdefhit).setUserData(this);

        shapehit.dispose();
        shape.dispose();
    }

    @Override
    public void use() {
        System.out.println("spawnable used");
        playscreen.getPlayer().collectGem();
        if (!destroyed && !toDestroyed) {
            if (body != null) {
                destroy();
            }
        }
    }

    @Override
    public void update(float dt) {

        super.update(dt);

        if(!destroyed) {
            if (startcount > 0) {
                startcount--;
            } else if (startcount == 0){

                float ximpuls = new Random().nextInt(2);
                if (ximpuls == 0){
                    ximpuls = -0.2f - new Random().nextFloat()*1f;
                } else {
                    ximpuls = 0.2f + new Random().nextFloat()*1f;
                }

                float yimpuls = new Random().nextFloat() * 1.5f + 2f;

                body.applyLinearImpulse(new Vector2(ximpuls, yimpuls), body.getWorldCenter(), true);

                startcount--;
            }

            if (!toDestroyed && !destroyed) {
                framecount++;
                if ((framecount % 15) == 0) {
                    frame++;
                    if (frame > 3) {
                        frame = 0;
                    }
                }
            }

            setRegion(textureRegion[0][frame]);
            setPosition(body.getPosition().x - getWidth() / 2, body.getPosition().y - getHeight() / 2);
        }


    }

    @Override
    public void draw(Batch batch) {
        if (startcount <= 0 && !destroyed) {
            super.draw(batch);
        }
    }

    @Override
    public void dispose() {
        texture.dispose();
    }
}
