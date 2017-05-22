package com.example.rens.competitive_gol.Model;

import java.util.ArrayList;

public class Tile {

    // -1 = dood
    // 0,1,2,3,4.. = van speler 0,1,2,3,4..
    int team;
    int health;
    public static final int DEAD = -1;
    public static final int DEFAULTLIFE = 1;

    public Tile(){
        this(DEAD,0);
    }

    public Tile(int team, int health){
        this.team = team;
        this.health = health;
    }

    /*******************UPDATE*******************/

    public Tile update(ArrayList<Tile> neighbours, TileSettings settings){
        if(team!=DEAD) return updateDie(neighbours, settings);
        else        return updateLive(neighbours, settings);
    }

    private Tile updateDie(ArrayList<Tile> neighbours, TileSettings settings){
        int nLiving = 0;

        for(Tile tile : neighbours)
            if(tile.team!=DEAD) nLiving++;


        if(nLiving < settings.minSurvive || nLiving > settings.maxSurvive) {
            if (health > 1) {
                return new Tile(team,health-1);
            } else {
                return new Tile();
            }
        }
        return this;
    }

    private Tile updateLive(ArrayList<Tile> neighbours, TileSettings settings) {

        int bestTeam = DEAD;
        int maxCount = 0;

        int teamCount[] = new int[20];
        for(int i = 0; i < teamCount.length; i++) teamCount[i] = 0;

        int nLiving = 0;


        for (Tile tile : neighbours) {
            if (tile.team != DEAD) {
                teamCount[tile.team]++;
                nLiving++;
            }
        }

        for (int i = 0; i < teamCount.length; i++) {
            if (teamCount[i] > maxCount && nLiving >= settings.minLife && nLiving <= settings.maxLife) {
                maxCount = teamCount[i];
                bestTeam = i;
            }
        }

        return new Tile(bestTeam, Tile.DEFAULTLIFE);

    }
        /*
        for(Tile tile : neighbours){
            if(tile.team!=DEAD){
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
    */
}
