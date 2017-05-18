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

public class MainActivity extends Activity {

    final Player player1 = new Player(1, 0xffff0000);
    final Player player2 = new Player(2, 0xff00ff00);
    Board board1 = new Board(20,20,new TileSettings()); //
    Board board2 = new Board(10,10,new TileSettings()); // voorbeeld van meerdere levels
    BoardController game;
    Player currentPlayer ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags( WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        game = new BoardController(this,this,board1, player1);
        currentPlayer= player1;
        findViewById(R.id.buttonNextTurn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nextTurn();
            }
        });

    }

    private void nextTurn(){
        game.update();
        currentPlayer = (currentPlayer.getTeamNumber() == player1.getTeamNumber())? player2 : player1;
        game.redrawPlayer(player1); // this is stupid, need to find a better solution
        game.redrawPlayer(player2); // this is stupid, need to find a better solution
        game.setCurPlayer(currentPlayer);
    }
}