package com.example.rens.competitive_gol.Model;

/**
 * struct class to easily pass positions on the board.
 * Created by Glenn on 17/05/2017.
 *
 * Deze heeft de grootte van het bord als attribute. Mogelijk maakt dit dingen een stuk makkelijker
 * but what do i know hasjasjlafklaskl
 * - tom
 */

public class Position extends Coordinate {
    private final int width;
    private final int heigth;

    public Position(int x, int y, int width, int height){
        super(x,y);
        this.width  = width;
        this.heigth = height;
    }

    public void next(){
        if(x<width) x++;
        else if(y<heigth){
            x=0;
            y++;
        }
    }
}