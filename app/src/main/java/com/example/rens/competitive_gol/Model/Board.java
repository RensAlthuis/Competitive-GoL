package com.example.rens.competitive_gol.Model;

import java.util.ArrayList;

public class Board {

    private ArrayList<Tile> tiles;
    private ArrayList<Tile> tilesNext;
    public final int width;
    public final int height;
    public static final int DEAD = 0;

    private final TileSettings settings;

    public Board(int width, int height, TileSettings settings){
        this.width = width;
        this.height = height;
        this.settings = settings;
        tiles = new ArrayList<>();
        tilesNext = new ArrayList<>();
        createEmptyTiles(width,height);
    }

    public void setTeam(int x, int y, int team){
        getTileAt(x,y).team = team;
    }

    public int getTeam(int x, int y){
        return getTileAt(x,y).team;
    }

    private void createEmptyTiles(int width, int height){
        for(int i=0; i<height*width; i++){
            tiles.add(new Tile(DEAD));
        }
    }

    public void update(){
        for(int i=0; i<height*width; i++){
            tilesNext.add(i, tiles.get(i).update(getNeighbours(i%width, i/height),settings));
        }

        tiles =  tilesNext;
        tilesNext = new ArrayList<>();
    }

    private ArrayList<Tile> getNeighbours(int x, int y){
        ArrayList<Tile> n = new ArrayList<>();
        for(int i = -1; i <= 1; i++){
            for(int j = -1; j <= 1; j++){
                if(x+i >= 0 && x+i < width && y+j >= 0 && y+j < height && !(i==0 && j==0)) n.add(getTileAt(x+i,y+j));
            }
        }
        return n;
    }

    private Tile getTileAt(int x, int y){
        return tiles.get(y*width + x);
    }

    // TODO: deze print functie is ook tijdelijk. verwijder bij het inleveren!
    public void print(){
        for(int i=0; i<height; i++){
            for(int j=0; j<width; j++)
                System.out.print(" " + getTileAt(j,i).team + " ");
            System.out.println();
        }
    }
}
