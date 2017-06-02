package com.example.rens.competitive_gol.Controller;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * This class allow for text to be drawn on the screen a letter at a time.
 */
public class TypeWriter extends android.support.v7.widget.AppCompatTextView{

    private CharSequence mText;
    private int mIndex;
    private long mDelay = 500;

    public TypeWriter(Context context){
        super(context);
    }

    public TypeWriter(Context context, AttributeSet attrs){
        super(context, attrs);
    }

    private Handler mHandler = new Handler();

    private Runnable characterAdder = new Runnable(){
        @Override
        public void run() {
            setText(mText.subSequence(0, mIndex++));
            if(mIndex <= mText.length()){
                mHandler.postDelayed(characterAdder, mDelay);
            }
        }
    };

    /**
     * This function animates the actual text.
     * @param text The text to appear on screen.
     */
    public void animateText(CharSequence text){
        mText = text;
        mIndex = 0;

        setText("");
        mHandler.removeCallbacks(characterAdder);
        mHandler.postDelayed(characterAdder, mDelay);
    }

    /**
     * This function sets the delay between letters appearing.
     * @param millis the number of milliseconds between the appearance of each letter.
     */
    public void setCharacterDelay(long millis){
        mDelay = millis;
    }
}
