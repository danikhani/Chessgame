package pieces;

import board.Square;
import util.Move;

import java.io.Serializable;
import java.util.ArrayList;

/**
 *
 * Abstract class for chess piece.
 */
public abstract class Piece implements Serializable {

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

    protected Square square;
    protected ArrayList<Square> dangeredSquares = new ArrayList<Square>();
    protected ArrayList<Square> reachableSquares = new ArrayList<Square>();
    protected ArrayList<Square> dangerousForKingSquares = new ArrayList<Square>();


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
    public void clearDangered(){
        dangeredSquares.clear();
    }
    public void setDangered(Square square){
        dangeredSquares.add(square);
    }
    public ArrayList<Square> getDangered(){
        //System.out.println(dangeredSquares);
        return dangeredSquares;
    }
    public boolean hasDangered(Square square){
        boolean itHas = false;
        if(dangeredSquares.contains(square)){
            itHas = true;
        }
        return itHas;
    }
    public void clearReachable(){
        reachableSquares.clear();
    }
    public void setReachable(Square square){
        reachableSquares.add(square);
    }
    public ArrayList<Square> getReachable(){
        //System.out.println(dangeredSquares);
        return reachableSquares;
    }
    public boolean hasReachable(Square square){
        boolean itHas = false;
        if(reachableSquares.contains(square)){
            itHas = true;
        }
        return itHas;
    }
    public void clearDangerousForKingSquares(){
        dangerousForKingSquares.clear();
    }
    public void setDangerousForKingSquares(Square square){
        dangerousForKingSquares.add(square);
    }
    public ArrayList<Square> getDangerousForKingSquares(){
        //System.out.println(dangeredSquares);
        return dangerousForKingSquares;
    }
    public boolean hasDangerousForKingSquares(Square square){
        boolean itHas = false;
        if(dangerousForKingSquares.contains(square)){
            itHas = true;
        }
        return itHas;
    }
}
