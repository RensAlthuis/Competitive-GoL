package com.example.rens.competitive_gol.Controller;

import com.example.rens.competitive_gol.Model.Board;
import com.example.rens.competitive_gol.Model.Tile;

/**
 * This class is used to give the AI a board and a controller to test out moves internally.
 * Created by Glenn on 17/05/2017.
 */

public class BoardSimulator {
    private Board board;

    /**
     * There is a constructor that uses a Board as input and a constructor that uses
     * a BoardController as input.
     * There is also a copy constructor.
     */
    public BoardSimulator(Board board){
        this.board = new Board(board);
    }

    public BoardSimulator(BoardSimulator aBoardSimulator){
        this(aBoardSimulator.getBoard());
    }

    public BoardSimulator(BoardController boardControl){
        this(boardControl.getBoard());
    }

    /**
     * This function makes one iteration using the game of life rules.
     */
    public void iterateBoard(){
        board.update();
    }

    /**
     * This function counts the number tiles currently claimed by the player.
     * @param playerNr the AI's player number.
     * @return the number of tiles currently claimed by the AI.
     */
    public int countPlayerTiles(int playerNr){
        int width = this.getBoardWidth();
        int height = this.getBoardHeight();
        int ownTiles = 0;

        for(int x = 0; x < width; x++){
            for(int y = 0; y < height; y++){
                if(this.getTeam(x, y) == playerNr){
                    ownTiles++;
                }
            }
        }

        return ownTiles;
    }

    public double computePlayerRatio(int playerNr){
        int otherPlayer = -1;
        if(playerNr==0){
            otherPlayer = 1;
        }
        else if(playerNr==1){
            otherPlayer = 0;
        }
        int ownTiles = countPlayerTiles(playerNr);
        int otherTiles = countPlayerTiles(otherPlayer);
        double gain;
        if (otherTiles == 0){
            gain = 99;
        }
        else{
            gain = ownTiles/otherTiles;
        }
        return gain;
    }

    /**
     * getters and setters
     */
    public int getBoardWidth(){ return board.width; }
    public int getBoardHeight(){ return board.height; }

    public void setTeam(int x, int y, int player){
        if(board.getTileTeam(x,y)== Tile.DEAD)
            board.setTileTeam(x,y,player);
        else
            board.setTileDead(x,y);
    }

    public int getTeam(int x, int y){
        return board.getTileTeam(x, y);
    }

    public Board getBoard(){
        return board;
    }
}
