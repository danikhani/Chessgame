package board;

import pieces.Piece;
import ui.BoardPanel;

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
    public void changeColor(){
        //BoardPanel.changeSingleSquarePanel

    }

}
