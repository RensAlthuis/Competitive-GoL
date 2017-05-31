package com.example.rens.competitive_gol.Controller.Acivities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.example.rens.competitive_gol.R;

public class FeedBackActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags( WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_feedback);
        eventToSettings();
    }

    private void eventToSettings(){
        Button btn = (Button)findViewById(R.id.buttonSendFeedBack);
        Typeface type = Typeface.createFromAsset(getAssets(), "LCD_Solid.ttf");
        btn.setTypeface(type);
        final Activity activity = this;
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.finish();
            }
        });
    }
}
