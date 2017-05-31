package com.example.rens.competitive_gol.Controller;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.rens.competitive_gol.R;

public class RoomActivity extends Activity {

    private String[] arraySpinner;
    private TextView gameMode;
    private TextView diffGameMode;
    private TextView boardSize;
    private TextView timeLimit;
    private TextView characters;
    private TextView boardWidth;
    private TextView boardHeight;
    private TextView crossText;
    private Typeface type;
    private ImageView colourChoice;
    private static final int[] colours = {0xffef5350, 0xffec407a, 0xffab47bc, Color.BLUE,Color.RED,Color.GREEN,Color.MAGENTA,Color.YELLOW,Color.CYAN};
    private int currentColour = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags( WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_room);


        this.arraySpinner = new String[]{
                "2 min", "5 min", "10 min", "20 min"
        };
        Spinner s = (Spinner) findViewById(R.id.spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, arraySpinner);
        s.setAdapter(adapter);

        type =  Typeface.createFromAsset(getAssets(), "LCD_Solid.ttf");
        changeFonts();
        colourChoice = (ImageView)findViewById(R.id.colourChoice);
        colourChoice.setBackgroundColor(colours[currentColour]);
        eventToCreate();
        eventToLeftMode();
        eventToRightMode();
        eventToLeftChar();
        eventToRightChar();
        //eventToSpinner();
   }

   private void changeFonts(){
       gameMode = (TextView) findViewById(R.id.gameMode);
       diffGameMode = (TextView) findViewById(R.id.gameModes);
       boardSize = (TextView) findViewById(R.id.boardSize);
       timeLimit = (TextView) findViewById(R.id.timeLimit);
       characters = (TextView) findViewById(R.id.characters);
       boardWidth = (TextView) findViewById(R.id.boardWidth);
       boardHeight = (TextView) findViewById(R.id.boardHeight);
       crossText = (TextView) findViewById(R.id.crossText);
       gameMode.setTypeface(type);
       diffGameMode.setTypeface(type);
       boardSize.setTypeface(type);
       timeLimit.setTypeface(type);
       characters.setTypeface(type);
       boardWidth.setTypeface(type);
       boardHeight.setTypeface(type);
       crossText.setTypeface(type);
   }


    private void eventToCreate() {
        Button btn = (Button)findViewById(R.id.buttonCreateRoom);
        btn.setTypeface(type);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RoomActivity.this, MainActivity.class);
                intent.putExtra("Player1", colours[currentColour]);
                intent.putExtra("Player2", colours[(currentColour +1) % colours.length]);
                startActivity(intent);
            }
        });
    }

    /*private void eventToSpinner(){
        Button btn = (Button)findViewById(R.id.buttonRateSend);
        //btn.setTypeface(type);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RoomActivity.this, MainActivity.class));
            }
        });
    }*/

    private void eventToLeftMode(){
        Button btn = (Button) findViewById(R.id.buttonModeLeft);
        btn.setTypeface(type);
    }

    private void eventToRightMode(){
        Button btn = (Button) findViewById(R.id.buttonModeRight);
        btn.setTypeface(type);
    }

    private void eventToLeftChar(){
        Button btn = (Button) findViewById(R.id.buttonLeftChar);
        btn.setTypeface(type);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               currentColour = (currentColour - 1 < 0)? colours.length-1 : currentColour -1;
                colourChoice.setBackgroundColor(colours[currentColour]);
            }
        });
    }
    private void eventToRightChar(){
        Button btn = (Button) findViewById(R.id.buttonRightChar);
        btn.setTypeface(type);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentColour = (currentColour + 1) % colours.length;
                colourChoice.setBackgroundColor(colours[currentColour]);
            }
        });
    }

}
