package com.example.rens.competitive_gol.Controller;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.example.rens.competitive_gol.R;
import com.example.rens.competitive_gol.View.BoardView;

public class MenuActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags( WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_menu);
        createTitle();
        eventToMain();
        eventToOnline();
        eventToSettings();
        eventToTutorial();
    }

    private void createTitle(){
        BoardView board = (BoardView) findViewById(R.id.boardTitle);
        //board

    }

    private void eventToMain() {
        Button btn = (Button)findViewById(R.id.buttonAi);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MenuActivity.this, MainActivity.class));
            }
        });
    }

    private void eventToOnline() {
        Button btn = (Button)findViewById(R.id.buttonPlayer);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MenuActivity.this, OnlineActivity.class));
            }
        });
    }

    private void eventToTutorial() {
        Button btn = (Button)findViewById(R.id.buttonTut);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MenuActivity.this, TutorialActivity.class));
            }
        });
    }

    private void eventToSettings() {
        Button btn = (Button)findViewById(R.id.buttonSet);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MenuActivity.this, SettingsActivity.class));
            }
        });
    }


}
