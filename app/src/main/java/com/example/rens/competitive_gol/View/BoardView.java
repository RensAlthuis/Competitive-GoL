package com.example.rens.competitive_gol.View;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;

import com.example.rens.competitive_gol.Model.Board;
import com.example.rens.competitive_gol.Model.TileSettings;

/**
 * Created by Tom on 12-5-2017.
 */

public class BoardView extends View {
    public static float mousex; //MouseX, public and static(perhaps relevant in other classes)
    public static float mousey; //MouseY, static and public as well

    private ScaleGestureDetector mScaleDetector;
    private GestureDetector mGestureDetector;
    private Paint field = new Paint();
    private Paint recBorder = new Paint();
    private Paint rectangle = new Paint();
    private Paint recTOne = new Paint(); //Rectangle belonging to team one!
    private Board board;
    private float hitbox_x;
    private float hitbox_y;
    private float scaling = 1; //May the gods be with us
    private float pivx=0;
    private float pivy=0;

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

    public void init(Context context){
        //super(context);
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

        mScaleDetector = new ScaleGestureDetector(context, new ScaleGestureDetector.OnScaleGestureListener() {
            @Override
            public void onScaleEnd(ScaleGestureDetector detector) {
            }
            @Override
            public boolean onScaleBegin(ScaleGestureDetector detector) {
                Log.d("sure", String.valueOf(detector.getFocusX()));
                return true;
            }
            @Override
            public boolean onScale(ScaleGestureDetector detector) {
                scaling = detector.getScaleFactor();
                if (scaling < 1) scaling =  1;
                if (scaling > 2) scaling =  2;
                pivx = detector.getFocusX()*(scaling-1);
                pivy = detector.getFocusY()*(scaling-1);
                Log.d("UDEBUG_scaling","scaling " + scaling );
                return false;
            }
        });
        mGestureDetector = new GestureDetector(context, new GestureDetector.OnGestureListener() {
            @Override
            public boolean onDown(MotionEvent e) {
                return false;
            }

            @Override
            public void onShowPress(MotionEvent e) {

            }

            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                for(int a=0; a<board.width;a++)
                    for(int b=0; b<board.height;b++)
                    {
                        if(mousex> ((a*hitbox_x)*scaling) && mousex < (((a+1)*hitbox_x)*scaling)
                                && mousey> ((b*hitbox_y)*scaling) && mousey< (((b+1)*hitbox_y)*scaling))
                        {
                            board.getTileAt(a,b).Set(1);
                        }
                    }
                return false;
            }

            @Override
            public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                pivx += distanceX;
                pivy += distanceY;
                if (pivx < 0) pivx =  0;
                if (pivy < 0) pivy =  0;
                int maxpiv = (int)(getWidth() * (scaling-1));
                if (pivx > maxpiv) pivx = maxpiv;
                if (pivy > maxpiv) pivy = maxpiv;
                Log.d("UDEBUG_pivx", "pivx " + pivx);
                Log.d("UDEBUG_maxpiv", "maxpiv " + maxpiv);
                Log.d("UDEBUG_maxWidth", "maxWidth " + getWidth());
                return false;
            }

            @Override
            public void onLongPress(MotionEvent e) {

            }

            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {

                return false;
            }
        });
    }

    @Override
    protected void onDraw(Canvas canvas)
    {
        //For future arguments: these need to be in onDraw because of screen flipping!
        //Cast to float simply for we aren't using arithmetic over floats
        hitbox_x = (float) (canvas.getWidth())/board.width;
        canvas.translate(-pivx,-pivy);
        canvas.scale(scaling,scaling);
        hitbox_y = (float) (canvas.getHeight())/board.height;
        canvas.drawColor(Color.RED);
        for(int a=0;a < board.width; a++)
            for(int b=0;b < board.height; b++)
            {
                if(board.getTileAt(a,b).getTeam()==0)
                canvas.drawRect(a*hitbox_x,b*hitbox_y,(a+1)*hitbox_x,(b+1)*hitbox_y, rectangle);
                else canvas.drawRect(a*hitbox_x,b*hitbox_y,(a+1)*hitbox_x,(b+1)*hitbox_y, recTOne);
                canvas.drawRect(a*hitbox_x,b*hitbox_y,(a+1)*hitbox_x,(b+1)*hitbox_y, recBorder);
            }


    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mousex = event.getX()+pivx; //Floats for extra precision!
        mousey = event.getY()+pivy;

        mScaleDetector.onTouchEvent(event);
        mGestureDetector.onTouchEvent(event);

        invalidate();
        return true;
    }
}
