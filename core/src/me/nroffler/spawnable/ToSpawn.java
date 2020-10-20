package me.nroffler.spawnable;

import com.badlogic.gdx.math.Vector2;

public class ToSpawn {

    //Position an der das Objekt erschaffen werden soll
    public Vector2 position;
    //Der Typ [Coin | Life | DynamicCoin]
    public Class<?> type;
    //Meta (zusätzliche) Informationen //TODO upgrade to ArrayList to support n meta values (for more than one world)
    public int item_meta;
    //TODO public int type_meta;

    //Werte über Konstruktor initialisieren
    public ToSpawn(Vector2 position, Class<?> type, int item_meta){
        this.position = position;
        this.type = type;
        this.item_meta = item_meta;
    }

}
