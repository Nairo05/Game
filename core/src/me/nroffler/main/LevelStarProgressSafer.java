package me.nroffler.main;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

public class LevelStarProgressSafer {

    //Mit dieser Klasse wird nur der Fortschirtt gespeichert (Also wie viele Sterne man in welchem Level erreicht hat).
    private Preferences preferences;

    //Über diesen String erreicht man die Daten
    public static final String PREF_ACCESS_STRING = "PRE_JUMPANDRUN_LEVELPROGRESS";

    public LevelStarProgressSafer(){
        //Sicherheitshalber das Objekt preferences initialiseren.
        getPrefs();
    }

    //Über diesen String erreicht man die Daten
    private Preferences getPrefs() {
        if (preferences == null)
            preferences = Gdx.app.getPreferences(PREF_ACCESS_STRING);
        return preferences;
    }

    //Nach jedem Schreibvorgang ausführen, um Änderungen zu speichern
    private void writeData(){
        getPrefs().flush();
    }

    //Mit dem String name wird das Level übergeben, mit dem int value die Anzahl der Sterne die erreicht wurden.
    public void wirteLeveldata(String name, int value){
        String ACCESS_STRING = name+"";
        getPrefs().putInteger(ACCESS_STRING, value);
        writeData();
    }

    //Die Sterne die im Level erreicht wurden auslesen, wenn nicht vorhanden def[-ault]Value also 0 zurückgeben
    public int getLevelData(String name){
        String ACCESS_STRING = name+"";
        return getPrefs().getInteger(ACCESS_STRING, 0);
    }

    //Für alle Level dn Wert auf 0 also nicht abgeschlossen setzen
    public void resetData() {
        for (int i = 0; i < 100; i++) {
            wirteLeveldata(i + "", 0);
        }
    }
}
