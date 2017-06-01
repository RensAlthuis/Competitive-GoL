package com.example.rens.competitive_gol.Controller;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by Lenovo on 1-6-2017.
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

    public void animateText(CharSequence text){
        mText = text;
        mIndex = 0;

        setText("");
        mHandler.removeCallbacks(characterAdder);
        mHandler.postDelayed(characterAdder, mDelay);
    }

    public void setCharacterDelay(long millis){
        mDelay = millis;
    }
}
