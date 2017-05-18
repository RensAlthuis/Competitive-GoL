package com.example.rens.competitive_gol.Controller;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;

import com.example.rens.competitive_gol.Model.Board;
import com.example.rens.competitive_gol.Model.Player;
import com.example.rens.competitive_gol.R;
import com.example.rens.competitive_gol.View.BoardView;

import java.util.Map;

public class BoardController {
    private final Board board;
    private Player curPlayer;
    private final BoardView boardView;
    private final ScaleGestureDetector mScaleDetector;
    private final GestureDetector mGestureDetector;

    /*******************CONSTRUCTORS*******************/

    public BoardController(final Activity activity, final Context context, final Board level, Player player){
        board = level;
        boardView = (BoardView)activity.findViewById(R.id.board);
        boardView.init(getBoardWidth(), getBoardHeight());
        curPlayer = player;

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

                doMove(a,b); // TODO: 1 moet vervangen worden door de actieve speler atm

                return false;
            }

            @Override //Voor: schuiven
            public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                //Deze functie wordt gebruikt wanneer je via een swipe beweging je verplaatst over het bord
                boardView.updateOffset(distanceX, distanceY);
                return false;
            }
        });
    }

    /*******************FUNCTIONS*******************/

    public int getBoardWidth(){ return board.width; }
    public int getBoardHeight(){ return board.height; }

    public void touched(MotionEvent event){
        mScaleDetector.onTouchEvent(event);
        mGestureDetector.onTouchEvent(event);
    }

    public void setCurPlayer(Player p){
        curPlayer = p;
    }

    private void setTeam(int x, int y, int team){
        board.setTeam(x, y, team);
        boardView.setColor(x, y, curPlayer.getColor());
    }

    private void doMove(int x, int y){
        //TODO temporary solution
        if(board.getTeam(x,y) == board.DEAD) {
            setTeam(x, y, curPlayer.getTeamNumber());
        }
    }

    private int getTileColor(int x, int y){return getTileColor(board.getTeam(x,y));}

   public int getTileColor(int team){
        switch(team){
            case 0:
            case 1:

            default:
                return Color.WHITE; // als je wit ziet, ligt het hieraan
        }
    }

    public void update(){
        board.update();

        //TODO NO, really need to fix this shit
        for(int y = 0; y < board.width; y++) {
            for (int x = 0; x < board.width; x++) {
                if (board.getTeam(x, y) == board.DEAD) {
                    boardView.setColor(x, y, Color.GRAY);
                }
            }
        }
    }

    //TODO find a better solution for this this is crappy
    public void redrawPlayer(Player p){
        for(int y = 0; y < board.width; y++) {
            for (int x = 0; x < board.width; x++) {
                if (board.getTeam(x, y) == p.getTeamNumber()) {
                    boardView.setColor(x, y, p.getColor());
                }
            }
        }
    }

}


