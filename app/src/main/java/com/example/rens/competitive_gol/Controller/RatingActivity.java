package com.example.rens.competitive_gol.Controller;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RatingBar;

import com.example.rens.competitive_gol.R;

public class RatingActivity extends Activity {

    private RatingBar ratingBar;
    private Button btnSumbit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags( WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_rating);
        eventToSettings();
        eventOnListeningButton();
    }

    private void eventOnListeningButton(){
        ratingBar = (RatingBar) findViewById(R.id.ratingBar);
        btnSumbit = (Button) findViewById(R.id.buttonRateSend);

        //if click on me, then send the rating to ...
        btnSumbit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {

            }
        });
    }
    private void eventToSettings(){
        Button btn = (Button)findViewById(R.id.buttonRateSend);
        Typeface type = Typeface.createFromAsset(getAssets(), "LCD_Solid.ttf");
        btn.setTypeface(type);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RatingActivity.this, SettingsActivity.class));
            }
        });
    }
}
