package me.nroffler.main;

public class Statics {

    //Bilder pro Sekunde (Wie oft render() aufgerufen wird)
    public static final int FRAMES_PER_SECOND = 60;

    //Die Größe des Fensters (Desktop)
    public static final int WIDTH = 1200;
    public static final int HEIGHT = 624;

    //Die Größe in der das Spiel berechnet wird
    public static final int V_WIDTH = 400;
    public static final int V_HEIGHT = 208;

    //Pixel pro Meter System
    public static final float PPM = 100;

    //Spielwerte - wird am Anfang geladen
    public static int unlock_level = -1;
    public static int lifes = -1;
    public static int coins = -1;
    public static int current_world = 1;

    //Einstellungen - wird am Anfang geladen
    public static boolean hud = false;
    public static boolean musik = false;
    public static boolean sounds = false;
    public static boolean effects = false;
}
