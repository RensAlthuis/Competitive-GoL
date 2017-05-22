package com.example.rens.competitive_gol.Model;

import java.util.ArrayList;
import java.util.Random;

public class Board {

    /*******************VARIABLES*******************/

    private ArrayList<Tile> tiles = new ArrayList<>();
    private ArrayList<Tile> tilesNext = new ArrayList<>();

    public final int width;
    public final int height;

    private final TileSettings settings; // update settings

    /*******************CONSTRUCTORS*******************/

    //TODO: een mooie contructor die andere tiles accepteerd

    public Board(int width, int height, TileSettings settings){
        this.width = width;
        this.height = height;
        this.settings = settings;
        createEmptyTiles();
    }

    /*******************FUNCTIONS*******************/

    //is the tile dead right now?
    public boolean isDead(int x, int y){
        return tiles.get(y*width + x).team == Tile.DEAD;
    }

    //will the tile be dead next turn?
    public boolean isDeadNext(int x, int y) { return tilesNext.get(y*width + x).team == Tile.DEAD; }

    //set tile x,y to a certain team
    public void setTileTeam(int x, int y, int team){
        tiles.set(y*width + x, new Tile(team,settings.defaultHealth));
    }

    //set tile x,y to dead
    public void setTileDead(int x, int y) { tiles.set(y*width + x, new Tile()); }

    //return the team for tile x,y
    public int getTileTeam(int x, int y){
        return tiles.get(y*width + x).team;
    }

    //TODO this is never any different from getTileTeam can probably be deleted
    public int getTileTeamNext(int x, int y){
        return tilesNext.get(y*width + x).team;
    }

    //create all the tiles for the board
    private void createEmptyTiles(){
        for(int i=0; i<height*width; i++){
            tiles.add(new Tile());
            tilesNext.add(new Tile());
        }
    }

    //fill the board in randomly
    public void createRandomBoard(int tilesPP, ArrayList<Player> allPlayers){
        Random rand = new Random();

        int tilesLeft[] = new int[allPlayers.size()];

        for(int i = 0; i < tilesLeft.length; i++){
            tilesLeft[i] = tilesPP;
            while(tilesLeft[i]!= 0) {
                int x;
                int y;
                do {
                    x = rand.nextInt(width);
                    y = rand.nextInt(height);
                } while (getTileTeam(x, y) != Tile.DEAD);
                setTileTeam(x, y, allPlayers.get(i).getTeam());
                tilesLeft[i]--;
            }

        }
        setNext();
    }
    /*******************UPDATE*******************/

    //calculate the next board and store it in tileNext
    public void setNext(){
        tilesNext = new ArrayList<>();
        for(int i=0; i<height*width; i++){
            tilesNext.add(i, tiles.get(i).update(getNeighbours(i%width, i/height),settings));
        }
    }

    //This updates the board for the next turn
    public void update(){
        setNext();
        tiles = tilesNext;
        setNext();
    }

    //return a list of neighbours, useful for Tile update
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
