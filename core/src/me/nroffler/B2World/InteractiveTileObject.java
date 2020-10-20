package me.nroffler.B2World;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

import me.nroffler.Screens.Playscreen;

import static me.nroffler.main.Statics.PPM;

public abstract class InteractiveTileObject {

    private World world;
    private TiledMap map;
    private TiledMapTile tile;
    private Rectangle bounds;
    private Body body;

    protected Playscreen playscreen;
    protected Fixture fixture;
    protected Rectangle rectangle;

    public InteractiveTileObject(Playscreen playscreen, Rectangle bounds, float bouncy){
        this.playscreen = playscreen;
        this.world = playscreen.getWorld();
        this.map = playscreen.getMap();
        this.rectangle = bounds;

        this.bounds = bounds;

        BodyDef bdef = new BodyDef();

        bdef.type = BodyDef.BodyType.StaticBody;
        bdef.position.set((bounds.getX() + bounds.getWidth() / 2) / PPM, (bounds.getY() + bounds.getHeight() /2)/PPM);

        body = playscreen.getWorld().createBody(bdef);

        FixtureDef fdef  = new FixtureDef();

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(bounds.getWidth() / 2 / PPM, bounds.getHeight() / 2 / PPM);
        fdef.shape = shape;
        fdef.restitution = bouncy;

        fixture = body.createFixture(fdef);

    }

    public abstract void onBodyHit();
    public abstract void onHeadHit();
    public abstract void onFootHit();

    public void setSensor(){
        fixture.setSensor(true);
    }

    public void setCategorieFilter(short filterBit){
        Filter filter = new Filter();
        filter.categoryBits = filterBit;
        fixture.setFilterData(filter);
    }

    public TiledMapTileLayer.Cell getCell(){
        TiledMapTileLayer lay = (TiledMapTileLayer) map.getLayers().get(1);
        return lay.getCell((int)(body.getPosition().x * PPM / 16),
                (int)(body.getPosition().y * PPM / 16));
    }
}
