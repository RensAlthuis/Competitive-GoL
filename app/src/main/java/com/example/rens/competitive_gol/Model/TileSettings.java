package com.example.rens.competitive_gol.Model;

public class TileSettings {

    public final int minLife;
    public final int maxLife;
    public final int minSurvive;
    public final int maxSurvive;
    public final int defaultHealth;

    // TODO: het kunnen verranderen van de tilesettings.

    public TileSettings(int minLife, int maxLife, int minSurvive, int maxSurvive, int defaultHealth){
        this.minLife        = minLife;
        this.maxLife        = maxLife;
        this.minSurvive     = minSurvive;
        this.maxSurvive     = maxSurvive;
        this.defaultHealth  = defaultHealth;
    }

    public TileSettings() {
        minLife = 3;
        maxLife = 3;
        minSurvive = 2;
        maxSurvive = 3;
        defaultHealth = 1;
    }
}
