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

                for (int a = 0; a < board.width; a++)
                    for (int b = 0; b < board.height; b++) {

                        //basic bounding box
                        if (    hitX > (a * offset) &&
                                hitX < ((a + 1) * offset) &&
                                hitY > (b * offset) &&
                                hitY < ((b + 1) * offset)
                                )
                        {
                            board.setTeam(a,b,1);
                            boardView.setTileColor(a,b,getTileColor(a,b));
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

    public int getTileColor(int x, int y){
        switch(board.getTeam(x,y)){
            default:
            case 0:
                return Color.BLACK;
            case 1:
                return Color.GREEN;
        }
    }
}
