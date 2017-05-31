package com.example.rens.competitive_gol.Model;

public class TileSettings {

    public final int minLife;
    public final int maxLife;
    public final int minSurvive;
    public final int maxSurvive;
    public final int defaultHealth;

    public TileSettings(){
        minLife = 3;
        maxLife = 3;
        minSurvive = 2;
        maxSurvive = 3;
        defaultHealth = 1;
    }
}
