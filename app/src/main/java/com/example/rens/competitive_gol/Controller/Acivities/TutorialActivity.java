package com.example.rens.competitive_gol.Controller.Acivities;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.example.rens.competitive_gol.Controller.TypeWriter;
import com.example.rens.competitive_gol.R;

import java.util.ArrayList;

public class TutorialActivity extends Activity {

    private Typeface type;
    private TypeWriter textAnimated;
    private ArrayList<String> texts;
    private int textIndex = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        type =  Typeface.createFromAsset(getAssets(), "LCD_Solid.ttf");
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags( WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_tutorial);
        eventToLeftTut();
        eventToRightTut();
        animationOfText();
    }

    private void animationOfText(){
        textAnimated = (TypeWriter) findViewById(R.id.textAnimated);

        textAnimated.setTypeface(type);

        //Add a character every 50ms
        textAnimated.setCharacterDelay(50);

        textAnimated.animateText("Welcome to the tutorial section of this game. In this section the core mechanics of the game will be explained.");
        texts = new ArrayList<>();
        texts.add( "Welcome to the tutorial section of this game. In this section the core mechanics of the game will be explained.");
        texts.add( "The universe of the Game of Life is an infinite two-dimensional orthogonal grid +" +
                        "of square cells, each of which is in one of two possible states, alive or dead, or " +
                        "\"populated\" or \"unpopulated\".");
        texts.add( "Every cell interacts with its eight neighbours," +
                " which are the cells that are horizontally, vertically, or diagonally adjacent.");
        texts.add( "There are four simple rules in this universe:");
        texts.add( "1.Any live cell with fewer than two live neighbours dies, as if caused by underpopulation.\n");
        texts.add("2.Any live cell with two or three live neighbours lives on to the next generation.\n" );
        texts.add("3.Any live cell with more than three live neighbours dies, as if by overpopulation.\n" );
        texts.add("4.Any dead cell with exactly three live neighbours becomes a live cell, as if by reproduction.");
    }

    private void eventToLeftTut(){
        Button btn = (Button) findViewById(R.id.buttonLeftTut);
        btn.setTypeface(type);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textAnimated.animateText("");
                textIndex--;
                if (textIndex < 0) textIndex = texts.size()-1;
                textAnimated.animateText(texts.get(textIndex));
            }
        });

    }
    private void eventToRightTut(){
        Button btn = (Button) findViewById(R.id.buttonRightTut);
        btn.setTypeface(type);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textAnimated.animateText("");
                textIndex++;
                if (textIndex > texts.size()-1) textIndex = 0;
                textAnimated.animateText(texts.get(textIndex));
            }
        });
    }
}
