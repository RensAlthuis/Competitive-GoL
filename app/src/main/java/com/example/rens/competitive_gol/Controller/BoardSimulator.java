package com.example.rens.competitive_gol.Controller;

import com.example.rens.competitive_gol.Model.Board;

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
     * getters and setters
     */
    public int getBoardWidth(){ return board.width; }
    public int getBoardHeight(){ return board.height; }

    public void setTeam(int x, int y, int player){
        if(board.getTileTeam(x,y)==0)
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
