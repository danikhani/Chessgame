package pieces;

import board.Square;
import ui.GameFrame;
import util.GameModel;

import java.io.Serializable;
import java.util.*;

public class PieceSet {

    private static Map<Piece.Color, Map<Piece.Type, List<Piece>>> pieceSet = null;
    private static Map<Piece.Color, Stack<Piece>> capturedPieceSet;
    private static Map<Piece.Color, Stack<Piece>> availablePieceSet;

    /*private static char whiteKingFile;
    private static int whiteKingRank;
    private static char blackKingFile;
    private static int blackKingRank;*/

    private static PieceSet pieceSetInstance = new PieceSet();

    public static PieceSet getInstance() {
        return pieceSetInstance;
    }
    /*private static List<Piece> availableRequestedColorPieces = new ArrayList<Piece>();
     *//*    private static List<Piece> availablePieces = new ArrayList<Piece>();
    private static List<Piece> emptyList = new ArrayList<Piece>();*/

    private PieceSet() {
        initialize();
    }

    private static void initialize() {
        initializePieceSet();
        initializeCapturedPieceSet();
    }

    public static List<Piece> getPieces(Piece.Color color) {
        List<Piece> piecesSameColor = new ArrayList<Piece>();
        for (Map.Entry<Piece.Type, List<Piece>> piecesEntry : pieceSet.get(color).entrySet()) {
            for (Piece piece : piecesEntry.getValue()) {
                piecesSameColor.add(piece);
            }
        }
        return piecesSameColor;
    }

    public static List<Piece> getPieces(Piece.Color color, Piece.Type type) {
        return pieceSet.get(color).get(type);
    }

    //It sets all the ranks and files of the pieces.
    private static void setRankAndFile() {
        for (int i = 1; i <= 8; i++) {
            for (char j = 'a'; j <= 'h'; j++) {
                Square currentSquare = board.Board.getSquare(j, i);
                if (currentSquare.getCurrentPiece() != null) {
                    currentSquare.getCurrentPiece().setRank(i);
                    currentSquare.getCurrentPiece().setFile(j);
                }
            }
        }
    }

    //This method will get all the pieces on the board and adds them to an array.
    public static ArrayList<Piece> getAvailablePieces() {
        setRankAndFile();
        ArrayList<Piece> availablePieces = new ArrayList<Piece>();
        for (int i = 1; i <= 8; i++) {
            for (char j = 'a'; j <= 'h'; j++) {
                Square currentSquare = board.Board.getSquare(j, i);
                if (currentSquare.getCurrentPiece() != null) {
                    //addes the piece to the available list
                    availablePieces.add(currentSquare.getCurrentPiece());
                }
            }
        }
        return availablePieces;
    }

    //overloadeded method from above. It only returns all pieces with the desired color
    public static ArrayList<Piece> getAvailablePieces(Piece.Color color) {
        setRankAndFile();
        ArrayList<Piece> availablePieces = new ArrayList<Piece>();
        for (int i = 1; i <= 8; i++) {
            for (char j = 'a'; j <= 'h'; j++) {
                Square currentSquare = board.Board.getSquare(j, i);
                if (currentSquare.getCurrentPiece() != null && currentSquare.getCurrentPiece().getColor() == color) {
                    //addes the piece to the available list
                    availablePieces.add(currentSquare.getCurrentPiece());
                }
            }
        }
        return availablePieces;
    }

    //overloadeded method from above. It only returns all avaialble single typ of pieces with a desired color.
    public static ArrayList<Piece> getAvailablePieces(Piece.Color color, Piece.Type type) {
        setRankAndFile();
        ArrayList<Piece> availablePieces = new ArrayList<Piece>();
        for (int i = 1; i <= 8; i++) {
            for (char j = 'a'; j <= 'h'; j++) {
                Square currentSquare = board.Board.getSquare(j, i);
                if (currentSquare.getCurrentPiece() != null && currentSquare.getCurrentPiece().getColor() == color
                        && currentSquare.getCurrentPiece().getType() == type) {
                    //addes the piece to the available list
                    availablePieces.add(currentSquare.getCurrentPiece());
                }
            }
        }
        return availablePieces;
    }

