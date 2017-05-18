package com.example.rens.competitive_gol.Controller;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.example.rens.competitive_gol.R;

public class SettingsActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags( WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_settings);
        eventToRating();
        eventToFeedBack();
        eventToAbout();
    }

    private void eventToRating(){
        Button btn = (Button)findViewById(R.id.buttonRate);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SettingsActivity.this, RatingActivity.class));
            }
        });
    }

    private void eventToFeedBack(){
        Button btn = (Button)findViewById(R.id.buttonFeedBack);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SettingsActivity.this, FeedBackActivity.class));
            }
        });
    }

    private void eventToAbout(){
        Button btn = (Button)findViewById(R.id.buttonAbout);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SettingsActivity.this, AboutActivity.class));
            }
        });
    }
}
