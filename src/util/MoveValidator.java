package util;

import board.Board;
import board.Square;
import pieces.*;
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
        /*
        char KingCurrentFile = pieces.PieceSet.getOpponentKingFile(currentMoveColor);
        int KingCurrentRank = pieces.PieceSet.getOpponentKingRank(currentMoveColor);

        switch (move.getPiece().getType()) {

            //move.getCapturedPiece().
            case PAWN:
                int j = 1;
                if ((char)(move.getDestinationFile()+1) == KingCurrentFile &&  )
                Square destinationSquare = board.Board.getSquare((move.getDestinationFile()), move.getDestinationRank());
                Square attackPoint1 = board.Board.getSquare((char)(move.getDestinationFile()+1), move.getDestinationRank()+1);
                Square attackPoint2 = board.Board.getSquare((char)(move.getDestinationFile()-1), move.getDestinationRank()+1);
                if (attackPoint2.getCurrentPiece().getType() == Piece.Type.KING
                     //   && !attackPoint2.getCurrentPiece().getColor().equals(move.getPiece().getColor())
                ){
                    return true;
                }
                if (attackPoint1.getCurrentPiece().getType() == Piece.Type.KING
                       // && !attackPoint1.getCurrentPiece().getColor().equals(move.getPiece().getColor())
                ){
                    return true;
                }
                return false;
        }

         */


        return false;
    }



    public static boolean isCheckMate(Move move) {
        // TODO-check
        return false;
    }

    private static boolean validateClearPath(Move move) {
        if(move.getPiece().getType() == Piece.Type.KNIGHT){return true;}
            else{
                //check if diagonally
                        if (Math.abs( move.getDestinationFile() -  move.getOriginFile()) ==
                                Math.abs( move.getDestinationRank() - move.getOriginRank())) {
                            if(move.getOriginRank() > move.getDestinationRank()){
                                int rankDif = move.getOriginRank() - move.getDestinationRank();
                                int j = 1;
                                if(move.getOriginFile() > move.getDestinationFile()){
                                    //
                                    while(j < rankDif ){
                                        Square currentSquare = board.Board.getSquare((char)(move.getOriginFile()-j), move.getOriginRank()-j);
                                        if (!checkClearPath(move,currentSquare)) {
                                            return false;
                                        }
                                        j++;
                                    }
                                }
                                if(move.getOriginFile() < move.getDestinationFile()){
                                    //2
                                    while(j < rankDif){
                                        Square currentSquare = board.Board.getSquare((char)(move.getOriginFile()+j), move.getOriginRank()-j);
                                        if (!checkClearPath(move,currentSquare)) {
                                            return false;
                                        }
                                        j++;
                                    }
                                }
                            }
                            if(move.getOriginRank() < move.getDestinationRank()){
                                int rankDif = move.getDestinationRank() - move.getOriginRank();
                                int j = 1;
                                if(move.getOriginFile() > move.getDestinationFile()){
                                    //3
                                    while(j < rankDif ){
                                        Square currentSquare = board.Board.getSquare((char)(move.getOriginFile()-j), move.getOriginRank()+j);
                                        if (!checkClearPath(move,currentSquare)) {
                                            return false;
                                        }
                                        j++;
                                    }
                                }
                                if(move.getOriginFile() < move.getDestinationFile()){
                                    //4
                                    while(j < rankDif ) {
                                        Square currentSquare = board.Board.getSquare((char) (move.getOriginFile() + j), move.getOriginRank() + j);
                                        if (!checkClearPath(move,currentSquare)) {
                                            return false;
                                        }
                                        j++;
                                    }
                                }
                            }

                        }
                        // only for movements along rank
                        else {
                            if (move.getOriginRank() > move.getDestinationRank()) {
                                //when ranks get smaller
                                int rankDif = move.getOriginRank() - move.getDestinationRank() ;
                                int j = 1;
                                while (j < rankDif) {
                                    Square currentSquare = board.Board.getSquare(move.getOriginFile(), move.getDestinationRank() + j);
                                    if (!checkClearPath(move,currentSquare)) {
                                        return false;
                                    }
                                    j++;
                                }
                            }
                            if (move.getOriginRank() < move.getDestinationRank()) {
                                //when ranks get bigger
                                int rankDif = move.getDestinationRank() - move.getOriginRank();
                                int j = 1;
                                while (j < rankDif) {
                                    Square currentSquare = board.Board.getSquare(move.getOriginFile(), move.getOriginRank() + j);
                                    if (!checkClearPath(move,currentSquare)) {
                                        return false;
                                    }
                                    j++;
                                }
                            }
                            //only for movements along file
                            if (move.getOriginFile() > move.getDestinationFile()) {
                                //when file gets smaller
                                int fileDif = move.getOriginFile() - move.getDestinationFile();
                                int j = 1;
                                while (j < fileDif) {
                                    Square currentSquare = board.Board.getSquare((char)(move.getDestinationFile() + j), move.getOriginRank());
                                    if (!checkClearPath(move,currentSquare)) {
                                        return false;
                                    }
                                    j++;
                                }
                            }
                            if (move.getOriginFile() < move.getDestinationFile()) {
                                //when file gets bigger
                                int fileDif = move.getDestinationFile() - move.getOriginFile();
                                int j = 1;
                                while (j < fileDif) {
                                    Square currentSquare = board.Board.getSquare((char)(move.getOriginFile() + j), move.getOriginRank());
                                    if (!checkClearPath(move,currentSquare)) {
                                        return false;
                                    }
                                    j++;
                                }
                            }
                        }
            }
        return true;
    }
    private static boolean checkClearPath(Move move, Square currentSquare) {
        if(
        (currentSquare.getCurrentPiece() != null)){
            return false;
        }
        return true;
    }


}
