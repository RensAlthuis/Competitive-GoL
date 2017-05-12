package com.example.rens.competitive_gol.Model;

/**
 * Created by Tom on 12-5-2017.
 */

public class main {

    public static void main(String[] args){
        Board board = new Board(3,3,new TileSettings());
        board.print();
        board.update();
        System.out.println();
        board.print();
    }
}
