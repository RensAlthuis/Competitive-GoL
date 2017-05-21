package com.example.rens.competitive_gol.Model;

import java.util.ArrayList;

public class Tile {

    // 0 = dood
    // 1,2,3,4.. = van speler 1,2,3,4..
    int team;

    public Tile(){
        this(0);
    }
    public Tile(int team){
        this.team = team;
    }

    public Tile update(ArrayList<Tile> neighbours, TileSettings settings){
        if(team!=0) return updateDie(neighbours, settings);
        else        return updateLive(neighbours, settings);
    }

    private Tile updateDie(ArrayList<Tile> neighbours, TileSettings settings){
        int nLiving = 0;

        for(Tile tile : neighbours)
            if(tile.team!=0) nLiving++;

        if(nLiving < settings.minSurvive || nLiving > settings.maxSurvive)
            return new Tile();
        else return this;
    }

    private Tile updateLive(ArrayList<Tile> neighbours, TileSettings settings){
        int bestTeam = 0;
        int maxCount = 0;
        for(Tile tile : neighbours){
            if(tile.team!=0){
                int count = 0;

                for(Tile otherTile : neighbours)
                    if(tile.team == otherTile.team)
                        count++;

                if(count > maxCount && count >= settings.minLife && count <= settings.maxLife){
                    maxCount = count;
                    bestTeam = tile.team;
                }
            }
        }

        return new Tile(bestTeam);
    }
}
