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
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends Activity {

    /*******************VARIABLES*******************/

    private static BoardController game; //Het actieve spel atm
    private ImageView character;

    private final static int FRACTIONRANDOM = 5; // is de fractie van het bord (als 1/..) dat wordt gevuld door random tiles
    private int gameMode;

    private CountDownTimer timer;
    private TextView[] timerText;
    private int maxTime;
    boolean alreadyWon;

    /***********************************************/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags( WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        final TextView aiText = (TextView)findViewById(R.id.AIText);
        aiText.setVisibility(View.INVISIBLE);
        alreadyWon = false;

        /**** NEXT-TURN BUTTON ****/
        final Button btnNext = (Button)findViewById(R.id.buttonNext);
        btnNext.setBackgroundColor(Color.DKGRAY);
        btnNext.setTextColor(Color.WHITE);
        btnNext.setTextSize(20);
        btnNext.setTypeface(Typeface.createFromAsset(getAssets(), "LCD_Solid.ttf"));
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnNext.setEnabled(false);

                nextTurn();

                if(gameMode != 0 && !alreadyWon){
                    aiText.setVisibility(View.VISIBLE);
                    aiText.invalidate();
                    Handler handler = new Handler();
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            ((AIPlayer)game.getPlayer(1)).makeNextMove();
                            nextTurn();
                            aiText.setVisibility(View.INVISIBLE);
                        }
                    });
                }
                btnNext.setEnabled(true);
            }
        });

        /**** UNDO BUTTON ****/
        final Button btnUndo = (Button)findViewById(R.id.buttonUndo);
        btnUndo.setBackgroundColor(Color.DKGRAY);
        btnUndo.setTextColor(Color.WHITE);
        btnUndo.setTextSize(20);
        btnUndo.setTypeface(Typeface.createFromAsset(getAssets(), "LCD_Solid.ttf"));
        btnUndo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                game.undoMove();
            }
        });

        /**** THE GAME ****/
        gameMode = getIntent().getIntExtra("gameMode", 0);
        makeGame(gameMode);

        character = (ImageView)findViewById(R.id.character);
        updateCharacterIcon();
    }

    private void updateCharacterIcon(){
        character.setBackgroundColor(game.curColor());
    }

    /**
     * This function checks whether one of the players has won. If not, the turn is passed to the
     * next player.
     */
    private void nextTurn(){
        // updaten -> gewonnen? -> volgende speler -> zet timer

        game.update();

        int win = game.winCheck();
        if(win != -1) toWinLoss(win);

        game.nextPlayer();
        updateCharacterIcon();

        if(maxTime != -1) {
            timer.cancel();
            startTimer(game.getPlayer(game.curTeam()).currentTime, timerText[game.curTeam()]);
        }
    }

    /********************MAKING THE GAME********************/
    /**
     * This function creates the initial board and sets the players
     * @param gameMode This integer signifies whether the game is between two players, or between
     *                 a player and an AI opponent and the difficulty of that AI.
     */
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

        /********************BOARD********************/

        game.setRandomBoard(size*size/FRACTIONRANDOM);

        /********************TIMERS********************/
        final String maxTimeStr = getIntent().getStringExtra("Time");

        if(maxTimeStr.equals("2 min"))       maxTime = 2*60;
        else if(maxTimeStr.equals("5 min"))  maxTime = 5*60;
        else if(maxTimeStr.equals("10 min")) maxTime = 10*60;
        else if(maxTimeStr.equals("20 min")) maxTime = 20*60;
        else{
            maxTime = -1;
            return;
        }

        timerText = new TextView[]{(TextView)findViewById(R.id.timerPlayer1),(TextView)findViewById(R.id.timerPlayer2)};

        timerText[0].setTypeface(Typeface.createFromAsset(getAssets(), "LCD_Solid.ttf"));
        timerText[1].setTypeface(Typeface.createFromAsset(getAssets(), "LCD_Solid.ttf"));

        timerText[0].setTextSize(40);
        timerText[1].setTextSize(40);

        timerText[0].setTextColor(game.getPlayer(0).getColor());
        timerText[1].setTextColor(game.getPlayer(1).getColor());

        game.getPlayer(0).currentTime = maxTime*1000;
        game.getPlayer(1).currentTime = maxTime*1000;

        setTimerText(timerText[0],(int)game.getPlayer(0).currentTime/1000);
        setTimerText(timerText[1],(int)game.getPlayer(1).currentTime/1000);
    }

    /********************************WIN/LOSE********************************/
    /**
     * This function is to be called when someone wins to transition to the win/loss screen.
     * @param winner the team number of the winner.
     */
    private void toWinLoss(int winner) {
        if(alreadyWon){
            return;
        }
        alreadyWon = true;
        finish();
        Intent intent = new Intent(MainActivity.this, WinLossActivity.class);

        if (gameMode == 0) {
            if (winner == 0) intent.putExtra("winner", 1);
            if (winner == 1) intent.putExtra("winner", 2);
        } else {
            if (winner == 0) intent.putExtra("winner", 3);
            if (winner == 1) intent.putExtra("winner", 0);
        }

        startActivity(intent);
    }

    /********************************TIMER********************************/

    private void setTimerText(TextView text, int timeSec){
        final int seconds = timeSec%60;
        final int minutes = (timeSec - seconds)/60;

        if(seconds<10) text.setText(minutes + ":0" + seconds);
        else           text.setText(minutes + ":"  + seconds);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(maxTime != -1) startTimer(game.getPlayer(0).currentTime, timerText[0]);
    }

    @Override
    protected void onDestroy() {
        if(maxTime != -1) timer.cancel();
        super.onDestroy();
    }

    /**
     * This function starts a timer if one is set.
     * @param n The number of seconds the timer should represent.
     * @param text The textview in which the timer is displayed.
     */
    public void startTimer(long n, final TextView text){
        timer = new CountDownTimer(n, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                game.getPlayer(game.curTeam()).currentTime = millisUntilFinished ;
                setTimerText(text,(int)(millisUntilFinished /1000));
            }

            @Override
            public void onFinish() {}
        }.start();
    }
}