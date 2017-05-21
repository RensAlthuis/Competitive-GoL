package com.example.rens.competitive_gol.Controller;

import android.app.Activity;
import android.content.Context;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;

import java.util.ArrayList;

import com.example.rens.competitive_gol.Model.Board;
import com.example.rens.competitive_gol.Model.Player;
import com.example.rens.competitive_gol.R;
import com.example.rens.competitive_gol.View.BoardView;

public class BoardController {

    private final Board board;
    private final BoardView boardView;

    private final ArrayList<Player> allPlayers;
    private int curPlayerIndex;

    private final ScaleGestureDetector mScaleDetector;
    private final GestureDetector mGestureDetector;

    /*******************CONSTRUCTORS*******************/

    public BoardController(final Activity activity, final Context context, final Board level, ArrayList<Player> players){
        board = level;
        boardView = (BoardView)activity.findViewById(R.id.board);
        boardView.init(getBoardWidth(), getBoardHeight());
        setBoardView(); //zorgt er btw ook voor dat een meer crazy bord gelijk goed getekent wordt! :)

        allPlayers = players;
        curPlayerIndex = 0;

        // TODO: Optionele verbetering:
        //allPlayers = sortedPlayers(players); //sorteerd de spelers opniew in afzonderlijke teams van 1 tm size() (aantal spelers)

        /*********************************************USER CONTROLS********************************************/

        boardView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                mScaleDetector.onTouchEvent(event);
                mGestureDetector.onTouchEvent(event);
                return true;
            }
        });

        // SCALING: dit detecteerd bewegingen waarbij je twee aanraakpunten naar elkaar toe trekt/van elkaar weg haalt
        mScaleDetector = new ScaleGestureDetector(context, new ScaleGestureDetector.OnScaleGestureListener() {
            private float oldScaleFactor;

            @Override // begin scaling
            public boolean onScaleBegin(ScaleGestureDetector detector) {
                oldScaleFactor = detector.getScaleFactor();
                return true;
            }

            @Override // tijdens scaling
            public boolean onScale(ScaleGestureDetector detector) {
                float dScaling = detector.getScaleFactor() - oldScaleFactor;
                boardView.updateScaling(dScaling, detector.getFocusX(), detector.getFocusY());
                oldScaleFactor = detector.getScaleFactor();
                return false;
            }

            @Override // einde scaling
            public void onScaleEnd(ScaleGestureDetector detector) {}

        });

        // CLICKING/SWIPING: dit detecteerd kleine simpele bewegingen die je met een aanraakpunt maakt
        mGestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {

            @Override //Voor: loslaten na een keer klikken
            public boolean onSingleTapUp(MotionEvent e) {
               //Deze functie wordt gebruikt wanneer je klikt op een tile :)
                final float offset = boardView.getScaledTileWidth();
                final int a = (int)Math.floor(boardView.offX(e.getX())/offset);
                final int b = (int)Math.floor(boardView.offY(e.getY())/offset);

                doMove(a,b);

                return false;
            }

            @Override //Voor: schuiven
            public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                //Deze functie wordt gebruikt wanneer je via een swipe beweging je verplaatst over het bord
                boardView.updateOffset(distanceX, distanceY);
                return false;
            }
        });

        /******************************************************************************************************/
    }

    public void touched(MotionEvent event){
        mScaleDetector.onTouchEvent(event);
        mGestureDetector.onTouchEvent(event);
    }

    // Maakt een kopie die gesorteerd is op basis van de volgorde van de array players
    private ArrayList<Player> sortedPlayers(ArrayList<Player>  players){
        ArrayList<Player> sortedPlayers = new ArrayList<>();
        for(int i=0; i<players.size() ; i++)
            sortedPlayers.add(players.get(i).copy(i+1));
        return sortedPlayers;
    }

    /*******************FUNCTIONS*******************/

    public int getBoardWidth(){ return board.width; }
    public int getBoardHeight(){ return board.height; }

    private int curTeam() { return allPlayers.get(curPlayerIndex).getTeam(); }
    private int curColor() { return allPlayers.get(curPlayerIndex).getColor(); }

    // zet de volgende speler
    public void nextPlayer(){
        if(curPlayerIndex < allPlayers.size()-1) curPlayerIndex++;
        else curPlayerIndex = 0;
    }

    // een 'zet'
    private void doMove(int x, int y){
        //TODO oke voorwaarden voor wat kan/niet kan?
        if(board.getTileTeam(x,y) == curTeam()){
            setDead(x, y);
        }
        else if(board.isDead(x,y)) {
            setPlayer(x, y);
        }
    }

    // zet (x,y) op de huidige speler
    private void setPlayer(int x, int y){
        board.setTilePlayer(x, y, curTeam());
        boardView.setTilePlayer(x, y, curColor());
    }

    // zet (x,y) op dood
    private void setDead(int x, int y){
        board.setTileDead(x, y);
        boardView.setTileDead(x, y);
    }

    /*******************UPDATE*******************/

    // stap 1) update board, stap 2) update boardView aan de hand van board
    public void update(){
        board.update();
        setBoardView();
    }

    // TODO: een oplossing om deze for functie te voorkomen is om players op tiles te zetten op het bord. idee?
    // TODO: alterntief zou je ook ipv team -32,342834209,12,38234 en 278 gewoon standaard team 1,2,3,4,5 kunnen gebruiken. dat zorgt dat teams -> speler een STUK makkelijker gaat door gewoon te kijken naar allPlayers.get(team-1)
    // (hiervoor sortedPlayers)
    private Player findPlayer(int team){
        for(Player player : allPlayers)
            if(player.getTeam() == team) return player;
        return null;
    }

    // maakt de boardView up-to-board met de latest tiles fashion
    private void setBoardView(){
        for(int y=0; y<board.height ; y++)
            for(int x=0; x<board.width ; x++){
                if(board.getTileTeam(x,y)!=0) boardView.setTilePlayer(x,y,findPlayer(board.getTileTeam(x,y)).getColor());
                else boardView.setTileDead(x,y);
            }
    }
}


