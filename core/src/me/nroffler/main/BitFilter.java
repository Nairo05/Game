package me.nroffler.main;

public class BitFilter {

    //jeder Objektgruppe ist eine ID zugeordnet. Wird in MyContactListener.java und den Klassen der jeweiligen Objekten gebraucht.
    //Damit die IDs sicher übereinstimmen werden sie hier (global) gespeichert und nicht einzelnen in den jeweiligen Klassen
    public static final short DEFAULT_BIT = 1;
    public static final short PLAYER_BIT = 2;
    public static final short BRICK_BIT = 4;
    public static final short COIN_BIT = 8;
    public static final short DESTROYED_BIT = 16;
    public static final short ENEMY_BIT = 32;
    public static final short OBJEKT_BIT = 64;
    public static final short ENEMY_HEAD_BIT = 128;
    public static final short MOVING_BIT = 256;
    public static final short ENEMYTHING = 512;
    public static final short KILL_ENEMY_BIT = 1024;
    public static final short SPAWNABLE_BIT = 2048;

}
