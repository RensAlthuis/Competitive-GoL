package com.example.rens.competitive_gol.Controller.Activities;

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
    private ImageView colourChoice1;
    private Typeface type;
    private TextView amountSteps;
    private TextView steps;
    private ImageView colourChoice;
    private final int[] colours = {0xffef5350, 0xffec407a, 0xffab47bc, Color.BLUE,Color.RED,Color.GREEN,Color.MAGENTA,Color.YELLOW,Color.CYAN};
    private final String[] gameModes = {"Player VS. Player", "Player VS. Easy AI", "Player VS. Hard AI"};
    private int stepChoice = 1;
    private int boardSizeChoice = 10;
    private int currentColour = 0;
    private int currentColour1 = 1;
    private int currentGame = 0;
    private Spinner timeLimitSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags( WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_room);


        this.arraySpinner = new String[]{
                "None", "2 min", "5 min", "10 min", "20 min"
        };
        Spinner s = (Spinner) findViewById(R.id.spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, arraySpinner);
        s.setAdapter(adapter);

        type =  Typeface.createFromAsset(getAssets(), "LCD_Solid.ttf");
        changeFonts();
        colourChoice = (ImageView)findViewById(R.id.colourChoice);
        colourChoice.setBackgroundColor(colours[currentColour]);
        colourChoice1 = (ImageView)findViewById(R.id.colourChoice1);
        colourChoice1.setBackgroundColor(colours[currentColour1]);
        eventToCreate();
        eventToLeftMode();
        eventToRightMode();
        eventToLeftChar();
        eventToRightChar();
        eventToLeftSteps();
        eventToRightSteps();
        eventToLeftChar1();
        eventToRightChar1();
        eventToLeftBoard();
        eventToRightBoard();
        eventToSpinner();
   }

   private void changeFonts(){
       gameMode = (TextView) findViewById(R.id.gameMode);
       diffGameMode = (TextView) findViewById(R.id.diffGameModes);
       amountSteps = (TextView) findViewById(R.id.amountSteps);
       steps = (TextView) findViewById(R.id.steps);
       boardSize = (TextView) findViewById(R.id.boardSize);
       timeLimit = (TextView) findViewById(R.id.timeLimit);
       characters = (TextView) findViewById(R.id.characters);
       boardWidth = (TextView) findViewById(R.id.boardWidth);
       boardHeight = (TextView) findViewById(R.id.boardHeight);
       crossText = (TextView) findViewById(R.id.crossText);
       gameMode.setTypeface(type);
       diffGameMode.setTypeface(type);
       amountSteps.setTypeface(type);
       steps.setTypeface(type);
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
                finish();
                Intent intent = new Intent(RoomActivity.this, MainActivity.class);
                intent.putExtra("Player1", colours[currentColour]);
                intent.putExtra("Player2", colours[currentColour1]);
                intent.putExtra("gameMode", currentGame);
                intent.putExtra("steps", stepChoice);
                intent.putExtra("boardSize", boardSizeChoice);
                intent.putExtra("Time", (String) timeLimitSpinner.getSelectedItem());
                startActivity(intent);
            }
        });
    }

    private void eventToSpinner(){
        timeLimitSpinner = (Spinner)findViewById(R.id.spinner);
    }

    private void eventToRightMode(){
        Button btn = (Button) findViewById(R.id.buttonModeRight);
        btn.setTypeface(type);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentGame = (currentGame + 1) % gameModes.length;
                diffGameMode.setText(gameModes[currentGame]);
            }
        });
    }

    private void eventToLeftMode(){
        Button btn = (Button) findViewById(R.id.buttonModeLeft);
        btn.setTypeface(type);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentGame = (currentGame - 1 < 0)? gameModes.length-1 : currentGame -1;
                diffGameMode.setText(gameModes[currentGame]);
            }
        });
    }

    private void eventToLeftSteps(){
        Button btn = (Button) findViewById(R.id.buttonStepsLeft);
        btn.setTypeface(type);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stepChoice = (stepChoice == 1) ? 4 : stepChoice - 1;
                steps.setText("" + stepChoice);
            }
        });
    }

    private void eventToRightSteps(){
        Button btn = (Button) findViewById(R.id.buttonStepsRight);
        btn.setTypeface(type);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stepChoice = (stepChoice == 4) ? 1 : stepChoice + 1;
                steps.setText("" + stepChoice);
            }
        });
    }

    private void eventToLeftBoard(){
        Button btn = (Button) findViewById(R.id.buttonLeftBoard);
        btn.setTypeface(type);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boardSizeChoice = (boardSizeChoice == 10) ? 25 : boardSizeChoice - 5;
                boardWidth.setText("" + boardSizeChoice);
                boardHeight.setText("" + boardSizeChoice);
            }
        });
    }

    private void eventToRightBoard(){
        Button btn = (Button) findViewById(R.id.buttonRightBoard);
        btn.setTypeface(type);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boardSizeChoice = (boardSizeChoice == 25) ? 10 : boardSizeChoice + 5;
                boardWidth.setText("" + boardSizeChoice);
                boardHeight.setText("" + boardSizeChoice);
            }
        });
    }



    private void eventToLeftChar(){

        Button btn = (Button) findViewById(R.id.buttonLeftChar);
        btn.setTypeface(type);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentColour = (currentColour - 1 < 0)? colours.length-1 : currentColour -1;
                if(currentColour1 == currentColour) currentColour = (currentColour - 1 < 0)? colours.length-1 : currentColour -1;
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
                if(currentColour1 == currentColour) currentColour = (currentColour + 1) % colours.length;
                colourChoice.setBackgroundColor(colours[currentColour]);
            }
        });
    }

    private void eventToLeftChar1(){
        Button btn = (Button) findViewById(R.id.buttonLeftChar1);
        btn.setTypeface(type);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentColour1 = (currentColour1 - 1 < 0)? colours.length-1 : currentColour1 -1;
                if(currentColour1 == currentColour) currentColour1 = (currentColour1 - 1 < 0)? colours.length-1 : currentColour1 -1;
                colourChoice1.setBackgroundColor(colours[currentColour1]);
            }
        });
    }

    private void eventToRightChar1(){
        Button btn = (Button) findViewById(R.id.buttonRightChar1);
        btn.setTypeface(type);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentColour1 = (currentColour1 + 1) % colours.length;
                if(currentColour1 == currentColour) currentColour1 = (currentColour1 + 1) % colours.length;
                colourChoice1.setBackgroundColor(colours[currentColour1]);
            }
        });
    }
}
