package com.example.rens.competitive_gol.Controller.Activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.media.AudioManager;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.rens.competitive_gol.R;

public class SettingsActivity extends Activity {

    private AudioManager volume;
    private TextView volumeText;
    private TextView feedback;
    private Typeface type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags( WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_settings);
        type =  Typeface.createFromAsset(getAssets(), "LCD_Solid.ttf");
        volumeText = (TextView) findViewById(R.id.volumeText);
        feedback = (TextView) findViewById(R.id.feedback);
        volumeText.setTypeface(type);
        feedback.setTypeface(type);
        volumeSettings();
        eventToRating();
        eventToFeedBack();
        eventToAbout();
        eventBack();

    }

    private void eventToRating(){
        Button btn = (Button)findViewById(R.id.buttonRate);
        btn.setTypeface(type);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SettingsActivity.this, RatingActivity.class));
            }
        });
    }

    private void volumeSettings(){
        volume = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
        int maxVolume = volume.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        int curVolume = volume.getStreamVolume(AudioManager.STREAM_MUSIC);
        SeekBar volControl = (SeekBar) findViewById(R.id.volumeBar);
        volControl.setMax(maxVolume);
        volControl.setProgress(curVolume);
        volControl.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener(){
            @Override
            public void onStopTrackingTouch(SeekBar arg0){}

            @Override
            public void onStartTrackingTouch(SeekBar arg0){}

            public void onProgressChanged(SeekBar arg0, int arg1, boolean arg2){
               volume.setStreamVolume(AudioManager.STREAM_MUSIC, arg1, 0);
            }
        });
    }

    private void eventToFeedBack(){
        Button btn = (Button)findViewById(R.id.buttonFeedBack);
        btn.setTypeface(type);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SettingsActivity.this, FeedBackActivity.class));
            }
        });
    }

    private void eventToAbout(){
        Button btn = (Button)findViewById(R.id.buttonAbout);
        Typeface type = Typeface.createFromAsset(getAssets(), "LCD_Solid.ttf");
        btn.setTypeface(type);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SettingsActivity.this, AboutActivity.class));
            }
        });
    }

    private void eventBack(){
        Button btn = (Button)findViewById(R.id.settingsBack);
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
