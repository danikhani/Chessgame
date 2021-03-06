package pieces;

import board.Square;
import util.Move;


public class Pawn extends Piece {

    public Pawn(Color color) {
        super(color);
        this.type = Type.PAWN;
    }

    @Override
    public boolean validateMove(Move move) {
        // executeMove
        if ((move.getCapturedPiece() == null)) {
            if (specialMove(move)) {
                return true;
            }
        }
        //capture
        if (move.getDestinationFile() != move.getOriginFile()
                && move.getDestinationRank() != move.getOriginRank()) {
            if (Math.abs( move.getDestinationFile() -  move.getOriginFile()) ==
                    Math.abs( move.getDestinationRank() - move.getOriginRank())) {
                if(move.getCapturedPiece() != null
                        && !move.getPiece().getColor().equals(move.getCapturedPiece().getColor())){
                    return true;
                }
            }
        }
        // all other cases
        return false;
    }
    @Override
    public boolean specialMove(Move move){
        if(move.getOriginFile()==move.getDestinationFile()) {
            if (move.getOriginRank() == 2 &&
                    (move.getPiece().getColor() == Piece.Color.WHITE) &&
                    (move.getDestinationRank() - move.getOriginRank() == 2)) {
                return true;
            } else if (move.getOriginRank() == 7 &&
                    (move.getPiece().getColor() == Piece.Color.BLACK) &&
                    (move.getDestinationRank() - move.getOriginRank() == -2)) {
                return true;
            } else if ((move.getPiece().getColor() == Piece.Color.WHITE) &&
                    (move.getDestinationRank() - move.getOriginRank() == 1)) {
                return true;
            } else if ((move.getPiece().getColor() == Piece.Color.BLACK) &&
                    (move.getDestinationRank() - move.getOriginRank() == -1)) {
                return true;
            }
        }
        return false;
    }

}
