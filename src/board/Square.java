package board;

import pieces.Piece;

public class Square {

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

}
