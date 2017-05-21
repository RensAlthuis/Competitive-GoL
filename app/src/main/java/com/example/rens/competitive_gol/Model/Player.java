package com.example.rens.competitive_gol.Model;

import android.graphics.Color;

/**
 * Created by Rens on 18-5-2017.
 */

public class Player {
    private int color;
    private int teamNumber;

    public Player(int teamNumber, int color){
        this.color = color;
        this.teamNumber = teamNumber;

    }

    public int getColor() { return color; }

    public int getTeamNumber() { return teamNumber; }
}
