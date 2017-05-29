package com.example.rens.competitive_gol.View;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import com.example.rens.competitive_gol.Controller.BoardController;
import com.example.rens.competitive_gol.Model.Coordinate;

import static java.lang.Math.max;
import static java.lang.Math.min;

public class BoardView extends View {

    /*******************VARIABLES*******************/

    private BoardController controller;
    private int nTilesX; // aantal tiles horizontaal (wordt gelijk gezet)
    private int nTilesY; // aantal tiles verticaal (wordt gelijk gezet)

    private float tileWidth; // lengte van een tile
    private float tileHeight; // breedte van een tile

    private float scaling = 1;
    private float offsetX = 0; //Hoeveel er naar links/rechts is beweegt (TODO: vergroot dit als we een rand willen toevoegen)
    private float offsetY = 0; //Hoeveel er naar boven/beneden is beweegt (TODO: vergroot dit als we een rand willen toevoegen)

    private int colors[]; // the color for every block.
    private int colorsNext[]; // the color for every block next turn
    private int health[]; // the health for every block.

    private final static int DEAD = Color.GRAY; // color for dead tiles
    private final static float BORDERSIZE = 2f; // size of border between blocks
    private final static float SIZENEXT = 0.5f; // size of the smaller block that indicates what happens next turn

    /*******************CONSTRUCTORS*******************/

    /*****  necessary for Android Views ********/
    public BoardView(Context context) {
        super(context);
    }

    public BoardView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BoardView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }
    /*****************************************/

    //Actual constructor
    public void init(int width, int height) {
        nTilesX = width;
        nTilesY = height;

        colors     = new int[nTilesX * nTilesY];
        colorsNext = new int[nTilesX * nTilesY];
        health     = new int[nTilesX * nTilesY];

        for (int i = 0; i < nTilesX * nTilesY; i++) {
            colors[i] = Color.WHITE; // Wit zien is foute boel!
            colorsNext[i] = Color.WHITE;
        }
    }

    /*******************FUNCTIONS*******************/

    //functions for changing tile colors
    public void setTilePlayer(int x, int y, int col) { colors[y * nTilesX + x] = col; }
    public void setTileDead(int x, int y){ colors[y*nTilesX + x] = DEAD; }

    public void setTilePlayerNext(int x, int y, int col) { colorsNext[y * nTilesX + x] = col; }
    public void setTileDeadNext(int x, int y){ colorsNext[y*nTilesX + x] = DEAD; }

    public void setTileHealth(int x, int y, int health) { this.health[y * nTilesX + x] = health; }

    //this updates the scaling when zooming in (pinching)
    public void updateScaling(float dScaling, float focusX, float focusY) {
        scaling += dScaling;

        //clamp to [1;2]
        scaling = max(1, min(2, scaling));

        offsetX += focusX * (dScaling);
        offsetY += focusY * (dScaling);
        int maxpiv = (int) (getWidth() * (scaling - 1));

        //clamp to [0;offset]
        offsetX = max(0, min(maxpiv, offsetX));
        offsetY = max(0, min(maxpiv, offsetY));
    }

    //this updates the offset used for scrolling across the screen
    public void updateOffset(float dOffX, float dOffY) {
        offsetX += dOffX;
        offsetY += dOffY;
        int maxpiv = (int) (getWidth() * (scaling - 1));

        //clamp to [0;offset]
        offsetX = max(0, min(maxpiv, offsetX));
        offsetY = max(0, min(maxpiv, offsetY));
    }

    //Width of a tile as it appears on the screen (the literal amount of pixels)
    public float getScaledTileWidth() {
        return tileWidth * scaling;
    }

    //get the scaled location of n in screen coordinates
    public float offX(float n) {
        return n + offsetX;
    }
    public float offY(float n) {
        return n + offsetY;
    }

    /********************DRAW********************/

    @Override
    protected void onDraw(Canvas canvas) {
        //For future arguments: these need to be in onDraw because of screen flipping!
        //Cast to float simply for we aren't using arithmetic over floats
        tileWidth  = (float) (canvas.getWidth()) / nTilesX;
        tileHeight = (float) (canvas.getHeight()) / nTilesY;

        canvas.translate(-offsetX, -offsetY);
        canvas.scale(scaling, scaling);
        canvas.drawColor(Color.DKGRAY); // the border color

        for (int a = 0; a < nTilesX; a++){
            for (int b = 0; b < nTilesY; b++){
                drawBlock(canvas, a, b);
                drawBlockNext(canvas, a, b);
                drawBlockHealth(canvas, a, b);
            }
        }

        invalidate();
    }


    //Draw the tile at x,y
    private void drawBlock(Canvas canvas, int x, int y) {
        Paint p = new Paint();
        p.setColor(colors[y * nTilesX + x]);
        p.setStyle(Paint.Style.FILL_AND_STROKE);
        canvas.drawRect(x * tileWidth, y * tileHeight, (x + 1) * tileWidth - BORDERSIZE, (y + 1) * tileHeight - BORDERSIZE, p);
    }

    //Draw the indicator for what the tile will be next turn
    private void drawBlockNext(Canvas canvas, int x, int y){
        Paint p = new Paint();
        p.setColor(colorsNext[y * nTilesX + x]);
        p.setStyle(Paint.Style.FILL_AND_STROKE);
        canvas.drawRect((x + 0.5f*SIZENEXT)* tileWidth, (y + 0.5f*SIZENEXT) * tileHeight, (x + 1 - 0.5f*SIZENEXT) * tileWidth - BORDERSIZE, (y + 1 - 0.5f*SIZENEXT) * tileHeight - BORDERSIZE, p);
    }

    //Draw the indicator for the tiles health
    private void drawBlockHealth(Canvas canvas, int x, int y){
        Paint p = new Paint();
        p.setColor(Color.WHITE);
        p.setStyle(Paint.Style.FILL_AND_STROKE);
        p.setFakeBoldText(true);
        p.setTextSize(40);
        p.setTextAlign(Paint.Align.CENTER);
        canvas.drawText(""+health[y * nTilesX + x], (x+0.5f) * tileWidth, (y+0.5f) * tileHeight, p);
    }
}

