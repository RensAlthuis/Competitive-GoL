package com.example.rens.competitive_gol.Controller;

import com.example.rens.competitive_gol.Controller.BoardController;
import com.example.rens.competitive_gol.Model.Board;
import com.example.rens.competitive_gol.Model.Player;
import com.example.rens.competitive_gol.Model.TileSettings;
import com.example.rens.competitive_gol.R;
import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import java.util.ArrayList;

public class MainActivity extends Activity {

    /*******************VARIABLES*******************/

    static BoardController game; //Het actieve spel atm

    final Player player1 = new Player(1, Color.BLUE);
    final Player player2 = new Player(2, Color.RED);

    Board board1 = new Board(20,20,new TileSettings()); //
    Board board2 = new Board(10,10,new TileSettings()); // voorbeeld van meerdere levels

    /***********************************************/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags( WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        // De next turn knop:
        findViewById(R.id.buttonNext).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                game.update();
                game.nextPlayer(); // <- 'een keer updaten = volgende speler' hierdoor
            }
        });

        // Het spel:
        game = new BoardController(this, this, board1, 2);
    }
}