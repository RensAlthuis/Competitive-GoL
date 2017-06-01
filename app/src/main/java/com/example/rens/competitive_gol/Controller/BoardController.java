package com.example.rens.competitive_gol.Controller;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;

import java.util.ArrayList;

import com.example.rens.competitive_gol.Model.Board;
import com.example.rens.competitive_gol.Model.Coordinate;
import com.example.rens.competitive_gol.Model.Player;
import com.example.rens.competitive_gol.Model.Tile;
import com.example.rens.competitive_gol.R;
import com.example.rens.competitive_gol.View.BoardView;

public class BoardController {

    /*******************VARIABLES*******************/

    private final Board board;
    private final BoardView boardView;

    private final ScaleGestureDetector mScaleDetector;
    private final GestureDetector mGestureDetector;

    private final ArrayList<Player> players;
    private int curPlayerIndex = 0;

    private final ArrayList<ArrayList<Tile>> last = new ArrayList<>();
    private int movesPerPlayer;
    private int movesDone = 0;

    /*******************CONSTRUCTORS*******************/

    public BoardController(final Activity activity, final Context context, final Board level, int movesPerPlayer){
        board = level;

        boardView = (BoardView)activity.findViewById(R.id.board);
        boardView.init(getWidth(), getHeight());

        players = new ArrayList<>();
        this.movesPerPlayer = movesPerPlayer;


        /*****USER CONTROLS*****/

        boardView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                mScaleDetector.onTouchEvent(event);
                mGestureDetector.onTouchEvent(event);
                return true;
            }
        });

        // SCALING
        // dit detecteerd bewegingen waarbij je twee aanraakpunten naar elkaar toe trekt/van elkaar weg haalt
        mScaleDetector = new ScaleGestureDetector(context, new ScaleGestureDetector.OnScaleGestureListener() {
            private float oldScaleFactor;

            @Override // begin scaling
            public boolean onScaleBegin(ScaleGestureDetector detector) {
                oldScaleFactor = detector.getScaleFactor();
                return true;
            }

            @Override // tijdens scaling
            public boolean onScale(ScaleGestureDetector detector) {
                float dScaling = detector.getScaleFactor() - oldScaleFactor;
                boardView.updateScaling(dScaling, detector.getFocusX(), detector.getFocusY());
                oldScaleFactor = detector.getScaleFactor();
                return false;
            }

            @Override // einde scaling
            public void onScaleEnd(ScaleGestureDetector detector) {}

        });

        // CLICKING/SWIPING
        // dit detecteerd kleine simpele bewegingen die je met een aanraakpunt maakt
        mGestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {

            @Override // LOSLATEN NA EEN KEER KLIKKEN
            public boolean onSingleTapUp(MotionEvent e) {
                //Deze functie wordt gebruikt wanneer je klikt op een tile :)
                // coordinaten muis -> coordinaten gehele canvas -> coordinaten relatief tot de blokken -> naar integers
                final int a = (int) Math.floor(boardView.relativeX(boardView.offX(e.getX())));
                final int b = (int) Math.floor(boardView.relativeY(boardView.offY(e.getY())));

                if(0<=a&&a<getWidth() && 0<=b&&b<getHeight()) doMove(a, b);

                return false;
            }

            @Override // SCHUIVEN
            public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                //Deze functie wordt gebruikt wanneer je via een swipe beweging je verplaatst over het bord
                boardView.updateOffset(distanceX, distanceY);
                return false;
            }
        });
    }

    /***********************************FUNCTIONS*************************************/

    public void addPlayer(Player player) { players.add(player); }
    public Player getPlayer(int index)   { return players.get(index); }

    public int getWidth()  { return board.width; }
    public int getHeight() { return board.height; }

    public int curTeam()  { return players.get(curPlayerIndex).getTeam(); }
    public int curColor() { return players.get(curPlayerIndex).getColor(); }

    /***********************************SET BOARD*************************************/

    public void setEmptyBoard(){
        board.setEmptyBoard();
        setBoardView();
    }

    /***********************************PLAYER CONTROLLES*****************************/
    // shit die je als spel buitenaf doet zonder de context te hoeven weten
    // dit zijn ijzerstekere functies waarvoor alleen nog maar een knop voor hoeft worden gemaakt


    public void setRandomBoard(int n){
        board.setRandomBoard(n,players); //vult het bord met n willekeurige levende blokken per speler
        setBoardView();
    }

    public Board getBoard(){ return new Board(board); }

    /***********************************GAME CONTROLS*****************************/

    // een 'zet' doen als speler
    // TODO DE FUNDAMENTELE SPELER REGELS
    public void doMove(Coordinate c){ doMove(c.x,c.y);}
    public void doMove(int x, int y){
        if(movesDone < movesPerPlayer){ // zet deze simpelweg uit door '|| true' in de if-statement ervoor te doen, en je kan meerdere dingen aanpassen

            last.add(board.getTiles()); // TODO: last slaat nu alle tiles op. misschien andere oplossing?

            if(move(x,y)) {
                movesDone++;
                setNext();
            }
            else last.remove(last.size()-1);
        }
    }

    public void undoMove(){
        if(!last.isEmpty()){
            board.setTiles(last.remove(last.size()-1));
            movesDone--;
            setNext();
        }
    }

    // zet de volgende speler
    public void nextPlayer() {
        curPlayerIndex = (curPlayerIndex+1)%players.size();
        movesDone = 0; //volgende speler, dus geen er is geen moveDone
        last.clear();
    }

    // TODO DE FUNDAMENTELE WIN REGELS
    public int winCheck() {
        return board.winExtinction();
    }

    /***********************************MOVE*****************************/
    // wat gebeurt er als de huidige speler iets doet op x,y
    // returned true als iets is verranderd,

    //TODO DE FUNDAMENTELE SPELREGELS
    private boolean move(int x, int y){

        if (board.getTileTeam(x, y) == curTeam()) {
            board.setTileDead(x, y);
        } else if (board.isDead(x, y)) {
            board.setTileTeam(x, y, curTeam());
        } else return false;

        return true;
    }

    /***********************************NEXT*****************************/

    // om de volgende itteratie uit te rekenen. deze functie kan zovaak anngeroepen worden als maar wilt~!
    private void setNext(){
        board.setNext();
        setBoardView();
    }

    /***********************************UPDATE*****************************/

    // deze functie maakt de beweging van deze beurt naar de volgende beurt
    public void update(){
        board.update();
        setBoardView();
    }

    /***********************************************************************/

    // maakt de boardView up-to-board met de latest tiles fashion
    private void setBoardView(){
        for(int y=0; y<board.height ; y++)
            for(int x=0; x<board.width ; x++){

                boardView.setTileHealth(x,y,board.getTile(x,y).getHealth());

                if(board.isDead(x,y))       boardView.setTileDead(x,y);
                else                        boardView.setTilePlayer(x,y,players.get(board.getTileTeam(x,y)).getColor());

                if(board.isDeadNext(x,y))   boardView.setTileDeadNext(x,y);
                else                        boardView.setTilePlayerNext(x,y,players.get(board.getTileNextTeam(x,y)).getColor());
            }
    }
}


