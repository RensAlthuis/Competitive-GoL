package com.example.rens.competitive_gol.Model;

import com.example.rens.competitive_gol.Controller.BoardController;

/**
 * This interface is used to implement different difficulties of AI.
 * Created by Glenn on 15/05/2017.
 */

public interface AIStrategy {
    /**
     * This function calculates the optimal move using the chosen strategy and the makes the move.
     * @param boardControl the board the AI is to play on.
     * @param playerNr the AI's player number.
     */
    public void makeMove(BoardController boardControl, int playerNr);
}
