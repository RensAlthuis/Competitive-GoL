package com.example.rens.competitive_gol.Controller;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;

import com.example.rens.competitive_gol.Model.Board;
import com.example.rens.competitive_gol.R;
import com.example.rens.competitive_gol.View.BoardView;

public class BoardController {
    private final Board board;
    private final BoardView boardView;
    private final ScaleGestureDetector mScaleDetector;
    private final GestureDetector mGestureDetector;

    /*******************CONSTRUCTORS*******************/

    public BoardController(final Activity activity, final Context context, final Board level){
        board = level;
        boardView = (BoardView)activity.findViewById(R.id.board);
        boardView.setBoard(this);

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

                clickTile(a,b,1); // TODO: 1 moet vervangen worden door de actieve speler atm

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

    // Deze functie bepaald wat er moet gebeuren wanneer tile (a,b) geklikt wordt door speler 'player'
    private boolean clickTile(int x, int y, int player){
        if(board.getTeam(x,y)==0)
            board.setTeam(x,y,player);
        else if(board.getTeam(x,y)==player)
            board.setTeam(x,y,0);
        else
            return false;
        return true;
    }

    // Bepaald de kleur/plaatje die een tile zou moeten hebben
    // TODO: als we plaatjes willen gebruiken ipv gekleurde vierkanten: verrander dit!
    public int getTileColor(int x, int y){return getTileColor(board.getTeam(x,y));}
    public int getTileColor(int team){
        switch(team){
            case 0:
                return Color.GRAY;
            case 1:
                return Color.GREEN;
            default:
                return Color.WHITE; // als je wit ziet, ligt het hieraan
        }
    }
}
