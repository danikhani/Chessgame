package util;

import board.Board;
import board.Square;
import pieces.*;
import ui.BoardPanel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static pieces.PieceSet.getAvailablePieces;

public class MoveValidator implements Serializable {

    private static MoveValidator ourInstance = new MoveValidator();

    public static MoveValidator getInstance() {
        return ourInstance;
    }

    private MoveValidator() {
        currentMoveColor = Piece.Color.WHITE;
        notCurrentMoveColor = Piece.Color.BLACK;
        getKingSquare();
    }

    public static Piece.Color currentMoveColor;
    public static Piece.Color notCurrentMoveColor;
    public static Piece dangerousPiece;
    public static Square kingSquare;
    //public static Square otherKingSquare;
    public static Piece kingPiece;
    private static Piece.Color draggedColor;

    public static void setCurrentMoveColor(Piece.Color color ){
        currentMoveColor = color ;
    }
    public static Piece.Color getDraggedColor(){
        return draggedColor;
    }
    public static void setDraggedColor(Piece.Color color ){
        draggedColor = color ;
    }

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
        if (!isStillChecked(move)){
            return false;
        }
        //checks if a piece get moved the king will be get checked.
        if(move.getPiece().getType() != Piece.Type.KING){
            if(!willBeChecked(move)) {
                return false;
            }
        }

