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

public class MovingBlock extends MovingTiledObject {

    private float framecount = 0;
    private boolean reversed = false;
    private boolean cycled;

    public MovingBlock(Playscreen playscreen, Rectangle rect) {
        super(playscreen, rect, playscreen.getAssetManager().get("Sprite/moving"+Statics.current_world+".png", Texture.class));

        if (height > 16){
            cycled = true;
        } else {
            cycled = false;
        }
    }

    @Override
    protected void defineObject() {
        BodyDef bdef = new BodyDef();
        if (height > 16) {
            bdef.position.set(sprite.getX() + (sprite.getWidth() * 1.85f), sprite.getY() + 8 / Statics.PPM);
        } else {
            bdef.position.set(sprite.getX() + (sprite.getWidth() * 1.85f), sprite.getY() + 8 / Statics.PPM);
        }
        bdef.type = BodyDef.BodyType.KinematicBody;
        b2body = playscreen.getWorld().createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(sprite.getWidth()/2, sprite.getHeight() / 2);
        fdef.filter.categoryBits = BitFilter.MOVING_BIT;
        fdef.filter.maskBits = BitFilter.BRICK_BIT | BitFilter.ENEMY_BIT  | BitFilter.PLAYER_BIT | BitFilter.OBJEKT_BIT | BitFilter.DEFAULT_BIT;

        fdef.shape = shape;
        b2body.createFixture(fdef).setUserData(this);
    }

    @Override
    public void touch() {

    }

    @Override
    public void draw(SpriteBatch batch) {
        sprite.draw(batch);
    }

    @Override
    public void update(float dt) {

        if (!reversed){
            framecount += 0.012;
            if (framecount > 1.6){
                reversed = true;
            }

        } else if (reversed){
            framecount -= 0.012;
            if (framecount < 0.4){
                reversed = false;
                cycled = !cycled;
            }
        }

        if (cycled){
            b2body.setLinearVelocity(new Vector2(0.4f *framecount ,0));
        } else {
            b2body.setLinearVelocity(new Vector2(-0.4f *framecount ,0));
        }

        sprite.setPosition(b2body.getPosition().x - sprite.getWidth() / 2, b2body.getPosition().y - sprite.getHeight() / 2);
    }

    @Override
    public void dispose() {

    }
}
