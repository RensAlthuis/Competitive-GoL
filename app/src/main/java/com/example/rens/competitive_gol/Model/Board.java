package com.example.rens.competitive_gol.Model;

import java.util.ArrayList;

/**
 * Created by Tom on 12-5-2017.
 */

public class Board {

    private final ArrayList<Tile> tiles;
    private final int width;
    private final int height;

    public Board(int width, int height){

        this.width = width;
        this.height = height;

        tiles = new ArrayList<>();
        createTiles(width,height);
    }

    private void createTiles(int width, int height){
        for(int i=0; i<height*width; i++)
            tiles.add(new Tile());
    }


}
