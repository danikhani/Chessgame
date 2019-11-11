package pieces;

import board.Square;
import util.Move;

public class King extends Piece {

    public King(Color color) {
        super(color);
        this.type = Type.KING;
    }

    @Override
    public boolean validateMove(Move move) {
        // executeMove or capture
        if ((move.getCapturedPiece() == null)
                || (move.getCapturedPiece() != null
                && !move.getPiece().getColor().equals(move.getCapturedPiece().getColor()))) {
            // diagonal movement
            if (move.getDestinationFile() != move.getOriginFile()
                    && move.getDestinationRank() != move.getOriginRank()) {
                if (Math.abs( move.getDestinationFile() -  move.getOriginFile()) ==
                        Math.abs( move.getDestinationRank() - move.getOriginRank())) {
                    if (specialMove(move)) {
                        return true;
                    }
                }
            }
            if (move.getDestinationFile() == move.getOriginFile()
                    && move.getDestinationRank() != move.getOriginRank()) {
                if (specialMove(move)) {
                    return true;
                }
            }
            // along rank
            if (move.getDestinationFile() != move.getOriginFile()
                    && move.getDestinationRank() == move.getOriginRank()) {
                if (specialMove(move)) {
                    return true;
                }
            }
        }
        // all other cases
        return false;
    }
    @Override
    public boolean specialMove(Move move) {
        if (Math.abs(move.getDestinationRank() - move.getOriginRank()) == 1) {
            refreshCoordinates(move);
            return true;
        }
        if (Math.abs(move.getDestinationFile() - move.getOriginFile()) == 1) {
            refreshCoordinates(move);
            return true;

        }
        return false;
    }
    //to get the kings coordinate.
    private void refreshCoordinates(Move move){
        if(move.getPiece().getColor() == Color.BLACK){
            PieceSet.initializeBlackKingCoordinates(move.getDestinationRank(),move.getDestinationFile());
        }
        else{
            PieceSet.initializeWhiteKingCoordinates(move.getDestinationRank(),move.getDestinationFile());
        }
    }
   /* public boolean possibleCheck(Move move){
        int rankDif = move.getDestinationRank() - move.getOriginRank();
        int j = 1;
        while (j < rankDif) {
            Square currentSquare = board.Board.getSquare(move.getOriginFile(), move.getOriginRank() + j);
            if (currentSquare.getCurrentPiece().getType() == Piece.Type.BISHOP &&
                    move.getPiece().getColor().equals(currentSquare.getCurrentPiece().getColor()) ) {
                return false;
            }
            j++;
        }




        return true;
    }

    */

}
