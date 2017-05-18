package com.example.rens.competitive_gol.View;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.example.rens.competitive_gol.Controller.BoardController;

import static java.lang.Math.max;
import static java.lang.Math.min;

public class BoardView extends View {

    /*******************VARIABLES*******************/

    private int nTilesX;
    private int nTilesY;
    private float tileWidth;
    private float tileHeight;
    private float scaling = 1; //May the gods be with us
    private float offsetX = 0;
    private float offsetY = 0;
    private int colors[];

    /*******************CONSTRUCTORS*******************/
    public BoardView(Context context) {
        super(context);
    }

    public BoardView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BoardView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void init(int width, int height) {
        nTilesX = width;
        nTilesY = height;

        colors = new int[nTilesX*nTilesY];
        for (int i = 0; i < nTilesX*nTilesY; i++){
            colors[i] = Color.GRAY;
        }
    }

    public void setColor(int x, int y, int col){
        colors[y*nTilesX + x] = col;
    }

    /*******************FUNCTIONS*******************/

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
        Log.d("UDEBUG_dOffX", "" + dOffX);
        int maxpiv = (int) (getWidth() * (scaling - 1));

        //clamp to [0;offset]
        offsetX = max(0, min(maxpiv, offsetX));
        offsetY = max(0, min(maxpiv, offsetY));
    }

    public float getScaledTileWidth(){ return tileWidth * scaling; }
    public float offX(float n){ return n + offsetX; }
    public float offY(float n){ return n + offsetY; }

    @Override
    protected void onDraw (Canvas canvas)
    {
        //For future arguments: these need to be in onDraw because of screen flipping!
        //Cast to float simply for we aren't using arithmetic over floats
        tileWidth = (float) (canvas.getWidth()) / nTilesX;
        tileHeight = (float) (canvas.getHeight()) / nTilesY;

        canvas.translate(-offsetX, -offsetY);
        canvas.scale(scaling, scaling);
        canvas.drawColor(Color.DKGRAY); // the border color

        for (int a = 0; a < nTilesX; a++) {
            for (int b = 0; b < nTilesY; b++) {
                //Inner blocks
                drawBlock(canvas, a, b);
            }
        }
        invalidate();
    }

    private void drawBlock(Canvas canvas, int x, int y){
        Paint p = new Paint();
        p.setColor(colors[y*nTilesX+x]);
        p.setStyle(Paint.Style.FILL_AND_STROKE);
        canvas.drawRect(x * tileWidth, y * tileHeight, (x + 1) * tileWidth -2f, (y + 1) * tileHeight -2f, p);
    }

   /* @Override
    public boolean onTouchEvent(MotionEvent event) {
        //event.offsetLocation(offsetX, offsetY);
        //event.setLocation(event.get);
        controller.touched(event);
        invalidate();
        return true;
    }
    */
}