    public static void addCapturedPiece(Piece piece) {
        piece.setCapture(true);
        capturedPieceSet.get(piece.getColor()).push(piece);
    }

    public static List<Piece> getCapturedPieces(Piece.Color color) {
        return capturedPieceSet.get(color);
    }


    private static void initializePieceSet() {
        pieceSet = new LinkedHashMap<Piece.Color, Map<Piece.Type, List<Piece>>>();

        Map<Piece.Type, List<Piece>> whitePieces = new LinkedHashMap<Piece.Type, List<Piece>>();
        Map<Piece.Type, List<Piece>> blackPieces = new LinkedHashMap<Piece.Type, List<Piece>>();

        //rooks
        List<Piece> whiteRooks = new ArrayList<Piece>();
        List<Piece> blackRooks = new ArrayList<Piece>();
        for (int i = 0; i < 2; i++) {
            whiteRooks.add(new Rook(Piece.Color.WHITE));
            blackRooks.add(new Rook(Piece.Color.BLACK));
        }
        whitePieces.put(Piece.Type.ROOK, whiteRooks);
        blackPieces.put(Piece.Type.ROOK, blackRooks);
        //knights
        List<Piece> whiteKnights = new ArrayList<Piece>();
        List<Piece> blackKnights = new ArrayList<Piece>();
        for (int i = 0; i < 2; i++) {
            whiteKnights.add(new Knight(Piece.Color.WHITE));
            blackKnights.add(new Knight(Piece.Color.BLACK));
        }
        whitePieces.put(Piece.Type.KNIGHT, whiteKnights);
        blackPieces.put(Piece.Type.KNIGHT, blackKnights);
        //Bishop
        List<Piece> whiteBishops = new ArrayList<Piece>();
        List<Piece> blackBishops = new ArrayList<Piece>();
        for (int i = 0; i < 2; i++) {
            whiteBishops.add(new Bishop(Piece.Color.WHITE));
            blackBishops.add(new Bishop(Piece.Color.BLACK));
        }
        whitePieces.put(Piece.Type.BISHOP, whiteBishops);
        blackPieces.put(Piece.Type.BISHOP, blackBishops);
        //Queen
        List<Piece> whiteQueen = new ArrayList<Piece>();
        List<Piece> blackQueen = new ArrayList<Piece>();
        whiteQueen.add(new Queen(Piece.Color.WHITE));
        blackQueen.add(new Queen(Piece.Color.BLACK));
        whitePieces.put(Piece.Type.QUEEN, whiteQueen);
        blackPieces.put(Piece.Type.QUEEN, blackQueen);
        //King
        List<Piece> whiteKing = new ArrayList<Piece>();
        List<Piece> blackKing = new ArrayList<Piece>();
        whiteKing.add(new King(Piece.Color.WHITE));
        blackKing.add(new King(Piece.Color.BLACK));
        whitePieces.put(Piece.Type.KING, whiteKing);
        blackPieces.put(Piece.Type.KING, blackKing);
        //Pawns
        List<Piece> whitePawns = new ArrayList<Piece>();
        List<Piece> blackPawns = new ArrayList<Piece>();
        for (int i = 0; i < 8; i++) {
            whitePawns.add(new Pawn(Piece.Color.WHITE));
            blackPawns.add(new Pawn(Piece.Color.BLACK));
        }
        whitePieces.put(Piece.Type.PAWN, whitePawns);
        blackPieces.put(Piece.Type.PAWN, blackPawns);

        //black and white
        pieceSet.put(Piece.Color.WHITE, whitePieces);
        pieceSet.put(Piece.Color.BLACK, blackPieces);
    }

    private static void initializeCapturedPieceSet() {
        capturedPieceSet = new LinkedHashMap<Piece.Color, Stack<Piece>>();
        Stack<Piece> whiteCapturedPieces = new Stack<Piece>();
        Stack<Piece> blackCapturedPieces = new Stack<Piece>();
        capturedPieceSet.put(Piece.Color.WHITE, whiteCapturedPieces);
        capturedPieceSet.put(Piece.Color.BLACK, blackCapturedPieces);
    }


}
