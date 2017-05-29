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

    private final ArrayList<Player> allPlayers;
    private int curPlayerIndex;

    private final ScaleGestureDetector mScaleDetector;
    private final GestureDetector mGestureDetector;

    private final ArrayList<ArrayList<Tile>> last = new ArrayList<>();

    private boolean moveDone = false;

    /*******************CONSTRUCTORS*******************/

    public BoardController(final Activity activity, final Context context, final Board level, final int numberPlayers){
        board = level;
        boardView = (BoardView)activity.findViewById(R.id.board);
        boardView.init(getBoardWidth(), getBoardHeight());

        allPlayers = setPlayers(numberPlayers);
        curPlayerIndex = 0;

        //board.createRandomBoard(20,allPlayers); //vult het bord met 20 willekeurige levende blokken per speler

        setBoardView();

        /*****USER CONTROLS*****/

        boardView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                mScaleDetector.onTouchEvent(event);
                mGestureDetector.onTouchEvent(event);
                return true;
            }
        });

        // SCALING: dit detecteerd bewegingen waarbij je twee aanraakpunten naar elkaar toe trekt/van elkaar weg haalt
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

        // CLICKING/SWIPING: dit detecteerd kleine simpele bewegingen die je met een aanraakpunt maakt
        mGestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {

            @Override //Voor: loslaten na een keer klikken
            public boolean onSingleTapUp(MotionEvent e) {
               //Deze functie wordt gebruikt wanneer je klikt op een tile :)
                final float offset = boardView.getScaledTileWidth();
                final int a = (int)Math.floor(boardView.offX(e.getX())/offset);
                final int b = (int)Math.floor(boardView.offY(e.getY())/offset);

                doMove(a,b);

                return false;
            }

            @Override //Voor: schuiven
            public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                //Deze functie wordt gebruikt wanneer je via een swipe beweging je verplaatst over het bord
                boardView.updateOffset(distanceX, distanceY);
                return false;
            }
        });

    }

    // maakt een gesoteerde lijst van alle spelers afhankelijk van al opgestelde kleuren
    private ArrayList<Player> setPlayers(int  numberPlayers){
        ArrayList<Player> sortedPlayers = new ArrayList<>();
        final int[] colours = {Color.BLUE,Color.RED,Color.GREEN,Color.MAGENTA,Color.YELLOW,Color.CYAN};

        for(int i=0; i<numberPlayers; i++)
            sortedPlayers.add(new Player(i,colours[i%colours.length]));

        return sortedPlayers;
    }

    /***********************************FUNCTIONS*************************************/

    public int getBoardWidth(){ return board.width; }
    public int getBoardHeight(){ return board.height; }

    public int curTeam() { return allPlayers.get(curPlayerIndex).getTeam(); }
    public int curColor() { return allPlayers.get(curPlayerIndex).getColor(); }

    /***********************************PLAYER CONTROLLES*****************************/
    // shit die je als spel buitenaf doet zonder de context te hoeven weten
    // dit zijn ijzerstekere functies waarvoor alleen nog maar een knop voor hoeft worden gemaakt

    // zet de volgende speler
    public void nextPlayer(){
        curPlayerIndex++;
        curPlayerIndex %= allPlayers.size();

        moveDone = false; //volgende speler, dus geen er is geen moveDone
    }

    // een 'zet' doen als speler
    public void doMove(int x, int y){
        if(!moveDone || true){ // TODO: hier zit nu singleMoveModeOn in verwerkt. verrander voor debugging!
            if(move(x,y)) // als het succesvol was
                moveDone = true; // zet deze simpelweg uit door '|| true' in de if-statement ervoor te doen, en je kan meerdere dingen aanpassen
        }
    }

    public void undoMove(){
        if(!last.isEmpty()){
            board.setTiles(last.remove(last.size()-1));
            moveDone = false;
            next();
        }
    }

    /***********************************MOVE*****************************/

    // wat gebeurt er als de huidige speler iets doet op x,y
    // returnt true als iets is verrandert,
    private boolean move(int x, int y){
        //hieronder staan de fundamentele spelregels voor wanneer je op iets mag klikken, en wat dat betekent
        // (als dit in bold kan dan zou ik het hebben gedaan)
        //TODO zijn de voorwaardens die hier staan goede voorwaarden voor wat kan/niet kan?

        if (board.getTileTeam(x, y) == curTeam()) {
            last.add(board.getTiles());
            setTileDead(x, y);

        } else if (board.isDead(x, y)) {
            last.add(board.getTiles());
            setTilePlayer(x, y);

        } else return false;

        next();
        return true;
    }

    // zet (x,y) op de huidige speler
    private void setTilePlayer(int x, int y){
        board.setTileTeam(x, y, curTeam());
        boardView.setTilePlayer(x, y, curColor());
    }

    // zet (x,y) op dood
    private void setTileDead(int x, int y){
        board.setTileDead(x, y);
        boardView.setTileDead(x, y);
    }

    /*******************NEXT*******************/

    // om de volgende itteratie uit te rekenen. deze functie kan zovaak anngeroepen worden als maar wilt~!
    public void next(){
        board.setNext();
        setBoardView();
    }

    /*******************UPDATE*******************/

    // deze functie maakt de beweging van deze beurt naar de volgende beurt
    // stap 1) update board, stap 2) update boardView aan de hand van board
    public void update(){
        board.update();
        setBoardView();
    }

    // maakt de boardView up-to-board met de latest tiles fashion
    private void setBoardView(){
        for(int y=0; y<board.height ; y++)
            for(int x=0; x<board.width ; x++){
                if(board.isDead(x,y))       boardView.setTileDead(x,y);
                else                        boardView.setTilePlayer(x,y,allPlayers.get(board.getTileTeam(x,y)).getColor());

                if(board.isDeadNext(x,y))   boardView.setTileDeadNext(x,y);
                else                        boardView.setTilePlayerNext(x,y,allPlayers.get(board.getTileNextTeam(x,y)).getColor());

                boardView.setTileHealth(x,y,board.getTile(x,y).getHealth());
            }
    }
}