        currentMoveColor = currentMoveColor.equals(Piece.Color.WHITE) ? Piece.Color.BLACK : Piece.Color.WHITE;
        notCurrentMoveColor = move.getPiece().getColor() == Piece.Color.WHITE? Piece.Color.WHITE: Piece.Color.BLACK;
        getKingSquare();
        return true;
    }
    private static void getKingSquare(){
        for (Piece piece : getAvailablePieces(currentMoveColor, Piece.Type.KING)) {
            kingPiece = piece;
            kingSquare = Board.getSquare(piece.getFile(), piece.getRank());
        }
    }
    public static boolean isStillChecked(Move move){
        //TODO: Check if the king is still checked after a move and if yes dont allow that move to happen.
        return true;
    }
    public static boolean willBeChecked(Move move){
        //Check if by moving the piece the king will be in danger.
        Square futureOccupiedSquare = Board.getSquare(move.getDestinationFile(), move.getDestinationRank());
        Square futureFreeSquare = Board.getSquare(move.getOriginFile(),move.getOriginRank());
        Piece futureOccupiedSquarePiece = futureOccupiedSquare.getCurrentPiece();
        //if the destination square is not null:
        if(futureOccupiedSquarePiece!=null) {
            futureOccupiedSquare.setCurrentPiece(null);
            futureOccupiedSquare.setCurrentPiece(move.getPiece());
            futureFreeSquare.setCurrentPiece(null);
            DangerPaths.setDangeredSquares();
            //it gets alle the pieces of other color and checks if they can attack the king
            for (Piece futureSetting : PieceSet.getAvailablePieces(MoveValidator.notCurrentMoveColor)) {
                 if (futureSetting.hasDangered(kingSquare)) {
                    futureOccupiedSquare.setCurrentPiece(futureOccupiedSquarePiece);
                    futureFreeSquare.setCurrentPiece(move.getPiece());
                    DangerPaths.setDangeredSquares();
                    return false;
                }
            }
            //this will bring the piece position to the way before the dragging.
            futureOccupiedSquare.setCurrentPiece(futureOccupiedSquarePiece);
            futureFreeSquare.setCurrentPiece(move.getPiece());
            System.out.println(kingSquare.getCurrentPiece().getColor() +" is not DANGERED by (notnull) "+ notCurrentMoveColor);
            DangerPaths.setDangeredSquares();
            return true;
        }
        else{
            //if the destination square is null.
            futureOccupiedSquare.setCurrentPiece(move.getPiece());
            futureFreeSquare.setCurrentPiece(null);
            DangerPaths.setDangeredSquares();
            for (Piece futureSetting : PieceSet.getAvailablePieces(MoveValidator.notCurrentMoveColor)) {
                System.out.println("checking if " + futureSetting.getColor() + futureSetting.getType() + " on rank "
                        + futureSetting.getRank() +  " is dangering " +kingSquare.getCurrentPiece().getColor() + kingSquare.getCurrentPiece().getType());
                if (futureSetting.hasDangered(kingSquare)) {
                    futureOccupiedSquare.setCurrentPiece(null);
                    futureFreeSquare.setCurrentPiece(move.getPiece());
                    System.out.println(kingSquare.getCurrentPiece().getColor() +" is DANGERED by "+ notCurrentMoveColor);
                    DangerPaths.setDangeredSquares();
                    return false;
                }
            }
            futureOccupiedSquare.setCurrentPiece(null);
            futureFreeSquare.setCurrentPiece(move.getPiece());
            System.out.println(kingSquare.getCurrentPiece().getColor() +" is not DANGERED by "+ notCurrentMoveColor);
            DangerPaths.setDangeredSquares();
            return true;
        }
    }


    public static boolean isCheckMove(Move move) {
                //System.out.println("king square of other color is " + kingSquare);
                for (Piece ourPiece : getAvailablePieces(move.getPiece().getColor())) {
                    if (ourPiece.hasDangered(kingSquare)) {
                        dangerousPiece = ourPiece;
                        return true;
                    }
                    /*//Because Pawn has another attack form. It should be checked extra.
                    if(ourPiece.getType() == Piece.Type.PAWN && ourPiece.hasDangerousForKingSquares(kingSquare)){
                        dangerousPiece = ourPiece;
                        return true;
                    }*/
        }
        return false;
    }
    //if king can move after the ccheck.
    private static boolean kingCanMove(Move move){
        for (Square kingsAccessibleSquare : kingPiece.getDangered()) {
            boolean squareIsFine = true;
            //System.out.println("new square" + kingsAccessibleSquare);
            if (kingsAccessibleSquare.getCurrentPiece() == null ||
                    kingsAccessibleSquare.getCurrentPiece().getColor() != currentMoveColor) {
                //System.out.println("free square" + kingsAccessibleSquare);
                for (Piece otherColorPieces : getAvailablePieces(move.getPiece().getColor())) {
                    //System.out.println("we are available" + otherColorPieces);
                    if (otherColorPieces.hasDangered(kingsAccessibleSquare)) {
                        //System.out.println(otherColorPieces + " can attack this square: " + kingsAccessibleSquare);
                        squareIsFine = false;
                        break;
                    }
                }
                if (squareIsFine) {
                    //System.out.println(kingsAccessibleSquare + " this square had no attackers");
                    System.out.println("king could run away");
                    return false;
                }

            }
        }
        return true;
    }
    private static boolean attackerCanGetCaptured(Move move){
        //if dangerous piece can get captured (WORKS!!!)
        Square dangerousSquare = Board.getSquare(dangerousPiece.getFile(), dangerousPiece.getRank());
        for (Piece piece : getAvailablePieces(currentMoveColor)) {
            if(piece.getType() != Piece.Type.KING){
                if (piece.hasDangered(dangerousSquare)) {
                    System.out.println("attacker can get captured");
                    return false;
                }
                else {
                    if (piece.hasDangered(dangerousSquare)) {
                        if(!kingCanMove(move)){
                        System.out.println("king can attack the attacker");
                            return false;}
                    }
                }

            }
        }
        return true;
    }
    private static boolean attackerCanGetBlocked(Move move){
        //checking if the way can get blocked (WORKS!)
        //here you get to save the path between the attacking piece and king.
        if (dangerousPiece.getType() == Piece.Type.QUEEN) {
            DangerPaths.setKingsRookPath(kingPiece, dangerousPiece);
            DangerPaths.setKingsBishopsPath(kingPiece, dangerousPiece);
        }
        if (dangerousPiece.getType() == Piece.Type.ROOK) {
            DangerPaths.setKingsRookPath(kingPiece, dangerousPiece);
        }
        if (dangerousPiece.getType() == Piece.Type.BISHOP) {
            DangerPaths.setKingsBishopsPath(kingPiece, dangerousPiece);
        }
        //actual checking for the blocking
        for (Square mustGetBlockedSquares : kingPiece.getReachable()) {
            for (Piece ourColorPieces : getAvailablePieces(currentMoveColor)) {
                //it doesnt matter if king can move itself to block the way.
                // so every piece other than king should get checked if they can block
                if(ourColorPieces.getType() != Piece.Type.KING) {
                    if (ourColorPieces.hasDangered(mustGetBlockedSquares)) {
                        System.out.println("path can get blocked");
                        System.out.println(kingPiece.getReachable());
                        return false;
                    }
                }
                //because pawn has extra movement rule, it should be checked if pawn can block the way.
                if(ourColorPieces.getType() == Piece.Type.PAWN) {
                    if (ourColorPieces.hasReachable(mustGetBlockedSquares)) {
                        System.out.println("path can get blocked");
                        System.out.println(kingPiece.getReachable());
                        return false;
                    }
                }
            }
        }
        return true;
    }
    public static boolean isCheckMate(Move move) {
        if(!kingCanMove(move) || !attackerCanGetBlocked(move) || !attackerCanGetCaptured(move)){
            return false;
        }
        return true;
    }

    private static boolean validateClearPath(Move move) {
        if (move.getPiece().getType() == Piece.Type.KNIGHT) {
            return true;
        } else {
            //king cant go into check.
            //check if diagonally
            if (Math.abs(move.getDestinationFile() - move.getOriginFile()) ==
                    Math.abs(move.getDestinationRank() - move.getOriginRank())) {
                if (move.getOriginRank() > move.getDestinationRank()) {
                    int rankDif = move.getOriginRank() - move.getDestinationRank();
                    int j = 1;
                    if (move.getOriginFile() > move.getDestinationFile()) {
                        //
                        while (j < rankDif) {
                            Square currentSquare = board.Board.getSquare((char) (move.getOriginFile() - j), move.getOriginRank() - j);
                            if (!checkClearPath(move, currentSquare)) {
                                return false;
                            }
                            j++;
                        }
                    }
                    if (move.getOriginFile() < move.getDestinationFile()) {
                        //2
                        while (j < rankDif) {
                            Square currentSquare = board.Board.getSquare((char) (move.getOriginFile() + j), move.getOriginRank() - j);
                            if (!checkClearPath(move, currentSquare)) {
                                return false;
                            }
                            j++;
                        }
                    }
                }
                if (move.getOriginRank() < move.getDestinationRank()) {
                    int rankDif = move.getDestinationRank() - move.getOriginRank();
                    int j = 1;
                    if (move.getOriginFile() > move.getDestinationFile()) {
                        //3
                        while (j < rankDif) {
                            Square currentSquare = board.Board.getSquare((char) (move.getOriginFile() - j), move.getOriginRank() + j);
                            if (!checkClearPath(move, currentSquare)) {
                                return false;
                            }
                            j++;
                        }
                    }
                    if (move.getOriginFile() < move.getDestinationFile()) {
                        //4
                        while (j < rankDif) {
                            Square currentSquare = board.Board.getSquare((char) (move.getOriginFile() + j), move.getOriginRank() + j);
                            if (!checkClearPath(move, currentSquare)) {
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
                    int rankDif = move.getOriginRank() - move.getDestinationRank();
                    int j = 1;
                    while (j < rankDif) {
                        Square currentSquare = board.Board.getSquare(move.getOriginFile(), move.getDestinationRank() + j);
                        if (!checkClearPath(move, currentSquare)) {
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
                        if (!checkClearPath(move, currentSquare)) {
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
                        Square currentSquare = board.Board.getSquare((char) (move.getDestinationFile() + j), move.getOriginRank());
                        if (!checkClearPath(move, currentSquare)) {
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
                        Square currentSquare = board.Board.getSquare((char) (move.getOriginFile() + j), move.getOriginRank());
                        if (!checkClearPath(move, currentSquare)) {
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
        if (
                (currentSquare.getCurrentPiece() != null)) {
            return false;
        }
        return true;
    }


}
