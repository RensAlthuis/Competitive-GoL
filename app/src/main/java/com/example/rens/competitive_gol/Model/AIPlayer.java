package com.example.rens.competitive_gol.Model;

import com.example.rens.competitive_gol.Controller.BoardController;

/**
 * This class is used to implement a strategy-pattern. This class is to be used to define an
 * AI player for a certain game.
 * Created by Glenn on 15/05/2017.
 */

public class AIPlayer {
    private BoardController boardControl;
    private AIStrategy strategy;
    private int playerNr;

    /**
     * The constructor.
     * @param boardControl the board the AI player will be playing on.
     * @param strategy the strategy is will be using; either Easy or Hard (Hard to be implemented)
     * @param playerNr the playerNr of the AI player.
     */
    public AIPlayer(BoardController boardControl, AIStrategy strategy, int playerNr){
        this.boardControl = boardControl;
        this.strategy = strategy;
        this.playerNr = playerNr;
    }

    /**
     * This function makes the next move and iterates the game one step.
     */
    public void makeNextMove(){
        strategy.makeMove(boardControl, playerNr);
        boardControl.iterateBoard();
    }

}
