package com.example.rens.competitive_gol.Model;

import com.example.rens.competitive_gol.Controller.BoardController;
import com.example.rens.competitive_gol.Controller.BoardSimulator;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

/**
 * Created by Glenn on 22/05/2017.
 */

public class HardStrategy implements AIStrategy{
    final private int UNCLAIMED = 0;
    final private int TREE_DEPTH = 50;
    final private int TREE_WIDTH_PER_MOVE = 20;
    private int humanPlayerNr;

    @Override
    public void makeMove(BoardController boardControl, int playerNr){
        if (playerNr == 1){
            humanPlayerNr = 0;
        }
        else if(playerNr == 0){
            humanPlayerNr = 1;
        }
        else{
            // This should throw an exception.
        }
        BoardSimulator boardSim = new BoardSimulator(boardControl);
        ArrayList<Coordinate> moves = possibleMoves(boardSim, playerNr);
        Coordinate bestMove = findOptimalMove(boardSim, playerNr, moves);
        boardControl.doMove(bestMove);
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
                if(boardSim.getTeam(x, y) == playerNr || boardSim.getTeam(x, y) == UNCLAIMED){
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
        Coordinate move = new Coordinate(0, 0);
        int tilesBefore;
        int tilesAfter;
        int maxGain = 0;
        int currentGain;
        Coordinate temp;

        Iterator<Coordinate> coordIterator = moves.iterator();
        while (coordIterator.hasNext()) {
            temp = coordIterator.next();
            currentGain = averageGainOnMove(boardSim, playerNr, temp);
            if(currentGain > maxGain){
                maxGain = currentGain;
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

    private int averageGainOnMove(BoardSimulator boardSim, int playerNr, Coordinate move){
        Random rand = new Random();
        ArrayList<Coordinate> tempMoves;
        Coordinate randomMove = new Coordinate(0, 0);
        int currentGain = 0;
        int averageGain;
        int totalGain = 0;
        int tilesBefore;
        int tilesAfter;


        for(int j = 0; j < TREE_WIDTH_PER_MOVE; j++){

            tilesBefore = countOwnTiles(boardSim, playerNr);
            BoardSimulator testBoard = new BoardSimulator(boardSim);
            boardSim.setTeam(move.x, move.y, playerNr);
            testBoard.iterateBoard();

            for(int i = 0; i < TREE_DEPTH; i++){

                if(i%2==0){
                    tempMoves = possibleMoves(testBoard, humanPlayerNr);
                    int randomSelector = rand.nextInt(tempMoves.size() + 1);
                    randomMove = tempMoves.get(randomSelector);
                }
                else if(i%2!=0){
                    tempMoves = possibleMoves(testBoard, playerNr);
                    int randomSelector = rand.nextInt(tempMoves.size() + 1);
                    randomMove = tempMoves.get(randomSelector);
                }

                testBoard.setTeam(randomMove.x, randomMove.y, playerNr);
                testBoard.iterateBoard();
            }

            tilesAfter = countOwnTiles(testBoard, playerNr);
            currentGain = tilesAfter - tilesBefore;
            totalGain += currentGain;
        }
        averageGain = totalGain/TREE_WIDTH_PER_MOVE;
        return averageGain;
    }
}
