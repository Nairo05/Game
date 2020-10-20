package me.nroffler.Sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Disposable;

import me.nroffler.Screens.Playscreen;
import me.nroffler.main.BitFilter;
import me.nroffler.main.PrefManager;
import me.nroffler.main.Statics;

public class Player extends Sprite implements Disposable {

    private World world;
    public Body b2body;
    private Playscreen playscreen;

    private int frame, framecount;
    private Texture texture;
    private TextureRegion[][] regions;

    private int gems = 0;
    private int statetimer = 0;
    private int animation_speed = 5;

    private boolean isJumping = false;

    private boolean flipX, flipY, rflipX, rflipY;

    public Player(Playscreen playscreen){
        this.playscreen = playscreen;
        this.world = playscreen.getWorld();

        definePlayer();
        setBounds(0,0,24 / Statics.PPM, 24 / Statics.PPM);

        texture = playscreen.getAssetManager().get("Sprite/dino_"+Statics.current_world+".png", Texture.class);
        regions = TextureRegion.split(texture, 24,24);

        setPosition(b2body.getPosition().x, b2body.getPosition().y);

        frame = 0;
        framecount = 0;
        if (Statics.current_world == 3){
            animation_speed = 4;
        }

        rflipX = false;
        rflipY = false;

        b2body.applyForce(new Vector2(0, -1f), b2body.getWorldCenter(), true);
    }

    private void definePlayer(){
        BodyDef bdef = new BodyDef();

        bdef.position.set(240 / Statics.PPM, 48 / Statics.PPM);

        bdef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(7 / Statics.PPM);
        fdef.filter.categoryBits = BitFilter.PLAYER_BIT;
        fdef.filter.maskBits = BitFilter.BRICK_BIT | BitFilter.DEFAULT_BIT | BitFilter.COIN_BIT | BitFilter.ENEMY_BIT | BitFilter.ENEMY_HEAD_BIT | BitFilter.OBJEKT_BIT
                | BitFilter.MOVING_BIT | BitFilter.ENEMYTHING | BitFilter.KILL_ENEMY_BIT | BitFilter.SPAWNABLE_BIT;

        fdef.shape = shape;
        b2body.createFixture(fdef);

        EdgeShape head = new EdgeShape();
        head.set(new Vector2(-6 / Statics.PPM, 7 / Statics.PPM), new Vector2(6 / Statics.PPM, 7 / Statics.PPM));
        fdef.shape = head;
        //fdef.isSensor = true;
        b2body.createFixture(fdef).setUserData("head");

        EdgeShape foot = new EdgeShape();
        foot.set(new Vector2(-4 / Statics.PPM, -9 / Statics.PPM), new Vector2(4 / Statics.PPM, -9 / Statics.PPM));
        fdef.shape = foot;
        fdef.isSensor = true;
        b2body.createFixture(fdef).setUserData("foot");

        shape.dispose();
        head.dispose();
        foot.dispose();
    }

    public void update(){
        statetimer++;

        setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2);
        b2body.setLinearDamping(1.5f);

        if (b2body.getPosition().y < 0){
            playscreen.setRunning(Playscreen.State.DEATH_FALLEN);
        }
        if (playscreen.getprefManager().getInteger(PrefManager.PREF_LIFE_TOTAL, 2) == 0){
            playscreen.setRunning(Playscreen.State.DEATH_NO_LIVES);
        }
        if (b2body.getPosition().x > 38f){
            playscreen.setRunning(Playscreen.State.LEVEL_FINISHED);
        }

        if (statetimer == 60){
            frame = 4;
        } else if (statetimer > 60) {

            if (b2body.getLinearVelocity().x != 0 && b2body.getLinearVelocity().y == 0 &&
                    (Gdx.input.isKeyPressed(Input.Keys.D) || Gdx.input.isKeyPressed(Input.Keys.A) || Gdx.input.isTouched() || playscreen.gamestat == Playscreen.State.LEVEL_FINISHED)) {

                rflipX = false;

                framecount++;

                if ((framecount % animation_speed) == 0) {
                    frame++;

                    if (frame > 9) {
                        frame = 4;
                    }
                }

                setRegion(regions[0][frame]);

                if (b2body.getLinearVelocity().x < 0) {
                    rflipX = true;
                }

            } else if (b2body.getLinearVelocity().x != 0 && b2body.getLinearVelocity().y != 0){
                rflipX = false;

                setRegion(regions[0][8]);

                if (b2body.getLinearVelocity().x < -0.1) {
                    rflipX = true;
                }
            } else {
                setRegion(regions[0][1]);
            }

        } else {

            if ((statetimer & 6) == 0){
                frame ++;
                if (frame > 16){
                    frame = 14;
                }
                setRegion(regions[0][frame]);
            }

        }

        setFlip(rflipX, rflipY);

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

    public void jump(){
        if (!isJumping){
            //3.4
            b2body.applyLinearImpulse(new Vector2(0, 4.0f), b2body.getWorldCenter(), true);
            isJumping = true;
        }
    }

    public void boostedjump(){
        playscreen.getPlayer().b2body.applyLinearImpulse(
                new Vector2(0,b2body.getLinearVelocity().y*(-2.6f)) ,b2body.getWorldCenter(), true);
    }

    public void jumped(){
        if (b2body.getLinearVelocity().y < -2f) {
            b2body.setLinearVelocity(b2body.getLinearVelocity().x, 0);
            System.out.println("Velocity x+ fix");
        }
        isJumping = false;
    }

    public void collectGem(){
        gems++;
    }

    public int getGems(){
        return gems;
    }

    public void looselife(){
        if (statetimer > 60) {
            statetimer = 0;
            playscreen.getprefManager().saveIntegerValue(PrefManager.PREF_LIFE_TOTAL,
                    playscreen.getprefManager().getInteger(PrefManager.PREF_LIFE_TOTAL, 2)-1);
            frame = 14;

            jumped();
        }
    }

    public void addLife(){
        playscreen.getprefManager().saveIntegerValue(PrefManager.PREF_LIFE_TOTAL, playscreen.getprefManager().getInteger(PrefManager.PREF_LIFE_TOTAL, 2)+1);
    }


    @Override
    public void dispose() {
        texture.dispose();
    }
}
