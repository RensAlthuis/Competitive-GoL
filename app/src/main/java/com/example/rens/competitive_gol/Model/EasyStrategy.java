package com.example.rens.competitive_gol.Model;

import android.util.Log;

import com.example.rens.competitive_gol.Controller.BoardController;
import com.example.rens.competitive_gol.Controller.BoardSimulator;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * This class implements an AI strategy that is easily beaten.
 * Created by Glenn on 15/05/2017.
 */

public class EasyStrategy implements AIStrategy{

    @Override
    public void makeMove(BoardController boardControl, int playerNr){
        BoardSimulator boardSim = new BoardSimulator(boardControl);
        ArrayList<Coordinate> moves = possibleMoves(boardSim, playerNr);
        Coordinate bestMove = findOptimalMove(boardSim, playerNr, moves);
        boardControl.doMove(bestMove);
        Log.d("AIMOVE", bestMove.x + " - " + bestMove.y);
    }

    /**
     * This function calculates the moves the AI is allowed to make.
     * @param boardSim a board for testing on, which is in the same state as the board the AI
     *                 is playing on
     * @param playerNr the AI's player number.
     * @return an ArrayList containing the Coordinates of the tiles where the AI is allowed to make
     * a move.
     */
    private ArrayList<Coordinate> possibleMoves(BoardSimulator boardSim, int playerNr){
        ArrayList<Coordinate> moves = new ArrayList();
        int width = boardSim.getBoardWidth();
        int height = boardSim.getBoardHeight();

        for(int x = 0; x < width; x++){
            for(int y = 0; y < height; y++){
                if(boardSim.getTeam(x, y) == playerNr || boardSim.getTeam(x, y) == Tile.DEAD){
                    Coordinate coord = new Coordinate(x, y);
                    moves.add(coord);
                }
            }
        }
        return moves;
    }

    /**
     * This function finds the optimal move by comparing the number of tiles claimed by the AI after
     * a single iteration to the current board state.
     * @param boardSim the board used to test moves on, it's initial state is equal to the board the
     *                 AI is playing on.
     * @param playerNr the AI's player number.
     * @param moves a list of Coordinates of tiles where the AI is allowed to make a move.
     * @return the coordinate of the tile where the optimal move can be made.
     */
    private Coordinate findOptimalMove(BoardSimulator boardSim, int playerNr, ArrayList<Coordinate> moves){
        Coordinate move = moves.get(1);
        int tilesBefore;
        int tilesAfter;
        int maxGain = 0;
        BoardSimulator testBoard;
        Coordinate temp;

        Iterator<Coordinate> coordIterator = moves.iterator();
        while (coordIterator.hasNext()) {
            testBoard = new BoardSimulator(boardSim);
            temp = coordIterator.next();
            tilesBefore = countOwnTiles(testBoard, playerNr);
            testBoard.setTeam(temp.x, temp.y, playerNr);
            testBoard.iterateBoard();
            tilesAfter = countOwnTiles(testBoard, playerNr);
            if(tilesAfter-tilesBefore > maxGain){
                maxGain = tilesAfter-tilesBefore;
                move.x = temp.x;
                move.y = temp.y;
            }
        }

        return move;
    }

    /**
     * This function counts the number tiles currently claimed by the AI.
     * @param boardSim the BoardSimulator containing the board we wish to count on.
     * @param playerNr the AI's player number.
     * @return the number of tiles currently claimed by the AI.
     */
    private int countOwnTiles(BoardSimulator boardSim, int playerNr){
        int width = boardSim.getBoardWidth();
        int height = boardSim.getBoardHeight();
        int ownTiles = 0;

        for(int x = 0; x < width; x++){
            for(int y = 0; y < height; y++){
                if(boardSim.getTeam(x, y) == playerNr){
                    ownTiles++;
                }
            }
        }

        return ownTiles;
    }
}
