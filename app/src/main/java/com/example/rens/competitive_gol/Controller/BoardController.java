package com.example.rens.competitive_gol.Controller;

import android.app.Activity;
import android.content.Context;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;

import com.example.rens.competitive_gol.Model.Board;
import com.example.rens.competitive_gol.Model.TileSettings;
import com.example.rens.competitive_gol.R;
import com.example.rens.competitive_gol.View.BoardView;

import static java.lang.Math.max;
import static java.lang.Math.min;

/**
 * Created by Rens on 14-5-2017.
 */

public class BoardController {
    private final Board board;
    private final TileSettings settings;

    private final BoardView boardView;

    private final ScaleGestureDetector mScaleDetector;
    private final GestureDetector mGestureDetector;



    public BoardController(Activity activity, Context context){
        settings = new TileSettings();
        board = new Board(20,20,settings);
        boardView = (BoardView) activity.findViewById(R.id.board);
        boardView.setBoard(this);

        mScaleDetector = new ScaleGestureDetector(context, new ScaleGestureDetector.OnScaleGestureListener() {
            private float oldScaleFactor;
            @Override
            public void onScaleEnd(ScaleGestureDetector detector) {}

            @Override
            public boolean onScaleBegin(ScaleGestureDetector detector) {
                oldScaleFactor = detector.getScaleFactor();
                return true;
            }

            @Override
            public boolean onScale(ScaleGestureDetector detector) {

                float dScaling = detector.getScaleFactor() - oldScaleFactor;
                boardView.updateScaling(dScaling, detector.getFocusX(), detector.getFocusY());

                oldScaleFactor = detector.getScaleFactor();

                return false;
            }
        });

        mGestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {

            @Override
            public boolean onSingleTapUp(MotionEvent e) {

                float offset = boardView.getOffset();
                float hitX = e.getX();
                float hitY = e.getY();

                for (int a = 0; a < board.width; a++)
                    for (int b = 0; b < board.height; b++) {

                        //basic bounding box
                        if (    hitX > (a * offset) &&
                                hitX < ((a + 1) * offset) &&
                                hitY > (b * offset) &&
                                hitY < ((b + 1) * offset)
                                )
                        {
                            board.getTileAt(a, b).Set(1);
                            boardView.setTileColor(a,b,getTileCol(a,b));
                        }
                    }
                return false;
            }

            @Override
            public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {

                boardView.updateOffset(distanceX, distanceY);
                return false;
            }
        });

    }

    public int getBoardWidth(){ return board.width; }
    public int getBoardHeight(){ return board.height; }

    public void touched(MotionEvent event){
        mScaleDetector.onTouchEvent(event);
        mGestureDetector.onTouchEvent(event);

    }

    public int[] getTileCol(int x, int y){

        int col [] = {0,0,0};
        switch(board.getTileAt(x, y).getTeam()){
            case 0:{
                col = new int[]{127,127,127};
                break;
            }
            case 1:{
                col = new int[]{0,255,0};
                break;
            }

        }
        return col;
    }
}
