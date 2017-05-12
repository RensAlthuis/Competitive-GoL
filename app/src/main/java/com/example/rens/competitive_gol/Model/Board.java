package com.example.rens.competitive_gol.Model;

import java.util.ArrayList;

/**
 * Created by Tom on 12-5-2017.
 */

public class Board {

    private ArrayList<Tile> tiles;
    private ArrayList<Tile> tilesNext;
    private final int width;
    private final int height;

    private final TileSettings settings;


    public Board(int width, int height, TileSettings settings){

        this.width = width;
        this.height = height;
        this.settings = settings;
        tiles = new ArrayList<>();
        tilesNext = new ArrayList<>();
        createTiles(width,height);
    }

    public void setTile(int x, int y, int team){
        //getTileAt(x,y).
    }

    private void createTiles(int width, int height){
        for(int i=0; i<height*width; i++) {
            tiles.add(new Tile(1));
            tilesNext.add(new Tile(1));
        }
    }

    public void update(){
        for(int i=0; i<height*width; i++){
            tilesNext.set(i, tiles.get(i).update(getNeighbours(i%width, i/height),settings));
        }

        tiles =  tilesNext;
        tilesNext = new ArrayList<>();
    }

    private ArrayList<Tile> getNeighbours(int x, int y){
        ArrayList<Tile> n = new ArrayList<>();
        for(int i = -1; i <= 1; i++){
            for(int j = -1; j <= 1; j++){
                if(x+i >= 0 &&
                   x+i < width &&
                   y+j >= 0 &&
                   y+j < height &&
                   !(i==0 && j==0))
                {
                    n.add(getTileAt(x+i,y+j));
                }

            }
        }
        return n;
    }

    private Tile getTileAt(int x, int y){
        return tiles.get(y*width + x);
    }

    public void print(){
        for(int i=0; i<height; i++){
            for(int j=0; j<width; j++)
                System.out.print(" " + getTileAt(j,i).getTeam() + " ");
            System.out.println();
        }
    }

}
