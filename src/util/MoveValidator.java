package util;

import board.Board;
import board.Square;
import pieces.*;
import ui.BoardPanel;

import java.util.ArrayList;
import java.util.List;

import static pieces.PieceSet.getAvailablePieces;

public class MoveValidator {

    private static MoveValidator ourInstance = new MoveValidator();

    public static MoveValidator getInstance() {
        return ourInstance;
    }

    private MoveValidator() {
        currentMoveColor = Piece.Color.WHITE;
    }

    private static Piece.Color currentMoveColor;
    private static Piece.Color otherColor;
    private static Piece dangerousPiece;


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
        otherColor = currentMoveColor.equals(Piece.Color.WHITE) ? Piece.Color.BLACK :Piece.Color.WHITE;

        return true;

    }
//this method only works for white rooks now. but it gives checks the pieces on top of the rook
// and if they are free it will be saved in rooks array as dangered. TODO: make a new class and add all methods for checking in all destinations
    public static void setDangeredSquares(){
        for(Piece piece: getAvailablePieces()) {
            if (piece.getType() == Piece.Type.ROOK && piece.getColor() == Piece.Color.WHITE) {
                System.out.println(piece);
                /*System.out.println(piece.getRank());
                System.out.println(piece.getFile());*/
                int a = piece.getRank()+1;
                char b = piece.getFile();
               while (a <= 8) {
                    Square currentSquare = Board.getSquare(b, a);
                    piece.setDangered(currentSquare);
                   System.out.println("Rook has followign squares under control:"+currentSquare);
                    if (currentSquare.getCurrentPiece() != null) {
                        break;
                    }
                    a++;
                }

            }
        }

    }


    public static boolean isCheckMove(Move move) {
        System.out.println("current move color is " + currentMoveColor);
        System.out.println("other move color is " + otherColor);
        Square kingSquare;
        for (Piece piece : getAvailablePieces(otherColor)) {
            if (piece.getType() == Piece.Type.KING) {
                kingSquare = Board.getSquare(piece.getFile(), piece.getRank());
                System.out.println("king square of other color is " + kingSquare);
                for (Piece ourPiece : getAvailablePieces(currentMoveColor)) {
                    if( ourPiece.hasDangered(kingSquare)){
                        dangerousPiece = ourPiece;
                        return true;
                    }
                }
            }
        }
        return false;
    }



    public static boolean isCheckMate(Move move) {
        //works fine but too late.
        System.out.println("dangerrouuus"+ dangerousPiece);
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
