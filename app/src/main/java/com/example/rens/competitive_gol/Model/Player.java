package com.example.rens.competitive_gol.Model;

import android.os.CountDownTimer;
import android.widget.TextView;

/**
 * This class contains the essential information about a Player.
 */
public class Player {

    /*******************VARIABLES*******************/

    protected int color;
    protected int team;
    public long currentTime;
    /*******************CONSTRUCTORS*******************/

    public Player(int team, int color){
        if(team==Tile.DEAD) throw new RuntimeException("Gereserveerd voor dood");
        this.color = color;
        this.team = team;
        currentTime = 0;
    }

    /*******************FUNCTIONS*******************/

    public int getColor() { return color; }
    public int getTeam()  { return team;  }

}
