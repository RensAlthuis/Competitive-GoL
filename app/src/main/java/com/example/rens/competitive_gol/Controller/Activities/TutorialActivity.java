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
    private ArrayList<String> texts1;
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
        texts1 = new ArrayList<>();
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

        texts1.add("After you touch the start game button you have to create a room. In here you can choose which game mode you want to play." +
                "You can choose between local player vs player mode or player vs ai mode. And between Ai you can choose the hard Ai or easy Ai.");
        texts1.add("You can also choose how many tiles you can choose every turn. The board size and time is also settable." +
                "At the end you can choose your own color and your opponents color. If you are done choosing you can start the game.");
        texts1.add("In the game you can choose a tile that you want to set each turn. " +
                "If you want to change the tile, you can undo your tile set." +
                "Then you can click next and wait till your opponent set a tile as well.");
        texts1.add("The Hard Ai and Easy Ai take a bit of time to set their tiles on the right coordinates" +
                "The numbers that are represented on the left is the amount of time left that you have set" +
                "And on the right is the amount of steps were made this time.");
        texts1.add("Have fun playing and Git Gud!");
    }

    private void eventToLeftTut(){
        Button btn = (Button) findViewById(R.id.buttonLeftTut);
        btn.setTypeface(type);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textAnimated.animateText("");
                textAnimated1.animateText("");
                textIndex--;
                if (textIndex < 0){
                    switcher.showNext();
                    textIndex = texts1.size()-1;
                    textAnimated1.animateText(texts1.get(textIndex));
                }
                else textAnimated.animateText(texts.get(textIndex));
                fixPictures(textIndex);
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
                textAnimated1.animateText("");
                textIndex++;
                if(textIndex == texts.size()) {
                    switcher.showNext();
                    textIndex = 0;
                    textAnimated1.animateText(texts1.get(textIndex));
                }
                else textAnimated.animateText(texts.get(textIndex));
                fixPictures(textIndex);
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
                textAnimated1.animateText("");
                textIndex--;
                if (textIndex < 0){
                    switcher.showPrevious();
                    textIndex = texts.size()-1;
                    textAnimated.animateText(texts.get(textIndex));
                }
                textAnimated1.animateText(texts1.get(textIndex));
                fixPictures(textIndex);
            }
        });

    }

    private void eventToRightTut1(){
        Button btnRight = (Button) findViewById(R.id.buttonRightTut1);
        btnRight.setTypeface(type);
        btnRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textAnimated.animateText("");
                textAnimated1.animateText("");
                textIndex++;
                fixPictures(textIndex);
                if(textIndex == texts1.size()) {
                    switcher.showPrevious();
                    textIndex = 0;
                    textAnimated.animateText(texts.get(textIndex));
                }
                else textAnimated1.animateText(texts1.get(textIndex));
                fixPictures(textIndex);

            }
        });
    }

    private void fixPictures(int textIndex){
        findViewById(R.id.plaatje0).setVisibility(View.GONE);
        findViewById(R.id.plaatje1).setVisibility(View.GONE);
        findViewById(R.id.plaatje2).setVisibility(View.GONE);
        if(textIndex == 0) {
            findViewById(R.id.plaatje0).setVisibility(View.VISIBLE);
        }
        if(textIndex == 1){
            findViewById(R.id.plaatje1).setVisibility(View.VISIBLE);
        }else if(textIndex == 2 || textIndex == 3 || textIndex == 4){
            findViewById(R.id.plaatje2).setVisibility(View.VISIBLE);
        }
    }
}
