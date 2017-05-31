package com.example.rens.competitive_gol.View;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import com.example.rens.competitive_gol.Controller.BoardController;

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
    private float offsetX = 0; //Hoeveel er naar links/rechts is bewogen
    private float offsetY = 0; //Hoeveel er naar boven/beneden is bewogen

    private int colors[]; // the color for every block.
    private int colorsNext[]; // the color for every block next turn
    private int health[]; // the health for every block.

    private final static int DEAD = Color.GRAY; // color for dead tiles
    private final static float MAXSCALING = 2;

    private final static float SIZETILE = 0.95f; // relative size of the block
    private final static float SIZENEXT = 0.3f; // relative size of the smaller block that indicates what happens next turn
    private final static float BORDERSIZE = 40f; // the size of the surrounding border

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
            colors[i] = Color.GRAY; // Wit zien is foute boel!
            colorsNext[i] = Color.GRAY;
        }
    }

    /*******************FUNCTIONS*******************/

    //functions for changing tile colors
    public void setTilePlayer(int x, int y, int col) {
        colors[y * nTilesX + x] = col;
    }

    public void setTileDead(int x, int y) {
        colors[y * nTilesX + x] = DEAD;
    }

    public void setTilePlayerNext(int x, int y, int col) {
        colorsNext[y * nTilesX + x] = col;
    }

    public void setTileDeadNext(int x, int y) {
        colorsNext[y * nTilesX + x] = DEAD;
    }

    public void setTileHealth(int x, int y, int health) {
        this.health[y * nTilesX + x] = health;
    }

    public float relativeX(float pixelX){
        return (pixelX - BORDERSIZE)/tileWidth; // een x-coordinaat proportioneel tot de tiles in de x richting (bijvoorbeeld, 0.5 betekent halverwegen tile 1)
    }

    public float relativeY(float pixelY){
        return (pixelY - BORDERSIZE)/tileHeight; // een y-coordinaat proportioneel tot de tiles in de y richting (of 2.5 betekent halverwegen tile 3)
    }

    /*******************SCALING&DRAGGING*******************/

    //this updates the scaling when zooming in (pinching)
    public void updateScaling(float dScaling, float focusX, float focusY) {
        scaling += dScaling;

        //clamp to [1;MAXSCALING]
        scaling = max(1, min(MAXSCALING, scaling));

        updateOffset(focusX * (dScaling), focusY * (dScaling));
    }

    //this updates the offset used for scrolling across the screen
    public void updateOffset(float dOffX, float dOffY) {
        offsetX += dOffX;
        offsetY += dOffY;

        //clamp to [0;offset]
        offsetX = max(0, min(getWidth()*(scaling - 1), offsetX));
        offsetY = max(0, min(getHeight()*(scaling - 1), offsetY));
    }

    //get the scaled location of n in screen coordinates
    //or: shows where n is on the canvas in pixels.. as if the canvas was fully zoomed out
    public float offX(float n) {
        return (n + offsetX)/scaling;
    }

    public float offY(float n) {
        return (n + offsetY)/scaling;
    }

    /********************DRAW********************/

    @Override
    protected void onDraw(Canvas canvas) {
        //For future arguments: these need to be in onDraw because of screen flipping!
        //Cast to float simply for we aren't using arithmetic over floats
        tileWidth = (canvas.getWidth()-2*BORDERSIZE) / nTilesX;
        tileHeight = (canvas.getHeight()-2*BORDERSIZE) / nTilesY;

        canvas.translate(-offsetX, -offsetY);
        canvas.scale(scaling, scaling);
        canvas.drawColor(Color.DKGRAY); // the inner-border color
        drawBorder(canvas);

        for (int a = 0; a < nTilesX; a++) {
            for (int b = 0; b < nTilesY; b++) {
                drawEmpty(canvas, a, b); // tekent de lege blokken eronder. DIT IS PUUR COSMETISCH!
                drawTile(canvas, a, b); // tekent de huidige blokken
                drawTileNext(canvas, a, b); // tekent de volgende blokken
                //drawTileHealth(canvas, a, b); // tekent de levens van de huidige blokken. OOK DIT IS HOPELIJK TIJDELIJK!
            }
        }

        invalidate();
    }

    private void drawBorder(Canvas canvas){
        Paint p = new Paint();
        p.setColor(Color.DKGRAY); // the border color
        p.setStyle(Paint.Style.STROKE);
        p.setStrokeWidth(2*BORDERSIZE);
        canvas.drawRect(0,0,canvas.getWidth(),canvas.getHeight(),p);
    }

    private void drawEmpty(Canvas canvas, int x, int y){
        Paint p = new Paint();
        p.setColor(DEAD);
        p.setStyle(Paint.Style.FILL_AND_STROKE);
        drawBlock(canvas, p, x, y, SIZETILE);
    }

    //Draw the tile at x,y
    private void drawTile(Canvas canvas, int x, int y) {
        Paint p = new Paint();
        p.setColor(colors[y * nTilesX + x]);
        p.setStyle(Paint.Style.FILL_AND_STROKE);
        drawBlock(canvas, p, x, y, SIZETILE);//TODO: interne pijn lijden omdat hier magic nummers staan (en nee, gewoon wat stic float maken zonder reden is niet de soort van oplosssing die dat verhelpt)
    }

    //Draw the indicator for what the tile will be next turn
    private void drawTileNext(Canvas canvas, int x, int y) {
        Paint p = new Paint();
        p.setColor(colorsNext[y * nTilesX + x]);
        p.setStyle(Paint.Style.FILL_AND_STROKE);
        drawBlock(canvas, p, x, y, SIZENEXT);
    }

    //Draw the indicator for the tiles health
    private void drawTileHealth(Canvas canvas, int x, int y) {
        Paint p = new Paint();
        p.setColor(Color.WHITE);
        p.setStyle(Paint.Style.FILL_AND_STROKE);
        p.setFakeBoldText(true);
        p.setTextSize(40);
        p.setTextAlign(Paint.Align.CENTER);
        canvas.drawText("" + health[y * nTilesX + x], (x + 0.5f) * tileWidth + BORDERSIZE, (y + 0.5f) * tileHeight + BORDERSIZE, p);
    }

    // even een algemene functie geschreven die goed blokjes tekent afhankelijk van een grootte en paint
    // zo wordt het iets makkelijker om dit goed te krijgen
    private void drawBlock(Canvas canvas, Paint p, int x, int y, float size) {
        canvas.drawRect((x + 0.5f * (1 - size)) * tileWidth + BORDERSIZE, (y + 0.5f * (1 - size)) * tileHeight + BORDERSIZE, (x + 0.5f * (1 + size)) * tileWidth + BORDERSIZE, (y + 0.5f * (1 + size)) * tileHeight + BORDERSIZE, p);
    }
}