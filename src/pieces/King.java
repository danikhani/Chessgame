package pieces;

import board.Board;
import board.Square;
import util.Move;
import util.MoveValidator;

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
    //This stops the king to move into check position
    public boolean specialMove(Move move) {
        if (Math.abs(move.getDestinationRank() - move.getOriginRank()) == 1) {
            return willCheck(move);
        }
        if (Math.abs(move.getDestinationFile() - move.getOriginFile()) == 1) {
            return willCheck(move);
        }

        return false;
    }
    //looks if the king is moveing into check
    private boolean willCheck(Move move){
        Square futureKingsSquare = Board.getSquare(move.getDestinationFile(), move.getDestinationRank());
        for(Piece futureSetting : PieceSet.getAvailablePieces(MoveValidator.notCurrentMoveColor)){
            if(futureSetting.hasDangered(futureKingsSquare)){
                return false;
            }
        }
        return true;
    }

}
