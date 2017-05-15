package com.example.rens.competitive_gol.View;

import com.example.rens.competitive_gol.Controller.BoardController;
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
        BoardController board = new BoardController(this,this);
        //BoardView board = new BoardView(this);

        //setContentView(board);
    }
}