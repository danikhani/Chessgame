package pieces;
import board.Board;
import board.Square;
import ui.BoardPanel;

import java.util.ArrayList;
import java.util.List;

import static pieces.PieceSet.getAvailablePieces;

public class DangerPaths {
    private boolean inBarriers;

    public static void setDangeredSquares() {
        for (Piece piece : getAvailablePieces()) {
            piece.clearDangered();
            piece.clearReachable();
            switch (piece.getType()) {
                case KING:
                    setKingPath(piece);
                    break;
                case ROOK:
                    setRookPath(piece);
                    break;
                case BISHOP:
                    setBishopPath(piece);
                    break;
                case KNIGHT:
                    setKnightPath(piece);
                    break;
                case QUEEN:
                    setBishopPath(piece);
                    setRookPath(piece);
                    break;
                case PAWN:
                    setPawnPath(piece);
                    break;
            }
        }
    }

    private static boolean isInBarriers(int rank, char file) {
        if (rank >= 1 && rank <= 8) {
            if (file >= 'a' && file <= 'h') {
                return true;
            }
        }
        return false;
    }

    private static void setRookPath(Piece piece) {
        //direction north:
        int currentRank = piece.getRank() + 1;
        char currentFile = piece.getFile();
        while (isInBarriers(currentRank, currentFile)) {
            Square currentSquare = Board.getSquare(currentFile, currentRank);
            piece.setDangered(currentSquare);
            if (currentSquare.getCurrentPiece() != null) {
                break;
            }
            currentRank++;
        }
        //direction south:
        currentRank = piece.getRank() - 1;
        currentFile = piece.getFile();
        while (isInBarriers(currentRank, currentFile)) {
            Square currentSquare = Board.getSquare(currentFile, currentRank);
            piece.setDangered(currentSquare);
            if (currentSquare.getCurrentPiece() != null) {
                break;
            }
            currentRank--;
        }
        //direction east:
        currentRank = piece.getRank();
        currentFile = (char) (piece.getFile() + 1);
        while (isInBarriers(currentRank, currentFile)) {
            Square currentSquare = Board.getSquare(currentFile, currentRank);
            piece.setDangered(currentSquare);
            if (currentSquare.getCurrentPiece() != null) {
                break;
            }
            currentFile++;
        }
        //direction west:
        currentRank = piece.getRank();
        currentFile = (char) (piece.getFile() - 1);
        while (isInBarriers(currentRank, currentFile)) {
            Square currentSquare = Board.getSquare(currentFile, currentRank);
            piece.setDangered(currentSquare);
            if (currentSquare.getCurrentPiece() != null) {
                break;
            }
            currentFile--;
        }
    }

    private static void setBishopPath(Piece piece) {
        //direction north east:
        int currentRank = piece.getRank() + 1;
        char currentFile = (char) (piece.getFile() + 1);
        while (isInBarriers(currentRank, currentFile)) {
            Square currentSquare = Board.getSquare(currentFile, currentRank);
            piece.setDangered(currentSquare);
            if (currentSquare.getCurrentPiece() != null) {
                break;
            }
            currentRank++;
            currentFile++;
        }
        //direction north west:
        currentRank = piece.getRank() + 1;
        currentFile = (char) (piece.getFile() - 1);
        while (isInBarriers(currentRank, currentFile)) {
            Square currentSquare = Board.getSquare(currentFile, currentRank);
            piece.setDangered(currentSquare);
            if (currentSquare.getCurrentPiece() != null) {
                break;
            }
            currentRank++;
            currentFile--;
        }
        //direction south west:
        currentRank = piece.getRank() - 1;
        currentFile = (char) (piece.getFile() - 1);
        while (isInBarriers(currentRank, currentFile)) {
            Square currentSquare = Board.getSquare(currentFile, currentRank);
            piece.setDangered(currentSquare);
            if (currentSquare.getCurrentPiece() != null) {
                break;
            }
            currentRank--;
            currentFile--;
        }
        //direction south east:
        currentRank = piece.getRank() - 1;
        currentFile = (char) (piece.getFile() + 1);
        while (isInBarriers(currentRank, currentFile)) {
            Square currentSquare = Board.getSquare(currentFile, currentRank);
            piece.setDangered(currentSquare);
            if (currentSquare.getCurrentPiece() != null) {
                break;
            }
            currentRank--;
            currentFile++;
        }
    }

