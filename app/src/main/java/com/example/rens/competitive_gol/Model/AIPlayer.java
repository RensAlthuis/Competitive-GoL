package com.example.rens.competitive_gol.Model;

import com.example.rens.competitive_gol.Controller.BoardController;

/**
 * This class is used to implement a strategy-pattern. This class is to be used to define an
 * AI player for a certain game.
 * Created by Glenn on 15/05/2017.
 */

public class AIPlayer extends Player{
    private BoardController boardControl;
    private AIStrategy strategy;

    /**
     * The constructor
     * @param team the number of the AI player's team.
     * @param color the number of the color.
     * @param boardControl the boardcontroller that holds the playing board.
     * @param strategy the hard or easy strategy to use.
     */
    public AIPlayer(int team, int color, BoardController boardControl, AIStrategy strategy){
        super(team, color);
        this.boardControl = boardControl;
        this.strategy = strategy;
    }

    /**
     * This function makes the next move and iterates the game one step.
     */
    public void makeNextMove(){
        strategy.makeMove(boardControl, team);
    }

}
