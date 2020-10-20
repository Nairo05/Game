package me.nroffler.Enemys;

import com.badlogic.gdx.graphics.Texture;
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

public class Dragon extends Enemy {

    int i = 0;
    int leben = 3;
    private int deathTexturecounter;
    private int hitcounter;
    private Texture texture;
    int destroyedframe = 0;

    public Dragon(Playscreen playscreen, float x, float y, boolean boss) {
        super(playscreen, x, y, boss);

        row = new Random().nextInt(2)+5;

        setBounds(0, 0, 20 / Statics.PPM, 20 / Statics.PPM);
        setRegion(regions[row][frame]);

        texture = playscreen.getAssetManager().get("Sprite/dragon_hit.png");

        deathTexturecounter = 40;
        hitcounter = -1;

    }

    @Override
    protected void defineEnemy() {
        BodyDef bdef = new BodyDef();
        bdef.position.set(getX(), getY());
        bdef.type = BodyDef.BodyType.KinematicBody;
        b2body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();

        CircleShape shape = new CircleShape();
        shape.setRadius(6 / Statics.PPM);
        fdef.shape = shape;

        fdef.filter.categoryBits = BitFilter.ENEMY_BIT;
        fdef.filter.maskBits = BitFilter.BRICK_BIT | BitFilter.DEFAULT_BIT | BitFilter.COIN_BIT | BitFilter.PLAYER_BIT | BitFilter.ENEMY_BIT;

        b2body.createFixture(fdef).setUserData(this);

        PolygonShape head = new PolygonShape();
        Vector2[] vertice = new Vector2[4];

        vertice[0] = new Vector2(-9 ,12).scl(1 / Statics.PPM);
        vertice[1] = new Vector2(9 ,12).scl(1 / Statics.PPM);

        vertice[2] = new Vector2(-4 ,2).scl(1 / Statics.PPM);
        vertice[3] = new Vector2(4 ,2).scl(1 / Statics.PPM);
        head.set(vertice);

        fdef.shape = head;
        fdef.restitution = 1f;
        fdef.filter.categoryBits = BitFilter.ENEMY_HEAD_BIT;
        b2body.createFixture(fdef).setUserData(this);

        b2body.setGravityScale(0);
    }

    @Override
    public void update(float dt) {
        framecount++;

        if (!destroyed && deathTexturecounter < 40 && hitcounter < 0){
            deathTexturecounter--;

            setRegion(texture);

            if (deathTexturecounter < 20){
                setToDestroyed = true;
            }

        } else if (!destroyed && deathTexturecounter == 40 && hitcounter < 0) {

            if ((framecount % 30) == 0) {
                frame++;
                if (frame == 2) {
                    frame = 0;
                }
            }

            setRegion(regions[row][frame]);
        }

        if (!destroyed && hitcounter >= 0){
            hitcounter--;

            if ((hitcounter % 7) == 0) {

                destroyedframe++;

                if ( destroyedframe > 1) {
                    destroyedframe = 0;
                }
                if ( destroyedframe == 1) {
                    setRegion(regions[row][3]);
                } else if ( destroyedframe == 0) {
                    setRegion(texture);
                    System.out.println("hittttttttte");
                }
            }
        }

        if ((((framecount % 80) == 0) && !destroyed && !setToDestroyed)) {
            i++;
                if ((i % 2) == 0) {
                    b2body.setLinearVelocity(new Vector2(0, -0.23f));
                } else {
                    b2body.setLinearVelocity(new Vector2(0, 0.23f));
                }
        }

        if (!destroyed && !setToDestroyed){
            setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 3);
        }

        if (setToDestroyed && !destroyed) {
            world.destroyBody(b2body);
            destroyed = true;
        }
    }


    @Override
    public void hitOnHead() {
        leben--;
        if (leben < 1){
            deathTexturecounter--;
        } else if (leben >= 1 && hitcounter < 0){
            hitcounter = 100;
            frame = 0;
        }
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
