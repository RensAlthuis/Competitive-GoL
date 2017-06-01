package com.example.rens.competitive_gol.Controller.Activities;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ViewSwitcher;

import com.example.rens.competitive_gol.Controller.TypeWriter;
import com.example.rens.competitive_gol.R;

import java.util.ArrayList;

public class TutorialActivity extends Activity {

    private Typeface type;
    private TypeWriter textAnimated;
    private ArrayList<String> texts;
    private int textIndex = 0;
    private TypeWriter textAnimated1;
    private ViewSwitcher switcher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        type =  Typeface.createFromAsset(getAssets(), "LCD_Solid.ttf");
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags( WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_tutorial);
        switcher = (ViewSwitcher) findViewById(R.id.switcher);
        eventToLeftTut();
        eventToRightTut();
        eventToLeftTut1();
        eventToRightTut1();
        animationOfText();
    }

    private void animationOfText(){
        textAnimated = (TypeWriter) findViewById(R.id.textAnimated);
        textAnimated1 = (TypeWriter) findViewById(R.id.textAnimated1);
        textAnimated1.setTypeface(type);
        textAnimated1.setCharacterDelay(50);

        textAnimated.setTypeface(type);

        //Add a character every 25ms
        textAnimated.setCharacterDelay(50);

        textAnimated.animateText("Welcome to the tutorial section of this game. In this section the core mechanics of the game will be explained." +
                    "The universe of the Game of Life is an infinite two-dimensional orthogonal grid" +
                    "of square cells, each of which is in one of two possible states, alive or dead, or " +
                    "\"populated\" or \"unpopulated\"." +
                    "Every cell interacts with its eight neighbours," +
                    " which are the cells that are horizontally, vertically, or diagonally adjacent.");

        texts = new ArrayList<>();
        texts.add("Welcome to the tutorial section of this game. In this section the core mechanics of the game will be explained." +
                "The universe of the Game of Life is an infinite two-dimensional orthogonal grid" +
                "of square cells, each of which is in one of two possible states, alive or dead, or " +
                "\"populated\" or \"unpopulated\"." +
                "Every cell interacts with its eight neighbours," +
                " which are the cells that are horizontally, vertically, or diagonally adjacent.");
        texts.add("There are four simple rules in this universe:" +
                "\n\n1.Any live cell with fewer than two live neighbours dies, as if caused by underpopulation." +
                "\n\n2.Any live cell with two or three live neighbours lives on to the next generation." +
                "\n\n3.Any live cell with more than three live neighbours dies, as if by overpopulation." +
                "\n\n4.Any dead cell with exactly three live neighbours becomes a live cell, as if by reproduction.");
        texts.add("The initial pattern constitutes the seed of the system. The first generation is " +
                "created by applying the previously mentioned rules simultaneously to every cell in the seed—births " +
                "and deaths occur simultaneously, and the discrete moment at which this happens is " +
                "sometimes called a tick (in other words, each generation is a pure function of the preceding one). " +
                "The rules continue to be applied repeatedly to create further generations.");
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
        Button btnRight = (Button) findViewById(R.id.buttonRightTut);
        btnRight.setTypeface(type);
        btnRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textAnimated.animateText("");
                textIndex++;
                if(textIndex > texts.size() - 1) {
                    switcher.showNext();
                    textIndex = 0;
                }
                else textAnimated.animateText(texts.get(textIndex));
            }
        });
    }

    private void eventToLeftTut1(){
        Button btn = (Button) findViewById(R.id.buttonLeftTut1);
        btn.setTypeface(type);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textAnimated.animateText("");
                textIndex--;
                if (textIndex < 0){
                    switcher.showPrevious();
                    textIndex = texts.size();
                }
                textAnimated1.animateText(texts.get(textIndex));
            }
        });

    }
    private void eventToRightTut1(){
        Button btnRight = (Button) findViewById(R.id.buttonRightTut1);
        btnRight.setTypeface(type);
        btnRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(textIndex > texts.size() - 1) {
                    switcher.showPrevious();
                    textIndex = 0;
                }
                else textAnimated1.animateText(texts.get(textIndex));
                textIndex++;
            }
        });
    }


}