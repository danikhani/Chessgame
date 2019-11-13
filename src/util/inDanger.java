package util;

import board.Board;
import pieces.Piece;

import java.util.*;

public class inDanger {
 /*   private static Map<Piece, rank,> available;

    private static void initializeCapturedPieceSet() {
        available = new LinkedHashMap<Piece.Color, Stack<Piece>>();
        Stack<Piece> whiteCapturedPieces = new Stack<Piece>();
        Stack<Piece> blackCapturedPieces = new Stack<Piece>();
        available.put(Piece.Color.WHITE, whiteCapturedPieces);
        available.put(Piece.Color.BLACK, blackCapturedPieces);
    }

    public static void getallavailalbe() {
        for (int i = 1; i <= 8; i++) {
            for (char j = 'a'; j <= 'h'; j++) {
                try {
                    System.out.println(Board.getSquare(j, i).getCurrentPiece());
                } catch (NullPointerException e) {
                }
            }
        }

    }*/
/*
    public static List<Piece> getAvailablePieces(Piece.Color color) {
        List<Piece> availablePiecesSameColor = new ArrayList<Piece>();
        for (Map.Entry<Piece.Type, List<Piece>> availablePiecesEntry : pieceSet.get(color).entrySet()) {
            for (Piece piece : availablePiecesEntry.getValue()) {
                if (!piece.getCapture()) {
                    availablePiecesSameColor.add(piece);
                }
            }
        }
        return availablePiecesSameColor;
    }
       */

    /*private int rank;
    private char file;
    private Piece.Color currentMoveColor;
    private List<inDanger> dangered = new ArrayList<inDanger>();
    private char KingCurrentFile = pieces.PieceSet.getOpponentKingFile(currentMoveColor);
    private int KingCurrentRank = pieces.PieceSet.getOpponentKingRank(currentMoveColor);

    public inDanger(int rank, char file) {
        this.rank = rank;
        this.file = file;
    }
    public int getInDangerRank(){
        return rank;
    }
    public char getInDangerFile(){
        return file;
    }
    public void setInDanger(int inDangerRank, char inDangerFile) {
        inDanger newDangeredPosition = new inDanger(inDangerRank, inDangerFile);
        dangered.add(newDangeredPosition);
    }
    public void removeList(){
        dangered.clear();
    }
    public boolean isKingDangered() {
        for (inDanger i : dangered) {
            if (rank == KingCurrentRank) {
                if (file == KingCurrentFile) {
                    return true;
                }
            }

        }
        return false;
    }*/

}