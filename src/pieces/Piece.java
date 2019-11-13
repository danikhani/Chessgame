package pieces;

import board.Square;
import util.Move;

/**
 * Abstract class for chess piece.
 */
public abstract class Piece {

    public enum Color {
        WHITE, BLACK
    }

    public enum Type {
        KING, ROOK, BISHOP, QUEEN, KNIGHT, PAWN
    }

    protected Color color;
    protected Type type;
    protected boolean capture;
    protected boolean inDanger;
    protected char file;
    protected int rank;

    public Piece(Color color) {
        this.color = color;
        this.capture = false;
    }

    public abstract boolean validateMove(Move move);
    public abstract boolean specialMove(Move move);

    public String getImageFileName() {
        String fileName = "/pieces/";
        switch (color) {
            case WHITE:
                fileName += "white_";
                break;
            case BLACK:
                fileName += "black_";
                break;
        }
        switch (type) {
            case KING:
                fileName += "king";
                break;
            case ROOK:
                fileName += "rook";
                break;
            case BISHOP:
                fileName += "bishop";
                break;
            case QUEEN:
                fileName += "queen";
                break;
            case KNIGHT:
                fileName += "knight";
                break;
            case PAWN:
                fileName += "pawn";
                break;
        }
        fileName += ".png";
        return fileName;
    }

    public Color getColor() {
        return color;
    }

    public Type getType() {
        return type;
    }

    public void setCapture(boolean isCaptured) {
        this.capture = isCaptured;
    }

    public boolean getCapture() {
        return this.capture;
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

}
