package com.example.rens.competitive_gol.Controller.Activities;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.example.rens.competitive_gol.R;

public class AboutActivity extends Activity {

    private TextView aboutUs;
    private Typeface type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags( WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_about);
        type =  Typeface.createFromAsset(getAssets(), "LCD_Solid.ttf");
        aboutUs = (TextView) findViewById(R.id.aboutUs);
        aboutUs.setTypeface(type);
        eventBack();
    }

    private void eventBack(){
        Button btn = (Button)findViewById(R.id.aboutBack);
        Typeface type = Typeface.createFromAsset(getAssets(), "LCD_Solid.ttf");
        btn.setTypeface(type);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

}
