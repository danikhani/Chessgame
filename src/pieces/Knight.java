package pieces;

import board.Square;
import util.Move;

public class Knight extends Piece {

    public Knight(Color color) {
        super(color);
        this.type = Type.KNIGHT;
    }

    @Override
    public boolean validateMove(Move move) {
        // executeMove or capture
        if ((move.getCapturedPiece() == null)
                || (move.getCapturedPiece() != null
                && !move.getPiece().getColor().equals(move.getCapturedPiece().getColor()))) {
            // along file
            if (specialMove(move)) {
                return true;
            }
        }

        // all other cases
        return false;
    }
    @Override
    public boolean specialMove(Move move) {
        if( move.getOriginRank() == move.getDestinationRank() +2) {
            if((move.getOriginFile() == move.getDestinationFile() +1) ||
            (move.getOriginFile() +1 == move.getDestinationFile() )){
                return true;
            }
        }
        if( move.getOriginRank() +2 == move.getDestinationRank()) {
            if ((move.getOriginFile() == move.getDestinationFile() + 1) ||
                    (move.getOriginFile() + 1 == move.getDestinationFile())) {
                return true;
            }
        }
        if( move.getOriginFile()  == move.getDestinationFile() + 2) {
            if ((move.getOriginRank() == move.getDestinationRank() + 1) ||
                    (move.getOriginRank() + 1 == move.getDestinationRank())) {
                return true;
            }
        }
        if( move.getOriginFile() +2  == move.getDestinationFile() ) {
            if ((move.getOriginRank() == move.getDestinationRank() + 1) ||
                    (move.getOriginRank() + 1 == move.getDestinationRank())) {
                return true;
            }
        }

            return false;
        }
}
