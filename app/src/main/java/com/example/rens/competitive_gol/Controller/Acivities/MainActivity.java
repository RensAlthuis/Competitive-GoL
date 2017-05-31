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
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;

import java.util.ArrayList;

public class MainActivity extends Activity {

    /*******************VARIABLES*******************/

    static BoardController game; //Het actieve spel atm

    private Board board0 = new Board(6,7,new TileSettings()); // TODO: om een of andere reden crasht hij als het board te klein is. raar
    private Board board1 = new Board(10,10,new TileSettings()); //
    private Board board2 = new Board(15,15,new TileSettings()); // voorbeeld van meerdere levels
    private ImageView character;
    private boolean isAIGame;

    /***********************************************/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags( WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        isAIGame = !getIntent().getStringExtra("gameMode").equals("Player VS. Player");
        // Het spel:
        game = new BoardController(this, this, board1, 2);
        int col1 = getIntent().getIntExtra("Player1", 0);
        game.addPlayer(new Player(0, col1));

        int col2 = getIntent().getIntExtra("Player2", 0);
        if(!isAIGame) {
            game.addPlayer(new Player(1, col2));
        }else{
            game.addPlayer(new AIPlayer(1, col2, game, new EasyStrategy()));
        }
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
                if(isAIGame){
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
        game.winCheck();
        game.nextPlayer(); // updaten -> gewonnen? -> volgende speler
        updateCharacterIcon();
    }
}