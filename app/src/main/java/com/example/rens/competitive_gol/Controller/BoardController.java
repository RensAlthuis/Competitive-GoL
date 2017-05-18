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

        // SCALING
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

        // CLICKING/SWIPING
        mGestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {

            @Override // loslaten een keer klikken
            public boolean onSingleTapUp(MotionEvent e) {
                float offset = boardView.getScaledTileWidth();
                float hitX = boardView.offX(e.getX());
                float hitY = boardView.offY(e.getY());

                for (int y = 0; y < board.height; y++) {
                    for (int x= 0; x < board.width; x++)

                        //basic bounding box
                        if (    hitX > (x * offset) &&
                                hitX < ((x + 1) * offset) &&
                                hitY > (y * offset) &&
                                hitY < ((y + 1) * offset)
                                )
                        {
                            // TODO: Moet volgens de spelregels het gewenste team aangeven.
                            doMove(x, y);
                        }
                    }
                return false;
            }

            @Override // schuiven naar links/rechts
            public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
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


