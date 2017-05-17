package com.example.rens.competitive_gol.View;

import com.example.rens.competitive_gol.Controller.BoardController;
import com.example.rens.competitive_gol.Model.Board;
import com.example.rens.competitive_gol.Model.TileSettings;
import com.example.rens.competitive_gol.R;
import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags( WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        Board board1 = new Board(20,20,new TileSettings()); //
        Board board2 = new Board(10,10,new TileSettings()); // voorbeeld van meerdere levels

        BoardController game = new BoardController(this,this,board1);
        //BoardView board = new BoardView(this);

        //setContentView(board);
    }
}