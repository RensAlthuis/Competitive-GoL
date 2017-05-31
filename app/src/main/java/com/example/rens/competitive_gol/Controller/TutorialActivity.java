package com.example.rens.competitive_gol.Controller;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.example.rens.competitive_gol.R;

public class TutorialActivity extends Activity {

    private Typeface type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        type =  Typeface.createFromAsset(getAssets(), "LCD_Solid.ttf");

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags( WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_tutorial);
        eventToLeftTut();
        eventToRightTut();
    }

    private void eventToLeftTut(){
        Button btn = (Button) findViewById(R.id.buttonLeftTut);
        btn.setTypeface(type);
    }
    private void eventToRightTut(){
        Button btn = (Button) findViewById(R.id.buttonRightTut);
        btn.setTypeface(type);
    }
}
