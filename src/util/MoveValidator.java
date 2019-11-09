package util;

import board.Board;
import pieces.Pawn;
import pieces.Piece;
import pieces.PieceSet;
import ui.BoardPanel;

import java.util.List;

public class MoveValidator {

    private static MoveValidator ourInstance = new MoveValidator();

    public static MoveValidator getInstance() {
        return ourInstance;
    }

    private MoveValidator() {
        currentMoveColor = Piece.Color.WHITE;
    }

    private static Piece.Color currentMoveColor;

    public static boolean validateMove(Move move) {
        return validateMove(move, false);
    }

    public static boolean validateMove(Move move, boolean ignoreColorCheck) {
        // check for out of bounds
        if (move.getDestinationFile() < 'a' || move.getDestinationFile() > 'h'
                || move.getDestinationRank() < 1 || move.getDestinationRank() > 8) {
            return false;
        }

        // check for valid origin
        if (move.getPiece() == null) {
            return false;
        }

        // check for valid color
        if (!move.getPiece().getColor().equals(currentMoveColor) && !ignoreColorCheck) {
            return false;
        }

        // check for valid destination
        if (move.getCapturedPiece() != null) {
            if (move.getPiece().getColor().equals(move.getCapturedPiece().getColor())) {
                return false;
            }
        }

        // check for piece rule
        if (!move.getPiece().validateMove(move)) {
            return false;
        }

        // check for clear path
        if (!validateClearPath(move)) {
            return false;
        }

        currentMoveColor = currentMoveColor.equals(Piece.Color.WHITE) ? Piece.Color.BLACK : Piece.Color.WHITE;
        return true;
    }

    public static boolean isCheckMove(Move move) {
        // TODO-check
        return false;
    }

    public static boolean isCheckMate(Move move) {
        // TODO-check
        return false;
    }

    private static boolean validateClearPath(Move move) {
        int path = move.getDestinationRank()-move.getOriginRank();
        int positions = move.getOriginRank();
        for(int i = 0; i< path; i++){
            positions =+ i;
            move.getCapturedPiece() != null




        }
        move.getDestinationFile() < 'a';


        switch (move.getPiece().getType()) {
            case PAWN:
                //Basic movement
                if(move.getOriginRank()==2 &&
                        (move.getPiece().getColor() == Piece.Color.WHITE) &&
                        (move.getDestinationRank()-move.getOriginRank() == 2)){
                    return true;
                }
                else if(move.getOriginRank()==7 &&
                        (move.getPiece().getColor() == Piece.Color.BLACK) &&
                        (move.getDestinationRank()-move.getOriginRank() == -2)) {
                    return true;
                }
                else if((move.getPiece().getColor() == Piece.Color.WHITE) &&
                        (move.getDestinationRank()-move.getOriginRank() == 1)) {
                        return true;
                }
                else if((move.getPiece().getColor() == Piece.Color.BLACK) &&
                        (move.getDestinationRank()-move.getOriginRank() == -1)) {
                    return true;
                }
                return false;


            case KING:
                if(Math.abs(move.getDestinationRank()-move.getOriginRank()) == 1) {
                    return true;
                }
                return false;
            case BISHOP:
                return true;
            case QUEEN:
                return true;
        }



        // TODO-movement
        return false;
    }

}
