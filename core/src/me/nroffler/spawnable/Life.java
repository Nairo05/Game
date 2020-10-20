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
import me.nroffler.main.PrefManager;
import me.nroffler.main.Statics;

public class Life extends SpawnAble {

    private Texture texture;
    private TextureRegion textureRegion[][];
    private int state;

    public Life(Playscreen playscreen, float x, float y, int meta) {
        super(playscreen, x, y, meta);

        //Textur holen
        texture = playscreen.getAssetManager().get("Sprite/hearts16x16.png", Texture.class);
        //Textur in 16x16 Texturen spalten
        textureRegion = TextureRegion.split(texture, 16, 16);

        if (playscreen.getprefManager().getInteger(PrefManager.PREF_LIFE_TOTAL, 2 ) < 3) {
            setRegion(textureRegion[0][0]); //rot
            state = 0;
        } else {
            setRegion(textureRegion[1][0]); //grün
            state = 1;
        }
    }

    @Override
    public void defineSpawnAble() {
        BodyDef bdef = new BodyDef();
        bdef.type = BodyDef.BodyType.DynamicBody;
        bdef.position.set(getX() + getWidth() / 2, getY() + getHeight() * 1.5f);
        if (body == null && !world.isLocked()) {
            body = world.createBody(bdef);
        }

        //Hitbox - wie der Coin mit der Welt interagiert
        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(6 / Statics.PPM);
        fdef.shape = shape;
        fdef.filter.categoryBits = BitFilter.SPAWNABLE_BIT;
        fdef.restitution = 0.3f;
        fdef.filter.maskBits = BitFilter.BRICK_BIT | BitFilter.DEFAULT_BIT  | BitFilter.OBJEKT_BIT | BitFilter.MOVING_BIT | BitFilter.SPAWNABLE_BIT | BitFilter.PLAYER_BIT;
        body.createFixture(fdef).setUserData(this);

        //Hitbox - wie der Coin mit dem Spieler interagiert (damit der Spieler nicht stockt wenn er gegen einen Coin läuft)
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

        //-----------------------------------------------------------------------------------------
        //Der Coin "fliegt aus dem Block raus"
        float ximpuls = new Random().nextInt(2);
        if (ximpuls == 0){
            ximpuls = -0.5f;
        } else {
            ximpuls = 0.5f;
        }

        body.applyLinearImpulse(new Vector2(ximpuls, 2.5f), body.getWorldCenter(), true);
        //-----------------------------------------------------------------------------------------
    }

    @Override
    public void use() {
        //Spieler ein Leben geben und Körper zerstören
        if (state == 0 && playscreen.getprefManager().getInteger(PrefManager.PREF_LIFE_TOTAL, 2) <= 2){
            playscreen.getPlayer().addLife();
            if (!destroyed && !toDestroyed) {
                if (body != null) {
                    destroy();
                }
            }
        } else if (state == 1 && playscreen.getprefManager().getInteger(PrefManager.PREF_LIFE_TOTAL, 2) >= 3
                && playscreen.getprefManager().getInteger(PrefManager.PREF_LIFE_TOTAL, 2) <= 6){
            playscreen.getPlayer().addLife();
            if (!destroyed && !toDestroyed) {
                if (body != null) {
                    destroy();
                }
            }
        }
    }

    @Override
    public void update(float dt) {

        super.update(dt);

        if (!destroyed) {
            setPosition(body.getPosition().x - getWidth() / 2, body.getPosition().y - getHeight() / 2);
        }
    }

    @Override
    public void draw(Batch batch) {
        if (!destroyed) {
            super.draw(batch);
        }
    }

    @Override
    public void dispose() {
        texture.dispose();
    }
}
