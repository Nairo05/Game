package me.nroffler.B2World;

import com.badlogic.gdx.math.Rectangle;

import me.nroffler.Screens.Playscreen;
import me.nroffler.main.BitFilter;

public class InvisibleBrick extends InteractiveTileObject {

    public InvisibleBrick(Playscreen playscreen, Rectangle bounds, float bouncy) {
        super(playscreen, bounds, bouncy);
        fixture.setUserData(this);
        setCategorieFilter(BitFilter.BRICK_BIT);
    }

    @Override
    public void onHeadHit() {
        setCategorieFilter(BitFilter.DEFAULT_BIT);

        if (getCell() != null){
            getCell().setTile(null);
        }
    }

    @Override
    public void onBodyHit() {

    }

    @Override
    public void onFootHit() {

    }
}
