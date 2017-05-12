package com.example.rens.competitive_gol.Model;

import java.util.HashMap;

/**
 * Created by Tom on 12-5-2017.
 */

public class TileSettings {

    //TODO besluit of dit beter is.
    //private final HashMap<String,Integer> settings;

    public final int minLife;
    public final int maxLife;
    public final int minSurvive;
    public final int maxSurvive;

    public TileSettings(){
        minLife = 3;
        maxLife = 3;
        minSurvive = 2;
        maxSurvive = 3;
    }
}
