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
    private int curPlayerIndex = 0;

    private final ScaleGestureDetector mScaleDetector;
    private final GestureDetector mGestureDetector;

    private final ArrayList<ArrayList<Tile>> last = new ArrayList<>();

    private final static int MOVESPERPLAYER = 3;
    private int movesDone = 0;

    /*******************CONSTRUCTORS*******************/

    public BoardController(final Activity activity, final Context context, final Board level, final int numberPlayers){
        board = level;
        boardView = (BoardView)activity.findViewById(R.id.board);
        boardView.init(width(), height());

        allPlayers = new ArrayList<>();

        curPlayerIndex = 0;

        board.setRandomBoard(20,allPlayers); //vult het bord met 20 willekeurige levende blokken per speler

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
                // wat hier onder staat is mogelijk niet echt meer leesbaar.
                // lang verhaal kort: coordinaten muis -> coordinaten gehele canvas -> coordinaten relatief tot de blokken -> naar beneden afgerond
                final int a = (int) Math.floor(boardView.relativeX(boardView.offX(e.getX())));
                final int b = (int) Math.floor(boardView.relativeY(boardView.offY(e.getY())));

                if(0<=a&&a<width() && 0<=b&&b<height()) doMove(a, b);

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

    /***********************************FUNCTIONS*************************************/

    public void addPlayer(Player player){
        allPlayers.add(player);
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

    public int width(){ return board.width; }
    public int height(){ return board.height; }

    public int curTeam() { return allPlayers.get(curPlayerIndex).getTeam(); }
    public int curColor() { return allPlayers.get(curPlayerIndex).getColor(); }

    /***********************************PLAYER CONTROLLES*****************************/
    // shit die je als spel buitenaf doet zonder de context te hoeven weten
    // dit zijn ijzerstekere functies waarvoor alleen nog maar een knop voor hoeft worden gemaakt

    // TODO DE FUNDAMENTELE WIN REGELS
    public void winCheck(){
        if(board.winExtinction(curTeam())) System.out.println("You won! :D");
    }

    // zet de volgende speler
    public void nextPlayer() {
        curPlayerIndex++;
        curPlayerIndex %= allPlayers.size();

        movesDone = 0; //volgende speler, dus geen er is geen moveDone
        last.clear();
    }

    // een 'zet' doen als speler
    // TODO DE FUNDAMENTELE SPELER REGELS
    public void doMove(Coordinate c){ doMove(c.x,c.y);}
    public void doMove(int x, int y){
        if(movesDone<MOVESPERPLAYER){ // TODO: hier zit nu singleMoveModeOn in verwerkt. verrander voor debugging!
            last.add(board.getTiles());

            if(move(x,y)) { // als het succesvol was
                movesDone++; // zet deze simpelweg uit door '|| true' in de if-statement ervoor te doen, en je kan meerdere dingen aanpassen
                setNext();
            }
            else
                last.remove(last.size()-1);
        }
    }

    public void undoMove(){
        if(!last.isEmpty()){
            board.setTiles(last.remove(last.size()-1));
            movesDone--;
            setNext();
        }
    }

    /***********************************MOVE*****************************/
    // wat gebeurt er als de huidige speler iets doet op x,y
    // returnt true als iets is verranderd,

    //hieronder staan de fundamentele spelregels voor wanneer je op iets mag klikken, en wat dat betekent
    //(als dit in bold kan dan zou ik het hebben gedaan)
    //TODO DE FUNDAMENTELE SPELREGELS
    private boolean move(int x, int y){

        if (board.getTileTeam(x, y) == curTeam()) {
            board.setTileDead(x, y);
        } else if (board.isDead(x, y)) {
            board.setTileTeam(x, y, curTeam());
        } else {
            return false;
        }

        return true;
    }

    /*******************NEXT*******************/

    // om de volgende itteratie uit te rekenen. deze functie kan zovaak anngeroepen worden als maar wilt~!
    private void setNext(){
        board.setNext();
        setBoardView();
    }

    /*******************UPDATE*******************/

    // deze functie maakt de beweging van deze beurt naar de volgende beurt
    public void update(){
        board.update();
        setBoardView();
    }

    // maakt de boardView up-to-board met de latest tiles fashion
    private void setBoardView(){
        for(int y=0; y<board.height ; y++)
            for(int x=0; x<board.width ; x++){
                boardView.setTileHealth(x,y,board.getTile(x,y).getHealth());

                if(board.isDead(x,y))       boardView.setTileDead(x,y);
                else                        boardView.setTilePlayer(x,y,allPlayers.get(board.getTileTeam(x,y)).getColor());

                if(board.isDeadNext(x,y))   boardView.setTileDeadNext(x,y);
                else                        boardView.setTilePlayerNext(x,y,allPlayers.get(board.getTileNextTeam(x,y)).getColor());
            }

    }

    public Board getBoard(){
        return new Board(board);
    }
}


