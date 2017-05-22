package com.example.rens.competitive_gol.Model;

public class Player {
    /*******************VARIABLES*******************/
    private int color;
    private int team;

    /*******************CONSTRUCTORS*******************/
    public Player(int team, int color){
        if(team==Tile.DEAD) throw new RuntimeException("Gereserveerd voor dood");
        this.color = color;
        this.team = team;
    }

    /*******************FUNCTIONS*******************/
    public int getColor() { return color; }
    public int getTeam()  { return team;  }
}
