package me.nroffler.spawnable;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Disposable;

import me.nroffler.Screens.Playscreen;
import me.nroffler.main.Statics;

//Oberklasse für alle erschaffbaren Objekte
//implementiert Disposable, da jedes Objekt in der Unterklasse mindestens eine Textur besitzen muss, die am Ende wieder aus dem Grafikspeicher raus muss
public abstract class SpawnAble extends Sprite implements Disposable {

    protected Playscreen playscreen;
    protected World world;
    protected boolean toDestroyed;
    protected boolean destroyed;
    protected Body body;
    protected int item_meta;

    public SpawnAble(Playscreen playscreen, float x, float y, int item_meta){
        this.playscreen = playscreen;
        this.world = playscreen.getWorld();
        this.item_meta = item_meta;

        //Position setzen (von Sprite)
        setPosition(x, y);
        //Breite und Höhe setzen (von Sprite)
        setBounds(getX(), getY(), 16 / Statics.PPM, 16 / Statics.PPM);

        defineSpawnAble();

        toDestroyed = false;
        destroyed = false;
    }

    public void update(float dt){
        //Körper zerstören
        if (toDestroyed && !destroyed){
            destroyed = true;
            if (body != null) {  //Sicherheit
                world.destroyBody(body); //TODO BREAKPOINT, improve
                body = null;
            }
        }
    }

    //nur zeichnen wenn nicht zerstört
    public void draw(Batch batch){
        if (!destroyed){
            super.draw(batch); //Sprite)
        }
    }

    //jedes Ojekt definiert den Körper selbst
    public abstract void defineSpawnAble();
    //jedes Objekt wird anders benutzt
    public abstract void use();
    //Objekt zerstören
    public void destroy(){
        toDestroyed = true;
    }

}
