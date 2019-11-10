package pieces;

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
            return true;
        }
        if (Math.abs(move.getDestinationFile() - move.getOriginFile()) == 1) {
            return true;
        }
        return false;
    }

}
