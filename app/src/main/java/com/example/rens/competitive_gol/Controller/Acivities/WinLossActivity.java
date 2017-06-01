package com.example.rens.competitive_gol.Controller.Acivities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.content.res.AppCompatResources;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.example.rens.competitive_gol.R;

/**
 * Created by Lenovo on 1-6-2017.
 */

public class WinLossActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags( WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_dead);

        int back = getIntent().getIntExtra("winner", 0);
        int drawable;
        switch (back) {
            case 0:
                 drawable = R.drawable.defeat;
                break;
            case 1:
                drawable = R.drawable.liveplayer1;
                break;
            case 2:
                drawable = R.drawable.liveplayer2;
                break;
            default:
                drawable = R.drawable.live;
                break;
        }

        ConstraintLayout constraintLayout = (ConstraintLayout)findViewById(R.id.winLossLayout);
        constraintLayout.setBackgroundResource(drawable);
        eventToRoom();
    }

    private void eventToRoom(){
        Button btn = (Button)findViewById(R.id.buttonDead);
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
