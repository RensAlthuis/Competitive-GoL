package com.example.rens.competitive_gol.View;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;

import com.example.rens.competitive_gol.Controller.BoardController;
import com.example.rens.competitive_gol.Model.Board;
import com.example.rens.competitive_gol.Model.TileSettings;

import static java.lang.Math.max;
import static java.lang.Math.min;

/**
 * Created by Tom on 12-5-2017.
 */

public class BoardView extends View {

    /*******************VARIABLES*******************/

    private Paint recBorder = new Paint();

    private BoardController controller;
    private int nBlocks[];
    private int colors[][];
    private int nTilesX;
    private int nTilesY;
    private float tileWidth;
    private float tileHeight;
    private float scaling = 1; //May the gods be with us
    private float offsetX = 0;
    private float offsetY = 0;


    /*******************CONSTRUCTORS*******************/
    public BoardView(Context context) {
        super(context);

        init(context);
    }

    public BoardView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public BoardView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    public void init(Context context) {

        recBorder.setColor(Color.BLACK);
        recBorder.setStyle(Paint.Style.STROKE);
        recBorder.setStrokeWidth(4);
    }

    /*******************FUNCTIONS*******************/
    public void setBoard(BoardController controller){
        this.controller = controller;
        nTilesX = controller.getBoardWidth();
        nTilesY = controller.getBoardHeight();

        colors = new int[nTilesX*nTilesY][3];
        for(int i = 0; i < nTilesX*nTilesY; i++){
            for (int j = 0; j < 3; j++){
                colors[i][j] = 127;
            }
        }
    }

    public void setTileColor(int x, int y, int[] col){
        colors[y*nTilesX + x][0] = col[0];
        colors[y*nTilesX + x][1] = col[1];
        colors[y*nTilesX + x][2] = col[2];
    }

    public void updateScaling(float dScaling, float focusX, float focusY){
        scaling += dScaling;

        //clamp to [1;2]
        scaling = max(1,min(2,scaling));

        offsetX += focusX * (dScaling);
        offsetY += focusY * (dScaling);
        int maxpiv = (int) (getWidth() * (scaling - 1));

        //clamp to [0;offset]
        offsetX = max(0, min(maxpiv, offsetX));
        offsetY = max(0, min(maxpiv, offsetY));

    }

    public void updateOffset(float dOffX, float dOffY){

        offsetX += dOffX;
        offsetY += dOffY;
        int maxpiv = (int) (getWidth() * (scaling - 1));

        //clamp to [0;offset]
        offsetX = max(0, min(maxpiv, offsetX));
        offsetY = max(0, min(maxpiv, offsetY));

    }

    public float getOffset(){ return tileWidth * scaling; }

    @Override
    protected void onDraw (Canvas canvas)
    {
        //For future arguments: these need to be in onDraw because of screen flipping!
        //Cast to float simply for we aren't using arithmetic over floats
        tileWidth = (float) (canvas.getWidth()) / nTilesX;
        tileHeight = (float) (canvas.getHeight()) / nTilesY;

        canvas.translate(-offsetX, -offsetY);
        canvas.scale(scaling, scaling);
        canvas.drawColor(Color.RED);

        for (int a = 0; a < nTilesX; a++) {
            for (int b = 0; b < nTilesY; b++) {
                //Inner blocks
                drawBlock(canvas, a, b);

                //Block borders
                canvas.drawRect(a * tileWidth, b * tileHeight, (a + 1) * tileWidth, (b + 1) * tileHeight, recBorder);
            }
        }


    }

    private void drawBlock(Canvas canvas, int x, int y){
        Paint p = new Paint();
        int col[] = colors[y*nTilesX + x];
        p.setColor(Color.rgb(col[0], col[1], col[2]));
        p.setStyle(Paint.Style.FILL_AND_STROKE);
        canvas.drawRect(x * tileWidth, y * tileHeight, (x + 1) * tileWidth, (y + 1) * tileHeight, p);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        event.offsetLocation(offsetX, offsetY);
        controller.touched(event);

        invalidate();
        return true;
    }
}
