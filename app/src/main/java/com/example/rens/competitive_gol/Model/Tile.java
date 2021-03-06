package com.example.rens.competitive_gol.Model;

import java.util.ArrayList;

/**
 * This class models a single Tile on the Board.
 */
public class Tile {

    /*******************VARIABLES*******************/

    public static final int DEAD = -1;

    // -1 = dood, 0,1,2,3,4.. = van speler 0,1,2,3,4..
    int team;
    private int health; // :( als levens 0 zijn kan het team nogsteeds iets anders zijn. i am not amused. // TODO: willen we nog levens behouden?

    /*******************CONSTRUCTORS*******************/

    public Tile(){
        this(DEAD,0);
    }

    public Tile(int team, int health){
        this.team   = team;
        this.health = health;
    }

    /*******************FUNCTIONS*******************/

    public boolean isDead() { return team == DEAD; }
    public int getHealth() {  return health; }

    public void kill() {
        team = DEAD;
        health = 0;
    }

    /*******************NEXT*******************/
    // TODO DE FUNDAMENTELE TILE SPELREGELS

    //Roept de correcte update functie aan
    public Tile next(ArrayList<Tile> neighbours, TileSettings settings) {
        if(team!=DEAD)  return nextDie(neighbours, settings);
        else            return nextLive(neighbours, settings);
    }

    /**
     * This function checks whether live tiles need to die when iterated.
     * @param neighbours A list of all tiles on the board.
     * @param settings the Settings for the tiles.
     * @return the new state of the Tile.
     */
    private Tile nextDie(ArrayList<Tile> neighbours, TileSettings settings) {
        int nLiving = 0;

        for(Tile tile : neighbours)
            if(tile.team!=DEAD) nLiving++;

        if(nLiving < settings.minSurvive || nLiving > settings.maxSurvive) {
            if (health > 1) return new Tile(team,health-1);
            else            return new Tile();
        }

        return this;
    }

    /**
     * This function checks whether dead tiles need to come alive and if so, for which team.
     * @param neighbours A list of all tiles on the board.
     * @param settings the Settings for the tiles
     * @return The new state of the Tile.
     */
    private Tile nextLive(ArrayList<Tile> neighbours, TileSettings settings) {
        int nLiving = 0;
        final ArrayList<Integer[]> teamCount = new ArrayList<>();

        for (Tile tile : neighbours) {
            if (tile.team != DEAD) {
                nLiving++;

                boolean foundTeam = false;

                for(Integer[] team : teamCount)
                    if(team[0] == tile.team){
                        foundTeam = true;
                        team[1]++;
                    }
                if(!foundTeam) teamCount.add(new Integer[]{tile.team,0});
            }
        }

        if(nLiving >= settings.minLife && nLiving <= settings.maxLife){ // als de tile tot leven komt
            int bestTeam = DEAD;
            int maxCount = 0;

            for (int i = 0; i < teamCount.size(); i++) {
                if (teamCount.get(i)[1] > maxCount) {
                    bestTeam = teamCount.get(i)[0];
                    maxCount = teamCount.get(i)[1];
                }
            }

            return new Tile(bestTeam, settings.defaultHealth);
        }

        return this;
    }
}
