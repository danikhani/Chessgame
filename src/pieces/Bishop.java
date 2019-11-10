package pieces;

import util.Move;

public class Bishop extends Piece {

    public Bishop(Color color) {
        super(color);
        this.type = Type.BISHOP;
    }

    @Override
    public boolean validateMove(Move move) {
        // executeMove or capture
        if ((move.getCapturedPiece() == null)
                || (move.getCapturedPiece() != null
                && !move.getPiece().getColor().equals(move.getCapturedPiece().getColor()))) {
            // along file
            if (move.getDestinationFile() != move.getOriginFile()
                    && move.getDestinationRank() != move.getOriginRank()) {
                if (Math.abs( move.getDestinationFile() -  move.getOriginFile()) ==
                        Math.abs( move.getDestinationRank() - move.getOriginRank())) {
                    return true;
                }
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
