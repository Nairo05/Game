package me.nroffler.main;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

public class PrefManager {

    private Preferences preferences;

    public static final String PREF_ACCESS_STRING = "PRE_JUMPANDRUN_MAIN";

    public static final String PREF_MUSIC_ENABLED = "pref_music_boolean";
    public static final String PREF_SOUND_ENABLED = "pref_sound_boolean";
    public static final String PREF_EFFEKT_ENABLED = "pref_effekt_boolean";
    public static final String PREF_HUD_ENABLED = "pref_hud_boolean";

    public static final String PREF_COINS_TOTAL = "pref_coins_int";
    public static final String PREF_UNLOCKLEVEL = "pref_level_int";
    public static final String PREF_LIFE_TOTAL = "pref_life_int";

    public PrefManager(){
        getPrefs();

        //Wipe
        overwrite();
    }

    private void overwrite(){
        saveIntegerValue(PREF_LIFE_TOTAL, 2);
        saveIntegerValue(PREF_UNLOCKLEVEL, 1);
        writeData();
    }

    private Preferences getPrefs() {
        if (preferences == null)
            preferences = Gdx.app.getPreferences(PREF_ACCESS_STRING);
        return preferences;
    }

    public void writeData(){
        getPrefs().flush();
        System.out.println("Prefs.flush");
    }

    public void saveBooleanState(String key, boolean state){
        getPrefs().putBoolean(key, state);
        writeData();
    }

    public void saveIntegerValue(String key, int state){
        getPrefs().putInteger(key, state);
        writeData();
    }

    public int getInteger(String key, int defaultvalue){
        return (getPrefs().getInteger(key , defaultvalue));
    }

    public boolean getBoolean(String key){
        return (getPrefs().getBoolean(key, true));
    }

    public void resetData() {
        saveIntegerValue(PREF_LIFE_TOTAL, 2);
        saveIntegerValue(PREF_UNLOCKLEVEL, 1);
        saveIntegerValue(PREF_COINS_TOTAL, 0);
    }
}
