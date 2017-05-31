package com.example.rens.competitive_gol.Model;

/**
 * struct class to easily pass positions on the board.
 * Created by Glenn on 17/05/2017.
 */

public class Coordinate {
    public int x;
    public int y;

    public Coordinate(int x, int y){
        this.x = x;
        this.y = y;
    }

    public int size(){ return x*y; }
}
