package com.example.rens.competitive_gol.Controller.Acivities;

import com.example.rens.competitive_gol.Controller.BoardController;
import com.example.rens.competitive_gol.Model.Board;
import com.example.rens.competitive_gol.Model.TileSettings;
import com.example.rens.competitive_gol.R;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import java.util.ArrayList;

public class MainActivity extends Activity {

    /*******************VARIABLES*******************/

    static BoardController game; //Het actieve spel atm

    private Board board1 = new Board(10,10,new TileSettings()); //
    private Board board2 = new Board(10,10,new TileSettings()); // voorbeeld van meerdere levels
    private ImageView character;

    /***********************************************/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags( WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        // Het spel:
        game = new BoardController(this, this, board1, 2);

        character = (ImageView)findViewById(R.id.character);
        updateCharacterIcon();

        // De next turn knop:
        findViewById(R.id.buttonNext).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                game.update();
                game.nextPlayer(); // <- 'een keer updaten = volgende speler' hierdoor
                updateCharacterIcon();
            }
        });

        findViewById(R.id.buttonUndo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                game.undoLastMove();
            }
        });

    }

    private void updateCharacterIcon(){
        character.setBackgroundColor(game.curColor());
    }
}