package com.example.rens.competitive_gol.Model;

public class main {

    //TODO: deze klassen is er alleen om de basis van spel makkelijk te kunnen testen. deze is dus tijdelijk!

    public static void main(String[] args){
        Board board = new Board(3,3,new TileSettings());
        board.print();
        board.update();
        System.out.println();
        board.print();
    }
}