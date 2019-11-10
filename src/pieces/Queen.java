package pieces;

import util.Move;

public class Queen extends Piece {

    public Queen(Color color) {
        super(color);
        this.type = Type.QUEEN;
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
                    return true;
                }
            }
            if (move.getDestinationFile() == move.getOriginFile()
                    && move.getDestinationRank() != move.getOriginRank()) {
                return true;
            }
            // along rank
            if (move.getDestinationFile() != move.getOriginFile()
                    && move.getDestinationRank() == move.getOriginRank()) {
                return true;
            }

        }

        // all other cases
        return false;
    }
    @Override
    public boolean specialMove(Move move) {
        return true;
    }

}
