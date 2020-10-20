package me.nroffler.B2World;

import com.badlogic.gdx.math.Rectangle;

import me.nroffler.Screens.Playscreen;
import me.nroffler.main.BitFilter;

public class Special extends InteractiveTileObject{

    private Playscreen playscreen;

    public Special(Playscreen playscreen, Rectangle bounds, float bouncy) {
        super(playscreen, bounds, bouncy);
        this.playscreen = playscreen;

        setCategorieFilter(BitFilter.OBJEKT_BIT);
        setSensor();
    }

    @Override
    public void onHeadHit() {

    }

    @Override
    public void onBodyHit() {

    }

    @Override
    public void onFootHit() {
        playscreen.getPlayer().boostedjump();
    }
}
