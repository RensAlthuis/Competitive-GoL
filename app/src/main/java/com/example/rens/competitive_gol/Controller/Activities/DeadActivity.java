package com.example.rens.competitive_gol.Controller.Activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.example.rens.competitive_gol.R;

/**
 * Created by Lenovo on 1-6-2017.
 */

public class DeadActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags( WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_dead);
        eventToRoom();
    }

    private void eventToRoom(){
        Button btn = (Button)findViewById(R.id.buttonDead);
        Typeface type = Typeface.createFromAsset(getAssets(), "LCD_Solid.ttf");
        btn.setTypeface(type);
        final Activity activity = this;
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DeadActivity.this, RoomActivity.class));
            }
        });
    }
}
