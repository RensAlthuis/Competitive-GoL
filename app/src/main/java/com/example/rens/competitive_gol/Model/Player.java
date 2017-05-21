package com.example.rens.competitive_gol.Model;

public class Player {
    private int color;
    private int team;

    public Player(int color){
        this.color = color;
    }

    public Player(int team, int color){
        this.color = color;
        this.team = team;
    }

    public int getColor() { return color; }
    public int getTeam()  { return team;  }

    public Player copy(int team){
        return new Player(color,team);
    }
}
