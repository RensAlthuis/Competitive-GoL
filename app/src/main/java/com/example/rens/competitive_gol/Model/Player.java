package com.example.rens.competitive_gol.Model;

public class Player {
    private int color;
    private int team;

    public Player(int team, int color){
        if(team==0) throw new RuntimeException("Gereserveerd voor dood");
        this.color = color;
        this.team = team;
    }

    public int getColor() { return color; }
    public int getTeam()  { return team;  }
}
