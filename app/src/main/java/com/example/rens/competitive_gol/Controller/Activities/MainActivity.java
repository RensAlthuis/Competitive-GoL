package com.example.rens.competitive_gol.Controller.Activities;

import com.example.rens.competitive_gol.Controller.BoardController;
import com.example.rens.competitive_gol.Model.AIPlayer;
import com.example.rens.competitive_gol.Model.Board;
import com.example.rens.competitive_gol.Model.EasyStrategy;
import com.example.rens.competitive_gol.Model.HardStrategy;
import com.example.rens.competitive_gol.Model.Player;
import com.example.rens.competitive_gol.Model.TileSettings;
import com.example.rens.competitive_gol.R;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends Activity {

    /*******************VARIABLES*******************/

    private static BoardController game; //Het actieve spel atm
    private static int FRACTIONRANDOM = 5; // is de fractie van het bord (als 1/..) dat wordt gevuld door random tiles
    private ImageView character;
    private int gameMode;
    private CountDownTimer timer;
    private TextView[] time;
    int maxTime;

    /***********************************************/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags( WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        time = new TextView[2];
        time[0] = (TextView)findViewById(R.id.timerText);
        time[1] = (TextView)findViewById(R.id.timerText2);

        // Het spel:
        gameMode = getIntent().getIntExtra("gameMode", 0);
        makeGame(gameMode);

        character = (ImageView)findViewById(R.id.character);
        updateCharacterIcon();

        // De next turn knop:
        Button btnNext = (Button)findViewById(R.id.buttonNext);
        Typeface type = Typeface.createFromAsset(getAssets(), "LCD_Solid.ttf");
        btnNext.setTypeface(type);
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                nextTurn();

                if(gameMode != 0){
                    ((AIPlayer)game.getPlayer(1)).makeNextMove();
                    nextTurn();
                }
            }
        });

        Button btnUndo = (Button)findViewById(R.id.buttonUndo);
        Typeface typeUndo = Typeface.createFromAsset(getAssets(), "LCD_Solid.ttf");
        btnUndo.setTypeface(typeUndo);
        btnUndo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                game.undoMove();
            }
        });

    }

    private void updateCharacterIcon(){
        character.setBackgroundColor(game.curColor());
    }

    private void nextTurn(){
        game.update();
        int win = game.winCheck();
        if(win != -1){
            toWinLoss(win);
        }
        game.nextPlayer(); // updaten -> gewonnen? -> volgende speler
        updateCharacterIcon();
        if(maxTime != -1) {
            stopTimer();
            startTimer(game.getPlayer(game.curTeam()).currentTime, time[game.curTeam()]);
        }
    }

    /********************MAKING THE GAME********************/

    private void makeGame(int gameMode){

        /********************BOARDSETTINGS********************/
        final int steps = getIntent().getIntExtra("steps", 1);
        final int size  = getIntent().getIntExtra("boardSize", 10);

        game = new BoardController(this, this, new Board(size,size,new TileSettings()), steps);

        /********************PLAYERS********************/
        final int col1 = getIntent().getIntExtra("Player1", 0);
        final int col2 = getIntent().getIntExtra("Player2", 0);


        game.addPlayer(new Player(0, col1));

        if(gameMode == 0) game.addPlayer(new Player(1, col2));
        else              game.addPlayer(new AIPlayer(1, col2, game, (gameMode == 1)? new EasyStrategy() : new HardStrategy()));


        /********************TIMERS********************/
        final String maxTimeStr = getIntent().getStringExtra("Time");
        if(maxTimeStr.equals("2 min")) {
            maxTime = 2*60;
        }else if(maxTimeStr.equals("5 min")){
            maxTime = 5*60;
        }else if(maxTimeStr.equals("10 min")){
            maxTime = 10*60;
        }else if(maxTimeStr.equals("20 min")){
            maxTime = 20*60;
        }
        else{
            maxTime = -1;
        }


        time[0].setTypeface(Typeface.createFromAsset(getAssets(), "LCD_Solid.ttf"));
        time[1].setTypeface(Typeface.createFromAsset(getAssets(), "LCD_Solid.ttf"));
        time[0].setTextSize(40);
        time[1].setTextSize(40);
        game.getPlayer(0).currentTime = maxTime*1000;
        game.getPlayer(1).currentTime = maxTime*1000;
        if(maxTime != -1) {
            time[0].setText("" + game.getPlayer(0).currentTime/1000);
            time[1].setText("" + game.getPlayer(1).currentTime/1000);
        }else{
            time[0].setText("0");
            time[1].setText("0");
        }

        /********************BOARD********************/
        game.setRandomBoard(size*size/FRACTIONRANDOM);
    }

    @Override
    protected void onDestroy() {
        if(maxTime != -1)
            stopTimer();
        super.onDestroy();
    }

    private void toWinLoss(int winner) {
        finish();
        Intent intent = new Intent(MainActivity.this, WinLossActivity.class);
        if (gameMode == 0) {
            if (winner == 0)
                intent.putExtra("winner", 1);
            if (winner == 1)
                intent.putExtra("winner", 2);
        } else {
            if (winner == 0)
                intent.putExtra("winner", 3);
            if (winner == 1)
                intent.putExtra("winner", 0);
        }
        startActivity(intent);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(maxTime != -1)
            startTimer(game.getPlayer(0).currentTime, time[0]);
    }

    public void startTimer(long n, final TextView text){

        timer = new CountDownTimer(n, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                game.getPlayer(game.curTeam()).currentTime = millisUntilFinished ;
                text.setText("" + millisUntilFinished /1000);
            }

            @Override
            public void onFinish() {
            }
        };
        timer.start();
    }

    public void stopTimer(){
        timer.cancel();
    }
}