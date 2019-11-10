package util;

import board.Board;
import board.Square;
import pieces.Knight;
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
        if(move.getPiece().getType() == Piece.Type.KNIGHT){return true;}
            else{
                switch (move.getPiece().getColor()) {
                    case WHITE:
                        //check if diagonally
                        if (Math.abs( move.getDestinationFile() -  move.getOriginFile()) ==
                                Math.abs( move.getDestinationRank() - move.getOriginRank())) {
                            if(move.getOriginRank() > move.getDestinationRank()){
                                if(move.getOriginFile() > move.getDestinationFile()){

                                    //
                                }
                                if(move.getOriginFile() < move.getDestinationFile()){
                                    //
                                }
                            }
                            if(move.getOriginRank() < move.getDestinationRank()){
                                if(move.getOriginFile() > move.getDestinationFile()){
                                    //
                                }
                                if(move.getOriginFile() < move.getDestinationFile()){
                                    //
                                }
                            }

                        }
                        // only for movements along rank
                        else {
                            if (move.getOriginRank() > move.getDestinationRank()) {
                                for (int i = move.getOriginRank() -1; i > move.getDestinationRank(); i--) {
                                    Square currentSquare = board.Board.getSquare(move.getOriginFile(), i);
                                    if (currentSquare.getCurrentPiece() != null) {
                                        return false;
                                    }
                                }
                            }
                            if (move.getOriginRank() < move.getDestinationRank()) {
                                for (int i = move.getOriginRank() +1 ; i < move.getDestinationRank(); i++) {
                                    Square currentSquare = board.Board.getSquare(move.getOriginFile(), i);
                                    if (currentSquare.getCurrentPiece() != null) {
                                        return false;
                                    }
                                }
                            }
                            //only for movements along file
                            if (move.getOriginFile() > move.getDestinationFile()) {
                                for (int i = move.getOriginFile() -1 ; i > (int)move.getDestinationFile(); i--) {
                                    Square currentSquare = board.Board.getSquare((char)i, move.getOriginRank());
                                    if (currentSquare.getCurrentPiece() != null) {
                                        return false;
                                    }
                                }
                            }
                            if (move.getOriginFile() < move.getDestinationFile()) {
                                for (int i = move.getOriginFile() +1 ; i < (int)move.getDestinationFile(); i++) {
                                    Square currentSquare = board.Board.getSquare((char)i, move.getOriginRank());
                                    if (currentSquare.getCurrentPiece() != null) {
                                        return false;
                                    }
                                }
                            }

                        }
                    case BLACK:
                }
            }


    /*




        switch (move.getPiece().getType()) {
            case ROOK:
                if((!validateClearFile(move) || !validateClearRank(move)){
                    return false;
                }
            case PAWN:
                if(!validateClearRank(move)){
                    return false;
                }
            case BISHOP:
                if(!validateClearDiagnoally(move)){
                    return false;
                }
            case QUEEN:
                if(!validateClearDiagnoally(move) ||) {
                    return false;
                }
        }
        return true;
    }
        if( Math.abs(move.getDestinationRank()-move.getOriginRank()) > 1) {
            if (move.getDestinationFile() != move.getOriginFile()
                    && move.getDestinationRank() == move.getOriginRank()) {
                if (move.getOriginRank() < move.getDestinationRank()){

                    for(int i= move.getOriginRank() ; i > move.getDestinationRank(); i--){
                        if (move.getCurrentPiece() != null){return false;}

                    }
                }


            }
            else{
                return true;
            }
        }
        else{
            return true;

    }



        if(move.getDestinationRank()-move.getOriginRank() > 1) {
            if (move.getDestinationFile() != move.getOriginFile()
                    && move.getDestinationRank() == move.getOriginRank()) {
                return true;
            }
            // along file
        }
        if(move.getDestinationFile() - move.getOriginFile() > 1){
            if (move.getDestinationFile() == move.getOriginFile()
                    && move.getDestinationRank() != move.getOriginRank()) {
                return true;
            }
            // along rank

        }




        int[][] history = new int[7][7];


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



            case KING:

                return false;
            case BISHOP:
                return true;
            case QUEEN:
                return true;
        }
        return false;
        */



        // TODO-movement

    return true;
    }


}
