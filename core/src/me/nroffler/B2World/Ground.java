package me.nroffler.B2World;

import com.badlogic.gdx.math.Rectangle;

import me.nroffler.Screens.Playscreen;
import me.nroffler.main.BitFilter;

public class Ground extends InteractiveTileObject{

    public Ground(Playscreen playscreen, Rectangle bounds, float bouncy) {
        super(playscreen, bounds, bouncy);
        fixture.setUserData(this);
        setCategorieFilter(BitFilter.DEFAULT_BIT);
    }

    @Override
    public void onHeadHit() {

    }

    @Override
    public void onBodyHit() {

    }

    @Override
    public void onFootHit() {

    }
}
