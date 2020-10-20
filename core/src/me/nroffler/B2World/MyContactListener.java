package me.nroffler.B2World;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;

import me.nroffler.Enemys.Enemy;
import me.nroffler.Screens.Playscreen;
import me.nroffler.main.BitFilter;
import me.nroffler.spawnable.SpawnAble;

public class MyContactListener implements ContactListener {

    private Playscreen playscreen;
    private int playerisongorund;

    public MyContactListener(Playscreen playscreen){
        this.playscreen = playscreen;
    }

    @Override
    public void beginContact(Contact contact) {
        Fixture fixA = contact.getFixtureA();
        Fixture fixB = contact.getFixtureB();

        if ((fixA.getFilterData().categoryBits == BitFilter.DEFAULT_BIT || fixA.getFilterData().categoryBits == BitFilter.BRICK_BIT || fixA.getFilterData().categoryBits == BitFilter.MOVING_BIT)
                && (fixB.getUserData() == "foot")){
            playerisongorund++;
            playscreen.getPlayer().jumped();
        } else if ((fixB.getFilterData().categoryBits == BitFilter.DEFAULT_BIT || fixB.getFilterData().categoryBits == BitFilter.BRICK_BIT || fixB.getFilterData().categoryBits == BitFilter.MOVING_BIT)
                && (fixA.getUserData() == "foot")){
            playerisongorund++;
            playscreen.getPlayer().jumped();
        }

        if (fixA.getUserData() == "head" || fixB.getUserData() == "head"){
            Fixture head = fixA.getUserData() == "head" ? fixA : fixB;
            Fixture object = head == fixA ? fixB : fixA;

            if (object.getUserData() != null && InteractiveTileObject.class.isAssignableFrom(object.getUserData().getClass())){
                ((InteractiveTileObject) object.getUserData()).onHeadHit();
                System.out.println("hit");
            }
        }

        if ((fixA.getFilterData().categoryBits == BitFilter.ENEMY_HEAD_BIT && fixB.getFilterData().categoryBits == BitFilter.PLAYER_BIT)){
            ((Enemy) fixA.getUserData()).hitOnHead();
        } else if (fixB.getFilterData().categoryBits == BitFilter.ENEMY_HEAD_BIT && fixA.getFilterData().categoryBits == BitFilter.PLAYER_BIT){
            ((Enemy) fixB.getUserData()).hitOnHead();
        }

        if (fixA.getUserData() == "foot" && fixB.getFilterData().categoryBits == BitFilter.OBJEKT_BIT){
            playscreen.getPlayer().boostedjump();
        } else if (fixB.getUserData() == "foot" && fixA.getFilterData().categoryBits == BitFilter.OBJEKT_BIT){
            playscreen.getPlayer().boostedjump();
        }


        if (fixA.getFilterData().categoryBits == BitFilter.MOVING_BIT && fixB.getUserData() == "foot"){
            ((MovingTiledObject) fixA.getUserData()).touch();
        } else if (fixB.getFilterData().categoryBits == BitFilter.MOVING_BIT && fixA.getUserData() == "foot"){
            ((MovingTiledObject) fixB.getUserData()).touch();
        }

        if (fixA.getFilterData().categoryBits == BitFilter.ENEMY_BIT && (fixB.getFilterData().categoryBits == BitFilter.BRICK_BIT)){
            ((Enemy) fixA.getUserData()).reverseVelocity(true, false);
        } else if (fixB.getFilterData().categoryBits == BitFilter.ENEMY_BIT && (fixA.getFilterData().categoryBits == BitFilter.BRICK_BIT)){
            ((Enemy) fixB.getUserData()).reverseVelocity(true, false);
        }

        if (fixA.getFilterData().categoryBits == BitFilter.ENEMY_BIT && fixB.getFilterData().categoryBits == BitFilter.ENEMY_BIT){
            ((Enemy) fixA.getUserData()).reverseVelocity(true, false);
            ((Enemy) fixB.getUserData()).reverseVelocity(true, false);
        }

        if (fixA.getFilterData().categoryBits == BitFilter.PLAYER_BIT && fixB.getFilterData().categoryBits == BitFilter.COIN_BIT){
            ((Coin) fixB.getUserData()).onHit();
        } else if (fixB.getFilterData().categoryBits == BitFilter.PLAYER_BIT && fixB.getFilterData().categoryBits == BitFilter.COIN_BIT){
            ((Coin) fixA.getUserData()).onHit();
        }

        if (fixA.getFilterData().categoryBits == BitFilter.PLAYER_BIT && fixB.getFilterData().categoryBits == BitFilter.ENEMY_BIT){
            playscreen.getPlayer().looselife();
            ((Enemy) fixB.getUserData()).reverseVelocity(true, false);
        } else if (fixB.getFilterData().categoryBits == BitFilter.PLAYER_BIT && fixA.getFilterData().categoryBits == BitFilter.ENEMY_BIT){
            playscreen.getPlayer().looselife();
            ((Enemy) fixA.getUserData()).reverseVelocity(true, false);
        }

        if (fixA.getFilterData().categoryBits == BitFilter.PLAYER_BIT && fixB.getFilterData().categoryBits == BitFilter.SPAWNABLE_BIT){
            ((SpawnAble) fixB.getUserData()).use();
        } else if (fixB.getFilterData().categoryBits == BitFilter.PLAYER_BIT && fixA.getFilterData().categoryBits == BitFilter.SPAWNABLE_BIT){
            ((SpawnAble) fixA.getUserData()).use();
        }

        if (fixA.getFilterData().categoryBits == BitFilter.PLAYER_BIT && fixB.getFilterData().categoryBits == BitFilter.KILL_ENEMY_BIT){
            playscreen.getPlayer().looselife();
        } else if (fixB.getFilterData().categoryBits == BitFilter.PLAYER_BIT && fixA.getFilterData().categoryBits == BitFilter.KILL_ENEMY_BIT){
            playscreen.getPlayer().looselife();
        }
    }

    @Override
    public void endContact(Contact contact) {
        Fixture fixA = contact.getFixtureA();
        Fixture fixB = contact.getFixtureB();

        if ((fixA.getFilterData().categoryBits == BitFilter.DEFAULT_BIT || fixA.getFilterData().categoryBits == BitFilter.BRICK_BIT || fixA.getFilterData().categoryBits == BitFilter.MOVING_BIT)
                && (fixB.getUserData() == "foot")){
            playerisongorund--;
        } else if ((fixB.getFilterData().categoryBits == BitFilter.DEFAULT_BIT || fixB.getFilterData().categoryBits == BitFilter.BRICK_BIT || fixB.getFilterData().categoryBits == BitFilter.MOVING_BIT)
                && (fixA.getUserData() == "foot")){
            playerisongorund--;
        }
    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }

    public boolean PlayerIsOnGorund(){
        System.out.println(playerisongorund);
        return playerisongorund > 0;
    }
}
