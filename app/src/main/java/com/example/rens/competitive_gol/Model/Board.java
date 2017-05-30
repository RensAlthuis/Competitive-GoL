package com.example.rens.competitive_gol.Model;

import java.util.ArrayList;
import java.util.Random;

public class Board {

    /*******************VARIABLES*******************/

    private final ArrayList<Tile> tiles;
    private final ArrayList<Tile> tilesNext;

    public  final int width;
    public  final int height;
    private final int size;

    private final TileSettings settings; // update settings

    /*******************CONSTRUCTORS*******************/

    //een constructor die alleen maar tiles hoeft te weten.
    //zo misschien makkelijker om snel een andere tiles te kunnen maken?
    public Board(Tile[][] tiles, TileSettings settings){
        this.width  = tiles[0].length;
        this.height = tiles.length;
        this.size   = width*height;

        this.settings = settings;

        this.tiles     = copyTiles(tiles);
        this.tilesNext = emptyTiles();
    }

    // van Tile[][] naar Arraylist
    private ArrayList<Tile> copyTiles(Tile[][] tiles){
        ArrayList<Tile> t = new ArrayList<>();
        for(int i=0; i<width; i++)
            for(int j=0; j<height; j++)
                t.add(tiles[j][i]);
        return t;
    }

    public Board(int width, int height, TileSettings settings){
        this.width  = width;
        this.height = height;
        this.size   = width*height;

        this.settings = settings;

        tiles     = emptyTiles();
        tilesNext = emptyTiles();
    }

    public Board(int width, int height, TileSettings settings, ArrayList<Tile> tiles){
        this.width  = width;
        this.height = height;
        this.size   = width*height;

        this.settings = settings;

        this.tiles = tiles;
        tilesNext  = emptyTiles();
        setNext();
    }

    //create all the tiles for the board
    private ArrayList<Tile> emptyTiles(){
        ArrayList<Tile> t = new ArrayList<>();
        for(int i=0; i<size; i++) t.add(new Tile());
        return t;
    }

    /*******************FUNCTIONS*******************/
    // TODO: met de komst van de levens is er veel meer dan alleen maar de team kleur wat een tile uniek maakt

    //return the tile x,y
    public Tile getTile(Coordinate c) { return getTile(c.x,c.y); }
    public Tile getTile(int x, int y){ return tiles.get(y*width + x); }

    //return the tile x,y next turn
    private Tile getTileNext(Coordinate c) { return getTile(c.x,c.y); }
    private Tile getTileNext(int x, int y){ return tilesNext.get(y*width + x); }

    //set tile x,y to a certain tile
    private void setTile(Coordinate c, Tile tile){ setTile(c.x,c.y, tile); }
    private void setTile(int x, int y, Tile tile){  tiles.set(y*width + x, tile); }


    //is the tile dead right now?
    public boolean isDead(Coordinate c){ return isDead(c.x,c.y); }
    public boolean isDead(int x, int y){ return getTile(x,y).isDead(); }

    //will the tile be dead next turn?
    public boolean isDeadNext(Coordinate c){ return isDeadNext(c.x,c.y); }
    public boolean isDeadNext(int x, int y) { return getTileNext(x,y).isDead(); }


    //return the team for tile x,y
    public int getTileTeam(Coordinate c){ return getTileTeam(c.x,c.y); }
    public int getTileTeam(int x, int y){ return tiles.get(y*width + x).team; }

    //return the team for tile x,y next turn
    public int getTileNextTeam(Coordinate c){ return getTileNextTeam(c.x,c.y); }
    public int getTileNextTeam(int x, int y){ return tilesNext.get(y*width + x).team; }


    //set tile x,y to a certain team
    public void setTileTeam(Coordinate c, int team){ setTileTeam(c.x,c.y, team); }
    public void setTileTeam(int x, int y, int team){  tiles.set(y*width + x, new Tile(team,settings.defaultHealth)); }

    //set tile x,y to dead
    public void setTileDead(Coordinate c){ setTileDead(c.x,c.y); }
    public void setTileDead(int x, int y) { tiles.set(y*width + x, new Tile()); }


    public void setTiles(ArrayList<Tile> tiles){
        this.tiles.clear();
        for(Tile tile : tiles) this.tiles.add(tile);
    }

    public ArrayList<Tile> getTiles(){
        ArrayList<Tile> copy =  new ArrayList<>();
        for(Tile tile : tiles) copy.add(new Tile(tile.team,tile.getHealth()));
        return copy;
    }

    /*******************DIFFERENT BOARDS*******************/

    public void setEmptyBoard(){
        for(Tile tile : tiles)
            tile.kill();

        setNext();
    }

    //fill the board in randomly
    public void setRandomBoard(int tilesPerPlayer, ArrayList<Player> allPlayers){
        Random rand = new Random();

        for(int i=0; i < allPlayers.size(); i++){
            for(int tilesLeft = tilesPerPlayer; tilesLeft!=0; tilesLeft--){
                int x;
                int y;

                do {
                    x = rand.nextInt(width);
                    y = rand.nextInt(height);
                } while (!isDead(x,y));

                setTileTeam(x, y, allPlayers.get(i).getTeam());
            }
        }

        setNext();
    }

    /*******************UPDATE*******************/

    //This updates the board for the next turn
    public void update(){
        setNext();
        overwriteArrayList(tiles,tilesNext);
        setNext();
    }

    // TODO: misschien een util class maken met al dit soort functies?
    // overschrijft de arraylist 'toOverwritte' met alle waardes van 'copy'
    // returnt false als ze niet even lang zijn.
    // TODO: een algemene check of ze wel dezelfde objecten gebruiken
    private boolean overwriteArrayList(ArrayList toOverwrite, ArrayList copy){
        if(toOverwrite.size() != copy.size()) return false;

        for(int i=0; i<toOverwrite.size(); i++)
            toOverwrite.set(i,copy.get(i));

        return true;
    }

    //calculate the next board and store it in tileNext
    //ROEP DEZE ZO VAAK AAN ALS JE WILT! :)
    public void setNext(){
        for(int i=0; i<size; i++) tilesNext.set(i, tiles.get(i).next(getNeighbours(i%width, i/height),settings));
    }

    //return a list of neighbours, useful for setNext()
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
