package me.nroffler.B2World;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;

import me.nroffler.Screens.Playscreen;
import me.nroffler.main.BitFilter;
import me.nroffler.main.Statics;

public class FallingBlock extends MovingTiledObject {

    private float startY, startX;
    int wegcount = 0;

    public FallingBlock(Playscreen playscreen, Rectangle rect) {
        super(playscreen, rect, playscreen.getAssetManager().get("Sprite/moving"+ Statics.current_world +".png", Texture.class));
    }

    @Override
    protected void defineObject() {
        BodyDef bdef = new BodyDef();
        bdef.position.set(sprite.getX() +0.08f, sprite.getY());
        bdef.type = BodyDef.BodyType.KinematicBody;
        b2body = playscreen.getWorld().createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(sprite.getWidth()/2, sprite.getHeight() / 2);
        fdef.filter.categoryBits = BitFilter.MOVING_BIT;
        fdef.filter.maskBits = BitFilter.BRICK_BIT | BitFilter.ENEMY_BIT  | BitFilter.PLAYER_BIT | BitFilter.OBJEKT_BIT | BitFilter.DEFAULT_BIT;

        fdef.shape = shape;
        b2body.createFixture(fdef).setUserData(this);
        b2body.setGravityScale(0f);

        startY = b2body.getPosition().y;
        startX = b2body.getPosition().x;
    }

    @Override
    public void touch() {
        System.out.println("touched");
        b2body.setLinearVelocity(new Vector2(0, -0.3f));
    }

    @Override
    public void dispose() {

    }

    @Override
    public void update(float dt) {
        if (b2body.getLinearVelocity().y > -0.9f && b2body.getLinearVelocity().y != 0) {
            b2body.setLinearVelocity(new Vector2(b2body.getLinearVelocity().x, b2body.getLinearVelocity().y * 1.007f));
        }

        sprite.setPosition(b2body.getPosition().x - sprite.getWidth() / 2, b2body.getPosition().y - sprite.getHeight() / 2);

        if (b2body.getPosition().y < 0) {
            wegcount ++;
            if (wegcount > 120) {
                System.out.println("removed");
                b2body.setTransform(startX, startY, 0);
                b2body.setLinearVelocity(new Vector2(0, 0));
                wegcount = 0;
            }
        }
    }

    @Override
    public void draw(SpriteBatch batch){
        sprite.draw(batch);
    }
}