    private static void setKnightPath(Piece piece) {
        //direction north big:
        int currentRank = piece.getRank() + 2;
        char currentFile = (char) (piece.getFile() - 1);
        if (isInBarriers(currentRank, currentFile)) {
            Square currentSquare = Board.getSquare(currentFile, currentRank);
            piece.setDangered(currentSquare);
        }
        currentFile = (char) (piece.getFile() + 1);
        if (isInBarriers(currentRank, currentFile)) {
            Square currentSquare = Board.getSquare(currentFile, currentRank);
            piece.setDangered(currentSquare);
        }
        // direction south big:
        currentRank = piece.getRank() - 2;
        currentFile = (char) (piece.getFile() - 1);
        if (isInBarriers(currentRank, currentFile)) {
            Square currentSquare = Board.getSquare(currentFile, currentRank);
            piece.setDangered(currentSquare);
        }
        currentFile = (char) (piece.getFile() + 1);
        if (isInBarriers(currentRank, currentFile)) {
            Square currentSquare = Board.getSquare(currentFile, currentRank);
            piece.setDangered(currentSquare);
        }
        //direction north small:
        currentRank = piece.getRank() + 1;
        currentFile = (char) (piece.getFile() - 2);
        if (isInBarriers(currentRank, currentFile)) {
            Square currentSquare = Board.getSquare(currentFile, currentRank);
            piece.setDangered(currentSquare);
        }
        currentFile = (char) (piece.getFile() + 2);
        if (isInBarriers(currentRank, currentFile)) {
            Square currentSquare = Board.getSquare(currentFile, currentRank);
            piece.setDangered(currentSquare);
        }
        // direction south small:
        currentRank = piece.getRank() - 1;
        currentFile = (char) (piece.getFile() - 2);
        if (isInBarriers(currentRank, currentFile)) {
            Square currentSquare = Board.getSquare(currentFile, currentRank);
            piece.setDangered(currentSquare);
        }
        currentFile = (char) (piece.getFile() + 2);
        if (isInBarriers(currentRank, currentFile)) {
            Square currentSquare = Board.getSquare(currentFile, currentRank);
            piece.setDangered(currentSquare);
        }
    }

    private static void setPawnPath(Piece piece) {
        int currentRank = piece.getRank();
        char currentFile = (char) (piece.getFile());
        Square currentSquare;
        //first pawn move white
        if (piece.getColor() == Piece.Color.WHITE) {
            currentRank++;
            currentSquare = Board.getSquare(currentFile, currentRank);
            if (currentSquare.getCurrentPiece() == null) {
                piece.setReachable(currentSquare);
                if (piece.getRank() == 2) {
                    currentRank++;
                    currentSquare = Board.getSquare(currentFile, currentRank);
                    if (currentSquare.getCurrentPiece() == null) {
                        piece.setReachable(currentSquare);
                    }
                }
            }
        }
        //first pawn move black
        if (piece.getColor() == Piece.Color.BLACK) {
            currentRank--;
            currentSquare = Board.getSquare(currentFile, currentRank);
            if (currentSquare.getCurrentPiece() == null) {
                piece.setReachable(currentSquare);
                if (piece.getRank() == 7) {
                    currentRank--;
                    currentSquare = Board.getSquare(currentFile, currentRank);
                    if (currentSquare.getCurrentPiece() == null) {
                        piece.setReachable(currentSquare);
                    }
                }
            }
        }
        //White pawn attack
        if (piece.getColor() == Piece.Color.WHITE) {
            currentRank = piece.getRank() + 1;
            currentFile = (char) (piece.getFile() + 1);
            if (isInBarriers(currentRank, currentFile)) {
                currentSquare = Board.getSquare(currentFile, currentRank);
                if(currentSquare.getCurrentPiece() != null) {
                    piece.setDangered(currentSquare);
                }
            }

            currentFile = (char) (piece.getFile() - 1);
            if (isInBarriers(currentRank, currentFile)) {
                currentSquare = Board.getSquare(currentFile, currentRank);
                if(currentSquare.getCurrentPiece() != null) {
                    piece.setDangered(currentSquare);
                }
            }
        }

        //black Pawn attack move
        if (piece.getColor() == Piece.Color.BLACK) {
            currentRank = piece.getRank() - 1;
            currentFile = (char) (piece.getFile() + 1);
            if (isInBarriers(currentRank, currentFile)) {
                currentSquare = Board.getSquare(currentFile, currentRank);
                if(currentSquare.getCurrentPiece() != null) {
                    piece.setDangered(currentSquare);
                }
            }
            currentFile = (char) (piece.getFile() - 1);
            if (isInBarriers(currentRank, currentFile)) {
                currentSquare = Board.getSquare(currentFile, currentRank);
                if(currentSquare.getCurrentPiece() != null) {
                    piece.setDangered(currentSquare);
                }
            }
        }
    }

