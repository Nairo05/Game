package me.nroffler.B2World;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import java.util.Random;

import me.nroffler.Screens.Playscreen;
import me.nroffler.main.BitFilter;
import me.nroffler.main.Statics;
import me.nroffler.spawnable.Coins;
import me.nroffler.spawnable.DynamicCoin;
import me.nroffler.spawnable.Life;
import me.nroffler.spawnable.ToSpawn;

public class Question extends InteractiveTileObject {

    //da der Spieler den Block länger als eine Sekunde berühren kann, aber nur einmalig was erschaffen werden darf
    private boolean spawned = false;

    public Question(Playscreen playscreen, Rectangle bounds, float bouncy) {
        super(playscreen, bounds, bouncy);
        fixture.setUserData(this);
        setCategorieFilter(BitFilter.BRICK_BIT);
    }

    @Override
    public void onHeadHit() {
        if (getCell() != null && !spawned) {
            getCell().setTile(null);

            int spawning = new Random().nextInt(12);
            System.out.println(spawning);

                if (spawning == 0 || spawning == 1 || spawning == 2) {
                    playscreen.spawn(new ToSpawn(new Vector2(rectangle.x / Statics.PPM, rectangle.y / Statics.PPM + 16 / Statics.PPM), Coins.class, 1));
                } else if (spawning == 3 ||spawning == 4) {
                    playscreen.spawn(new ToSpawn(new Vector2(rectangle.x / Statics.PPM, rectangle.y / Statics.PPM + 16 / Statics.PPM), Coins.class, 1));
                    playscreen.spawn(new ToSpawn(new Vector2(rectangle.x / Statics.PPM, (rectangle.y + 16) / Statics.PPM + 16 / Statics.PPM), Coins.class, 2));
                } else if (spawning == 5 ||spawning == 6) {
                    playscreen.spawn(new ToSpawn(new Vector2(rectangle.x / Statics.PPM, rectangle.y / Statics.PPM + 16 / Statics.PPM), Coins.class, 1));
                    playscreen.spawn(new ToSpawn(new Vector2(rectangle.x / Statics.PPM, (rectangle.y + 16) / Statics.PPM + 16 / Statics.PPM), Coins.class, 2));
                    playscreen.spawn(new ToSpawn(new Vector2(rectangle.x / Statics.PPM, (rectangle.y + 32) / Statics.PPM + 16 / Statics.PPM), Coins.class, 3));
                } else if (spawning == 7 || spawning == 8) {
                    playscreen.spawn(new ToSpawn(new Vector2(rectangle.x / Statics.PPM, rectangle.y / Statics.PPM + 16 / Statics.PPM), Coins.class, 1));
                    playscreen.spawn(new ToSpawn(new Vector2(rectangle.x / Statics.PPM, (rectangle.y + 16) / Statics.PPM + 16 / Statics.PPM), Coins.class, 2));
                    playscreen.spawn(new ToSpawn(new Vector2(rectangle.x / Statics.PPM, (rectangle.y + 32) / Statics.PPM + 16 / Statics.PPM), Coins.class, 3));
                    playscreen.spawn(new ToSpawn(new Vector2(rectangle.x / Statics.PPM, (rectangle.y + 48) / Statics.PPM + 16 / Statics.PPM), Coins.class, 4));
                } else if (spawning == 9){
                    playscreen.spawn(new ToSpawn(new Vector2(rectangle.x / Statics.PPM, rectangle.y / Statics.PPM), Life.class, -1));
                } else if (spawning == 10 || spawning == 11){
                    int tospawn = 10 + new Random().nextInt(10);
                    System.out.println(tospawn);
                    for (int i = 0; i < tospawn ; i++) {
                        playscreen.spawn(new ToSpawn(new Vector2(rectangle.x / Statics.PPM, rectangle.y / Statics.PPM), DynamicCoin.class, i));
                    }

                }
                spawned = true;

        }
    }

    @Override
    public void onBodyHit() {

    }

    @Override
    public void onFootHit() {

    }
}
