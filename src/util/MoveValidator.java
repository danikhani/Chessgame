package util;

import board.Board;
import board.Square;
import pieces.*;
import ui.BoardPanel;

import java.io.Serializable;
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
        notCurrentMoveColor = Piece.Color.BLACK;
        getKingSquare();
    }

    public static Piece.Color currentMoveColor;
    public static Piece.Color notCurrentMoveColor;
    public static Piece dangerousPiece;
    public static Square kingSquare;
    public static Square otherKingSquare;
    public static Piece kingPiece;
    public static Piece otherKingPiece;
    private static Piece.Color draggedColor;

    public static void setCurrentMoveColor(Piece.Color color) {
        currentMoveColor = color;
    }

    public static void setNotCurrentMoveColor(Piece.Color color) {
        notCurrentMoveColor = color;
    }

    public static Piece.Color getDraggedColor() {
        return draggedColor;
    }

    public static void setDraggedColor(Piece.Color color) {
        draggedColor = color;
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
        if (!isStillChecked(move)) {
            return false;
        }
        //checks if a piece get moved the king will be get checked.
        if (move.getPiece().getType() != Piece.Type.KING) {
            if (!willBeChecked(move)) {
                return false;
            }
        }
        currentMoveColor = currentMoveColor.equals(Piece.Color.WHITE) ? Piece.Color.BLACK : Piece.Color.WHITE;
        notCurrentMoveColor = move.getPiece().getColor() == Piece.Color.WHITE ? Piece.Color.WHITE : Piece.Color.BLACK;
        getKingSquare();
        return true;
    }

    public static void getKingSquare() {
        for (Piece piece : getAvailablePieces(currentMoveColor, Piece.Type.KING)) {
            kingPiece = piece;
            kingSquare = Board.getSquare(piece.getFile(), piece.getRank());
        }
        for (Piece piece : getAvailablePieces(notCurrentMoveColor, Piece.Type.KING)) {
            otherKingPiece = piece;
            otherKingSquare = Board.getSquare(piece.getFile(), piece.getRank());
        }
    }

    //This checks if the move destination is in the list of reachable squares.
    private static boolean validateClearPath(Move move) {
        Square futureOccupiedSquare = Board.getSquare(move.getDestinationFile(), move.getDestinationRank());
        if (move.getPiece().hasDangered(futureOccupiedSquare)) {
            return true;
        }
        // since pawn has a different type of movement we need to check for it.
        if (move.getPiece().getType() == Piece.Type.PAWN) {
            if (move.getPiece().hasReachable(futureOccupiedSquare)) {
                return true;
            }
        }
        return false;
    }

    //This avoids the move which wont help to bring the king out of check.
    public static boolean isStillChecked(Move move) {
        if (isCheckMove(move)) {
            if (move.getPiece().getType() == Piece.Type.KING) {
                Square futureKingsSquare = Board.getSquare(move.getDestinationFile(), move.getDestinationRank());
                for (Piece futureSetting : PieceSet.getAvailablePieces(MoveValidator.notCurrentMoveColor)) {
                    if (futureSetting.hasDangered(futureKingsSquare)) {
                        return false;
                    }
                    if (futureSetting.getType() == Piece.Type.PAWN && futureSetting.hasDangerousForKingSquares(futureKingsSquare)) {
                        return false;
                    }
                }
                return true;
            } else {
                return willBeChecked(move);
            }
        }
        return true;
    }

    //Check if by moving the piece the king will be in danger and avoids it
    public static boolean willBeChecked(Move move) {
        Square futureOccupiedSquare = Board.getSquare(move.getDestinationFile(), move.getDestinationRank());
        Square futureFreeSquare = Board.getSquare(move.getOriginFile(), move.getOriginRank());
        Piece futureOccupiedSquarePiece = futureOccupiedSquare.getCurrentPiece();
        //if the destination square is not null:
        if (futureOccupiedSquarePiece != null) {
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
            DangerPaths.setDangeredSquares();
            return true;
        } else {
            //if the destination square is null.
            futureOccupiedSquare.setCurrentPiece(move.getPiece());
            futureFreeSquare.setCurrentPiece(null);
            DangerPaths.setDangeredSquares();
            for (Piece futureSetting : PieceSet.getAvailablePieces(MoveValidator.notCurrentMoveColor)) {
                if (futureSetting.hasDangered(kingSquare)) {
                    futureOccupiedSquare.setCurrentPiece(null);
                    futureFreeSquare.setCurrentPiece(move.getPiece());
                    DangerPaths.setDangeredSquares();
                    return false;
                }
            }
            futureOccupiedSquare.setCurrentPiece(null);
            futureFreeSquare.setCurrentPiece(move.getPiece());
            DangerPaths.setDangeredSquares();
            return true;
        }
    }

    public static boolean isCheckMove(Move move) {
        //This checks if the kingsquare is in the list of dangered of the pieces of the othercolor.
        for (Piece ourPiece : getAvailablePieces(move.getPiece().getColor())) {
            if (ourPiece.hasDangered(kingSquare)) {
                dangerousPiece = ourPiece;
                return true;
            }
        }
        return false;
    }

    //checks for checkmate
    public static boolean isCheckMate(Move move) {
        if (!kingCanMove(move) || !attackerCanGetBlocked(move) || !attackerCanGetCaptured(move)) {
            return false;
        }
        return true;
    }

    //if king can move after the check.
    private static boolean kingCanMove(Move move) {
        for (Square kingsAccessibleSquare : kingPiece.getDangered()) {
            boolean squareIsFine = true;
            if (kingsAccessibleSquare.getCurrentPiece() == null ||
                    kingsAccessibleSquare.getCurrentPiece().getColor() != currentMoveColor) {
                for (Piece otherColorPieces : getAvailablePieces(move.getPiece().getColor())) {
                    if (otherColorPieces.hasDangered(kingsAccessibleSquare)) {
                        squareIsFine = false;
                        break;
                    }
                }
                if (squareIsFine) {
                    System.out.println("king could run away");
                    return false;
                }

            }
        }
        return true;
    }

    private static boolean attackerCanGetCaptured(Move move) {
        //if dangerous piece can get captured
        Square dangerousSquare = Board.getSquare(dangerousPiece.getFile(), dangerousPiece.getRank());
        for (Piece piece : getAvailablePieces(currentMoveColor)) {
            if (piece.getType() != Piece.Type.KING) {
                if (piece.hasDangered(dangerousSquare)) {
                    System.out.println("attacker can get captured");
                    return false;
                } else {
                    if (piece.hasDangered(dangerousSquare)) {
                        if (!kingCanMove(move)) {
                            System.out.println("king can attack the attacker");
                            return false;
                        }
                    }
                }

            }
        }
        return true;
    }

    private static boolean attackerCanGetBlocked(Move move) {
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
                if (ourColorPieces.getType() != Piece.Type.KING) {
                    if (ourColorPieces.hasDangered(mustGetBlockedSquares)) {
                        System.out.println("path can get blocked");
                        return false;
                    }
                }
                //because pawn has extra movement rule, it should be checked if pawn can block the way.
                if (ourColorPieces.getType() == Piece.Type.PAWN) {
                    if (ourColorPieces.hasReachable(mustGetBlockedSquares)) {
                        System.out.println("path can get blocked");
                        return false;
                    }
                }
            }
        }
        return true;
    }


}
