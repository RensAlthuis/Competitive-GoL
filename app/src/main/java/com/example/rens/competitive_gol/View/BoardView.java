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

import com.example.rens.competitive_gol.Model.Board;
import com.example.rens.competitive_gol.Model.TileSettings;

import static java.lang.Math.max;
import static java.lang.Math.min;

/**
 * Created by Tom on 12-5-2017.
 */

public class BoardView extends View {

    /*******************VARIABLES*******************/
    public static float hitX; //MouseX, public and static(perhaps relevant in other classes)
    public static float hitY; //MouseY, static and public as well

    private ScaleGestureDetector mScaleDetector;
    private GestureDetector mGestureDetector;

    private Paint field = new Paint();
    private Paint recBorder = new Paint();
    private Paint rectangle = new Paint();
    private Paint recTOne = new Paint(); //Rectangle belonging to team one!

    private Board board;

    private float tileWidth;
    private float tileHeight;
    private float scaling = 1; //May the gods be with us
    private float offsetX = 0;
    private float offsetY =0;
    private float oldScaleFactor;

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

    /*******************FUNCTIONS*******************/
    public void init(Context context){

        //INITIALIZING VARIABLES
        //We create a new board IN the view. This is so we dont need unnecessary references
        TileSettings settings = new TileSettings();
        board = new Board(20,20,settings);

        rectangle.setStyle(Paint.Style.FILL_AND_STROKE);
        field.setStyle(Paint.Style.FILL_AND_STROKE);
        rectangle.setColor(Color.GRAY);
        recBorder.setColor(Color.BLACK);
        field.setColor(Color.WHITE);
        recTOne.setColor(Color.GREEN);
        recBorder.setStyle(Paint.Style.STROKE);
        recBorder.setStrokeWidth(4);

        oldScaleFactor = 1;

        //INITIALIZING DETECTORS
        mScaleDetector = new ScaleGestureDetector(context, new ScaleGestureDetector.OnScaleGestureListener() {

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
                scaling += dScaling;
                //clamp to [1;2]
                scaling = max(1,min(2,scaling));

                if(scaling > 0.1) {
                    offsetX += detector.getFocusX() * (dScaling);
                    offsetY += detector.getFocusY() * (dScaling);
                    int maxpiv = (int) (getWidth() * (scaling - 1));
                    //clamp to [0;offset]
                    offsetX = max(0, min(maxpiv, offsetX));
                    offsetY = max(0, min(maxpiv, offsetY));
                }

                oldScaleFactor = detector.getScaleFactor();

                return false;
            }
        });

        mGestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {

            @Override
            public boolean onSingleTapUp(MotionEvent e) {

                float offset = tileWidth * scaling;

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
                        }
                    }
                return false;
            }

            @Override
            public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {

                offsetX += distanceX;
                offsetY += distanceY;

                int maxpiv = (int) (getWidth() * (scaling - 1));

                //clamp to [0;offset]
                offsetX = max(0, min(maxpiv, offsetX));
                offsetY = max(0, min(maxpiv, offsetY));

                return false;
            }
        });
    }

    @Override
    protected void onDraw(Canvas canvas)
    {
        //For future arguments: these need to be in onDraw because of screen flipping!
        //Cast to float simply for we aren't using arithmetic over floats
        tileWidth = (float) (canvas.getWidth())/board.width;
        tileHeight = (float) (canvas.getHeight())/board.height;

        canvas.translate(-offsetX,-offsetY);
        canvas.scale(scaling,scaling);
        canvas.drawColor(Color.RED);

        for(int a=0;a < board.width; a++)
            for(int b=0;b < board.height; b++)
            {
                if(board.getTileAt(a,b).getTeam()==0) // Box for dead tiles
                    canvas.drawRect(a * tileWidth, b * tileHeight,(a+1) * tileWidth, (b+1) * tileHeight, rectangle);
                else //Box for team one
                    canvas.drawRect(a * tileWidth,b * tileHeight,(a+1) * tileWidth, (b+1) * tileHeight, recTOne);

                //Borders
                canvas.drawRect(a * tileWidth, b * tileHeight, (a+1) * tileWidth,(b+1) * tileHeight, recBorder);
            }


    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        hitX = event.getX() + offsetX; //Floats for extra precision!
        hitY = event.getY() + offsetY;

        mScaleDetector.onTouchEvent(event);
        mGestureDetector.onTouchEvent(event);

        invalidate();
        return true;
    }
}
