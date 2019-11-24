package board;

import pieces.Piece;
import ui.BoardPanel;
import util.Move;

import java.io.Serializable;

public class Square implements Serializable {
    private int rank;
    private char file;

    private Piece currentPiece;

    public Square() {
        this.currentPiece = null;
    }

    public void setCurrentPiece(Piece piece) {
        this.currentPiece = piece;
    }
    public Piece getCurrentPiece() {
        return this.currentPiece;
    }
    public int getRank(){
        return this.rank;
    }
    public char getFile(){
        return this.file;
    }
    public void setRank(int rank){
        this.rank = rank;
    }
    public void setFile(char file){
        this.file = file;
    }
    /*public void switchIndicator(Move move){
        for (Square dangered : move.getPiece().getDangered()){
            if(dangered.getFile(). )
        }
    }*/

    }
