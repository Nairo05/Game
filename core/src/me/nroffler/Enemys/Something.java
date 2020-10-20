package me.nroffler.Enemys;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;

import java.util.Random;

import me.nroffler.Screens.Playscreen;
import me.nroffler.main.BitFilter;
import me.nroffler.main.Statics;

public class Something extends Enemy {

    public Something(Playscreen playscreen, float x, float y, boolean boss) {
        super(playscreen, x, y, boss);

        framecount = (new Random().nextInt(70)+1)*(-1);

        row = new Random().nextInt(1)+10;

        if (boss){
            setBounds(0, 0, 20 / Statics.PPM, 20 / Statics.PPM);
        } else {
            setBounds(0, 0, 20 / Statics.PPM, 20 / Statics.PPM);
        }
        setRegion(regions[row][frame]);

    }

    @Override
    protected void defineEnemy() {
        BodyDef bdef = new BodyDef();
        bdef.position.set(getX(), getY());
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();

        CircleShape shape = new CircleShape();
        shape.setRadius(7 / Statics.PPM);

        fdef.shape = shape;

        fdef.filter.categoryBits = BitFilter.ENEMY_BIT;
        fdef.filter.maskBits = BitFilter.BRICK_BIT | BitFilter.DEFAULT_BIT | BitFilter.COIN_BIT | BitFilter.PLAYER_BIT | BitFilter.ENEMY_BIT;

        b2body.createFixture(fdef).setUserData(this);

        PolygonShape head = new PolygonShape();
        Vector2[] vertice = new Vector2[4];

        vertice[0] = new Vector2(-9 ,14).scl(1 / Statics.PPM);
        vertice[1] = new Vector2(9 ,14).scl(1 / Statics.PPM);

        vertice[2] = new Vector2(-4 ,0).scl(1 / Statics.PPM);
        vertice[3] = new Vector2(4 ,0).scl(1 / Statics.PPM);
        head.set(vertice);

        fdef.shape = head;
        fdef.restitution = 1f;
        fdef.filter.categoryBits = BitFilter.ENEMY_HEAD_BIT;
        fdef.filter.maskBits =  BitFilter.PLAYER_BIT;
        b2body.createFixture(fdef).setUserData(this);
    }

    @Override
    public void update(float dt) {
        if (!destroyed) {
            framecount++;

            if ((framecount % 20) == 0) {
                frame++;
                if (frame == 2) {
                    frame = 0;
                }
            }

            if (setToDestroyed && !destroyed) {
                world.destroyBody(b2body);
                destroyed = true;
            }

            if (!destroyed) {
                if (boss){
                    setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 3);
                } else {
                    setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 3);
                }
                setRegion(regions[row][frame]);
            }

            if (!destroyed) {
                if ((framecount % 50) == 0 && playscreen.getPlayer().b2body.getPosition().x > b2body.getPosition().x - 2f) {
                    b2body.applyLinearImpulse(new Vector2(velocity.x, 2.4f), b2body.getWorldCenter(), true);
                }
            }
        }
    }



    @Override
    public void hitOnHead() {
        setToDestroyed = true;
    }

    public void draw(Batch batch){
        if (!destroyed){
            super.draw(batch);
        }
    }

    @Override
    public void dispose() {
        texture.dispose();
    }
}
