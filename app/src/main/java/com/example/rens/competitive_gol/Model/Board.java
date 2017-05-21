package com.example.rens.competitive_gol.Model;

import java.util.ArrayList;

public class Board {

    private ArrayList<Tile> tiles;
    private ArrayList<Tile> tilesNext;

    public final int width;
    public final int height;

    private final TileSettings settings; // update settings
    private static final int DEAD = 0;

    public Board(int width, int height, TileSettings settings){
        this.width = width;
        this.height = height;
        this.settings = settings;
        tiles = new ArrayList<>();
        tilesNext = new ArrayList<>();
        createEmptyTiles(width,height);
    }

    public boolean isDead(int x, int y){
        return tiles.get(y*width + x).team == DEAD;
    }

    public void setTilePlayer(int x, int y, int team){
        tiles.get(y*width + x).team = team;
    }
    public void setTileDead(int x, int y) { tiles.get(y*width + x).team = DEAD; }

    public int getTileTeam(int x, int y){
        return tiles.get(y*width + x).team;
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
                if(x+i >= 0 && x+i < width && y+j >= 0 && y+j < height && !(i==0 && j==0)) n.add(tiles.get((y+j)*width + (x+i)));
            }
        }
        return n;
    }
}
