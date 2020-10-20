package me.nroffler.B2World;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Rectangle;

import java.util.ArrayList;

import me.nroffler.Enemys.Dragon;
import me.nroffler.Enemys.Enemy;
import me.nroffler.Enemys.Slime;
import me.nroffler.Enemys.Something;
import me.nroffler.Screens.Playscreen;
import me.nroffler.main.Statics;

public class MapCreator {

    private Playscreen playscreen;
    private ArrayList<Enemy> enemys;
    private ArrayList<Coin> coins;
    private ArrayList<MovingTiledObject> movingBlocks;

    public MapCreator(Playscreen playscreen){
        this.playscreen = playscreen;

        enemys = new ArrayList<>();
        coins = new ArrayList<>();
        movingBlocks = new ArrayList<>();

        for (MapObject object : playscreen.getMap().getLayers().get(2).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rect = ((RectangleMapObject) object).getRectangle();

            if (Statics.current_world == 1) {
                new Ground(playscreen, rect, 0.0f);
            }
        }

        for (MapObject object : playscreen.getMap().getLayers().get(3).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rect = ((RectangleMapObject) object).getRectangle();

            new Brick(playscreen, rect, 0.01f);
        }


        for (MapObject object : playscreen.getMap().getLayers().get(4).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            if (Statics.current_world == 1) {
                if (rect.getWidth() == 32) {
                    enemys.add(new Slime(playscreen, rect.getX() / Statics.PPM, rect.getY() / Statics.PPM, true));
                } else if (rect.getWidth() == 16) {
                    enemys.add(new Slime(playscreen, rect.getX() / Statics.PPM, rect.getY() / Statics.PPM, false));
                }
            }
        }



        for (MapObject object : playscreen.getMap().getLayers().get(5).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rect = ((RectangleMapObject) object).getRectangle();

            if (Statics.current_world == 1) {
                enemys.add(new Dragon(playscreen, rect.getX() / Statics.PPM, rect.getY() / Statics.PPM, false));
            }
        }

        for (MapObject object : playscreen.getMap().getLayers().get(6).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rect = ((RectangleMapObject) object).getRectangle();

            if (Statics.current_world == 1) {
                enemys.add(new Something(playscreen, rect.getX() / Statics.PPM, rect.getY() / Statics.PPM, false));
            }
        }


        for (MapObject object : playscreen.getMap().getLayers().get(7).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rect = ((RectangleMapObject) object).getRectangle();

            if (Statics.current_world == 1) {
                coins.add(new Coin(rect, playscreen));
            }
        }

        for (MapObject object : playscreen.getMap().getLayers().get(9).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rect = ((RectangleMapObject) object).getRectangle();

            if (Statics.current_world == 1) {
                movingBlocks.add(new FallingBlock(playscreen, rect));
            }
        }

        for (MapObject object : playscreen.getMap().getLayers().get(8).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rect = ((RectangleMapObject) object).getRectangle();

            if (Statics.current_world == 1) {
                movingBlocks.add(new MovingBlock(playscreen, rect));
            }
        }

        for (MapObject object : playscreen.getMap().getLayers().get(10).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rect = ((RectangleMapObject) object).getRectangle();

            if (Statics.current_world == 1) {
                new Special(playscreen, rect, 0.0f);
            }
        }

        for (MapObject object : playscreen.getMap().getLayers().get(11).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rect = ((RectangleMapObject) object).getRectangle();

            if (Statics.current_world == 1) {
                new Question(playscreen, rect, 0.01f);
                System.out.println("?");
            }
        }

        for(MapObject object : playscreen.getMap().getLayers().get(12).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rect = ((RectangleMapObject) object).getRectangle();

            if (Statics.current_world == 1){
                new InvisibleBrick(playscreen, rect, 0);
            }
        }
    }

    public ArrayList<Enemy> getEnemys() {
        return enemys;
    }

    public ArrayList<Coin> getCoins() {
        return coins;
    }

    public ArrayList<MovingTiledObject> getMovingBlocks() {
        return movingBlocks;
    }
}
