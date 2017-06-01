package com.example.rens.competitive_gol.Controller.Acivities;

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
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;

public class MainActivity extends Activity {

    /*******************VARIABLES*******************/

    static BoardController game; //Het actieve spel atm

    private ImageView character;
    private int gameMode;
    private Player player1;

    /***********************************************/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags( WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        gameMode = getIntent().getIntExtra("gameMode", 0);

        // Het spel:
        makeGame();
        addPlayers();

        game.randomBoard(20);

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
    }
    private void addPlayers(){
        int col1 = getIntent().getIntExtra("Player1", 0);
        player1 = new Player(0, col1);
        game.addPlayer(player1);

        int col2 = getIntent().getIntExtra("Player2", 0);
        if(gameMode == 0) {
            game.addPlayer(new Player(1, col2));
        }else{
            game.addPlayer(new AIPlayer(1, col2, game, (gameMode == 1)? new EasyStrategy()
                                                                        : new HardStrategy()));
        }
    }

    private void makeGame(){
        int steps = getIntent().getIntExtra("steps", 1);
        int size = getIntent().getIntExtra("boardSize", 10);
        Board b = new Board(size,size,new TileSettings());
        game = new BoardController(this, this, b, 2, steps);
    }

    private void toWinLoss(int winner){
        finish();
        Intent intent = new Intent(MainActivity.this, WinLossActivity.class);
        if(gameMode == 0){
            if(winner == 0)
                intent.putExtra("winner", 1);
            if(winner == 1)
                intent.putExtra("winner", 2);
        }else{
            if (winner == 0)
                intent.putExtra("winner", 3);
            if (winner == 1)
                intent.putExtra("winner", 0);
        }
        startActivity(intent);
    }
}