package com.example.rens.competitive_gol.Controller;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
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

    /*******************VARIABLES*******************/

    private final Board board;
    private final BoardView boardView;

    private final ArrayList<Player> allPlayers;
    private int curPlayerIndex;

    private final ScaleGestureDetector mScaleDetector;
    private final GestureDetector mGestureDetector;

    /*******************CONSTRUCTORS*******************/

    public BoardController(final Activity activity, final Context context, final Board level, final int numberPlayers){
        board = level;
        boardView = (BoardView)activity.findViewById(R.id.board);
        boardView.init(getBoardWidth(), getBoardHeight());
        setBoardView(); //zorgt er btw ook voor dat een meer crazy bord gelijk goed getekent wordt! :)

        allPlayers = Players(numberPlayers);
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

    // maakt een gesoteerde lijst van alle spelers afhankelijk van al opgestelde kleuren
    private ArrayList<Player> Players(int  numberPlayers){
        ArrayList<Player> sortedPlayers = new ArrayList<>();
        final int[] colours = {Color.BLUE,Color.RED,Color.GREEN,Color.MAGENTA,Color.YELLOW,Color.CYAN};

        for(int i=0; i<numberPlayers; i++)
            sortedPlayers.add(new Player(i+1,colours[i%colours.length]));

        return sortedPlayers;
    }

    /*******************FUNCTIONS*******************/

    public int getBoardWidth(){ return board.width; }
    public int getBoardHeight(){ return board.height; }

    private int curTeam() { return allPlayers.get(curPlayerIndex).getTeam(); }
    private int curColor() { return allPlayers.get(curPlayerIndex).getColor(); }

    // zet de volgende speler
    public void nextPlayer(){
        curPlayerIndex++;
        curPlayerIndex %= allPlayers.size();
    }

    // een 'zet'
    private void doMove(int x, int y){
        //TODO oke voorwaarden voor wat kan/niet kan?
        if(board.getTileTeam(x,y) == curTeam()){
            setTileDead(x, y);
        }
        else if(board.isDead(x,y)) {
            setTilePlayer(x, y);
        }

        board.setNext();
        setBoardView();
    }

    // zet (x,y) op de huidige speler
    private void setTilePlayer(int x, int y){
        board.setTilePlayer(x, y, curTeam());
        boardView.setTilePlayer(x, y, curColor());
    }

    // zet (x,y) op dood
    private void setTileDead(int x, int y){
        board.setTileDead(x, y);
        boardView.setTileDead(x, y);
    }

    /*******************UPDATE*******************/

    // stap 1) update board, stap 2) update boardView aan de hand van board
    public void update(){
        board.update();
        setBoardView();
    }

    // maakt de boardView up-to-board met de latest tiles fashion
    private void setBoardView(){
        for(int y=0; y<board.height ; y++)
            for(int x=0; x<board.width ; x++){
                if(board.isDead(x,y))       boardView.setTileDead(x,y);
                else                        boardView.setTilePlayer(x,y,allPlayers.get(board.getTileTeam(x,y)-1).getColor());

                if(board.isDeadNext(x,y))   boardView.setTileDeadNext(x,y);
                else                        boardView.setTilePlayerNext(x,y,allPlayers.get(board.getTileTeamNext(x,y)-1).getColor());
            }
    }
}


