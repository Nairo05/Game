package me.nroffler.Enemys;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.utils.Disposable;

import java.util.Random;

import me.nroffler.Screens.Playscreen;
import me.nroffler.main.BitFilter;
import me.nroffler.main.Statics;

public class Slime extends Enemy implements Disposable {

    private boolean flipX, flipY, rflipX, rflipY;
    private int leben, hitcount;
    private Texture slimehit;
    private int deathTexturecounter;
    private Texture slimedathtexture;

    public Slime(Playscreen playscreen, float x, float y, boolean boss) {
        super(playscreen, x, y, boss);

        row = new Random().nextInt(2)+1;

        slimehit = playscreen.getAssetManager().get("Sprite/slimehit.png", Texture.class);

        if (row == 2) {
            slimedathtexture = playscreen.getAssetManager().get("Sprite/green_death.png", Texture.class);
        } else {
            slimedathtexture = playscreen.getAssetManager().get("Sprite/blue_death.png", Texture.class);
        }

        if (boss) {
            leben = 6;
            deathTexturecounter = 40;
            setBounds(0, 0, 40 / Statics.PPM, 36 / Statics.PPM);
        } else {
            setBounds(0, 0, 20 / Statics.PPM, 20 / Statics.PPM);
            leben = 1;
            deathTexturecounter = 40;
        }

        setRegion(regions[row][frame]);

        rflipX = false;
        rflipY = false;
    }

    @Override
    protected void defineEnemy() {
        BodyDef bdef = new BodyDef();
        bdef.position.set(getX(), getY());
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        if (boss){
            PolygonShape shape = new PolygonShape();
            shape.setAsBox(13 / Statics.PPM, 5 / Statics.PPM);
            fdef.shape = shape;
        } else {
            CircleShape shape = new CircleShape();
            shape.setRadius(6 / Statics.PPM);
            fdef.shape = shape;
        }
        fdef.filter.categoryBits = BitFilter.ENEMY_BIT;
        fdef.filter.maskBits = BitFilter.BRICK_BIT | BitFilter.DEFAULT_BIT | BitFilter.COIN_BIT | BitFilter.PLAYER_BIT | BitFilter.ENEMY_BIT;

        b2body.createFixture(fdef).setUserData(this);

        PolygonShape head = new PolygonShape();
        Vector2[] vertice = new Vector2[4];
        if (boss){
            vertice[0] = new Vector2(-12 ,14).scl(1 / Statics.PPM);
            vertice[1] = new Vector2(12 ,14).scl(1 / Statics.PPM);
        } else {
            vertice[0] = new Vector2(-5 ,11).scl(1 / Statics.PPM);
            vertice[1] = new Vector2(5 ,11).scl(1 / Statics.PPM);
        }
        vertice[2] = new Vector2(-4 ,3).scl(1 / Statics.PPM);
        vertice[3] = new Vector2(4 ,3).scl(1 / Statics.PPM);
        head.set(vertice);

        fdef.shape = head;

        if (boss){
            fdef.restitution = 1f;
        } else {
            fdef.restitution = 0.5f;
        }

        fdef.filter.categoryBits = BitFilter.ENEMY_HEAD_BIT;
        b2body.createFixture(fdef).setUserData(this);

    }

    @Override
    public void setFlip (boolean x, boolean y) {
        super.setFlip(x, y);
        this.flipX = x;
        this.flipY = y;
    }

    @Override
    public void flip (boolean x, boolean y) {
        super.flip(x, y);
        if (x) this.flipX = !flipX;
        if (y) this.flipY = !flipY;
    }

    @Override
    public void setRegion (float u, float v, float u2, float v2) {
        super.setRegion(u, v, u2, v2);
        super.setFlip(flipX, flipY);
    }

    @Override
    public void update(float dt) {
        if (deathTexturecounter < 40 && !destroyed && !boss) {
            //Kann nicht in hitonhead sonst fliegt einem box2d um die ohren
            b2body.setLinearVelocity(0, 0);
            //

            setRegion(slimedathtexture);

            deathTexturecounter--;

            if (deathTexturecounter <= 0) {
                System.out.println("slime.destroyed");
                setToDestroyed = true;
            }
        } else if (deathTexturecounter < 40 && !destroyed && boss){
            setToDestroyed = true;
        } else if (!destroyed && deathTexturecounter == 40) {
            framecount++;

            if ((framecount % 20) == 0) {
                frame++;
                if (frame > 1) {
                    frame = 0;
                }
            }

            if (velocity.x > 0) {
                rflipX = true;
            } else if (velocity.x < 0) {
                rflipX = false;
            }

            if (setToDestroyed && !destroyed) {
                world.destroyBody(b2body);
                destroyed = true;

            } else if (!destroyed) {
                b2body.setLinearVelocity(velocity);
                if (boss) {
                    setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 4 + 0.02f);
                } else {
                    setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 3 - 0.02f);
                }
            }

            setFlip(rflipX, rflipY);

            if (!(hitcount < framecount)) {
                if ((framecount % 7) == 0) {
                    frame++;
                    if (frame > 1) {
                        frame = 0;
                    }
                    if (frame == 1) {
                        setRegion(regions[row][frame]);
                    } else if (frame == 0) {
                        setRegion(slimehit);
                    }
                }
            } else {
                setRegion(regions[row][frame]);
            }
        }

        if (setToDestroyed && !destroyed) {
            world.destroyBody(b2body);
            destroyed = true;
        }
    }

    @Override
    public void hitOnHead() {
        leben--;

        if (!boss) {
            playscreen.getPlayer().boostedjump();
        }

        setRegion(slimehit);

        hitcount = framecount+42;

        if (leben == 0) {
            deathTexturecounter--;
            //setToDestroyed = true;
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
        slimedathtexture.dispose();
    }
}