    private static void setKingPath(Piece piece) {
        int currentRank;
        char currentFile;
        Square currentSquare;
        //north
        currentRank = piece.getRank() + 1;
        currentFile = (char) (piece.getFile());
        if (isInBarriers(currentRank, currentFile)) {
            currentSquare = Board.getSquare(currentFile, currentRank);
            piece.setDangered(currentSquare);
        }
        //south
        currentRank = piece.getRank() - 1;
        currentFile = (char) (piece.getFile());
        if (isInBarriers(currentRank, currentFile)) {
            currentSquare = Board.getSquare(currentFile, currentRank);
            piece.setDangered(currentSquare);
        }
        //west
        currentRank = piece.getRank();
        currentFile = (char) (piece.getFile() - 1);
        if (isInBarriers(currentRank, currentFile)) {
            currentSquare = Board.getSquare(currentFile, currentRank);
            piece.setDangered(currentSquare);
        }
        //east
        currentRank = piece.getRank();
        currentFile = (char) (piece.getFile() + 1);
        if (isInBarriers(currentRank, currentFile)) {
            currentSquare = Board.getSquare(currentFile, currentRank);
            piece.setDangered(currentSquare);
        }
        //north east
        currentRank = piece.getRank() + 1;
        currentFile = (char) (piece.getFile() + 1);
        if (isInBarriers(currentRank, currentFile)) {
            currentSquare = Board.getSquare(currentFile, currentRank);
            piece.setDangered(currentSquare);
        }
        //north west
        currentRank = piece.getRank() + 1;
        currentFile = (char) (piece.getFile() - 1);
        if (isInBarriers(currentRank, currentFile)) {
            currentSquare = Board.getSquare(currentFile, currentRank);
            piece.setDangered(currentSquare);
        }
        //south east
        currentRank = piece.getRank() - 1;
        currentFile = (char) (piece.getFile() + 1);
        if (isInBarriers(currentRank, currentFile)) {
            currentSquare = Board.getSquare(currentFile, currentRank);
            piece.setDangered(currentSquare);
        }
        //south west
        currentRank = piece.getRank() - 1;
        currentFile = (char) (piece.getFile() - 1);
        if (isInBarriers(currentRank, currentFile)) {
            currentSquare = Board.getSquare(currentFile, currentRank);
            piece.setDangered(currentSquare);
        }
    }

    public static void setKingsRookPath(Piece king, Piece attacker) {
        //horizontal
        System.out.println("checking horizontaly");
        if (king.getRank() - attacker.getRank() == 0) {
            System.out.println(" horizontaly possible");
            //from attacker to king
            if (king.getFile() > attacker.getFile()) {
                for (char i = (char) (attacker.getFile() + 1); i < king.getFile(); i++) {
                    Square currentSquare = Board.getSquare(i, king.getRank());
                    king.setReachable(currentSquare);
                }
            }
            //from king to attacker
            if (king.getFile() < attacker.getFile()) {
                for (char i = (char) (king.getFile() + 1); i < attacker.getFile(); i++) {
                    Square currentSquare = Board.getSquare(i, king.getRank());
                    king.setReachable(currentSquare);
                }
            }
        }
        //vertical
        if (king.getFile() - attacker.getFile() == 0) {
            System.out.println(" verticaly possible");
            //from attacker to king
            if (king.getRank() > attacker.getRank()) {
                for (int i = attacker.getRank() + 1; i < king.getRank(); i++) {
                    Square currentSquare = Board.getSquare(king.getFile(), i);
                    king.setReachable(currentSquare);
                }
            }
            //from king to attacker
            if (king.getRank() < attacker.getRank()) {
                for (int i = king.getRank() + 1; i < attacker.getRank(); i++) {
                    Square currentSquare = Board.getSquare(king.getFile(), i);
                    king.setReachable(currentSquare);
                }
            }
        }
    }

    public static void setKingsBishopsPath(Piece king, Piece attacker) {
        //check if diagonally
        if (Math.abs(attacker.getFile() - king.getFile()) ==
                Math.abs(attacker.getRank() - king.getRank())) {
            System.out.println("diagonally is possible");
            if (king.getRank() > attacker.getRank()) {
                int rankDif = king.getRank() - attacker.getRank();
                int j = 1;
                if (king.getFile() > attacker.getFile()) {
                    //
                    while (j < rankDif) {
                        Square currentSquare = Board.getSquare((char) (king.getFile() - j), king.getRank() - j);
                        king.setReachable(currentSquare);
                        j++;
                    }
                }
                if (king.getFile() < attacker.getFile()) {
                    //2
                    while (j < rankDif) {
                        Square currentSquare = Board.getSquare((char) (king.getFile() + j), king.getRank() - j);
                        king.setReachable(currentSquare);
                        j++;
                    }
                }
            }
            if (king.getRank() < attacker.getRank()) {
                int rankDif = attacker.getRank() - king.getRank();
                int j = 1;
                if (king.getFile() > attacker.getFile()) {
                    //3
                    while (j < rankDif) {
                        Square currentSquare = Board.getSquare((char) (king.getFile() - j), king.getRank() + j);
                        king.setReachable(currentSquare);
                        j++;
                    }
                }
                if (king.getFile() < attacker.getFile()) {
                    //4
                    while (j < rankDif) {
                        Square currentSquare = Board.getSquare((char) (king.getFile() + j), king.getRank() + j);
                        king.setReachable(currentSquare);
                        j++;
                    }
                }
            }

        }
    }
}
