package ui;

import board.Square;
import pieces.*;
import util.*;

import javax.swing.*;
import java.awt.*;
import java.io.Serializable;
import java.util.Iterator;
import java.util.Observable;
import java.util.Observer;

public class BoardPanel extends JPanel implements Observer, Serializable {

    public static final int SQUARE_DIMENSION = 100;

    private GameModel gameModel;
    private boolean boardReversed;
    private boolean usingCustomPieces;
    private JLayeredPane boardLayeredPane;
    private JPanel boardPanel;
    private JPanel[][] squarePanels;

    public BoardPanel(GameModel gameModel) {
        super(new BorderLayout());
        this.gameModel = gameModel;
        this.boardReversed = Core.getPreferences().isBoardReversed();
        this.usingCustomPieces = Core.getPreferences().isUsingCustomPieces();
        initializeBoardLayeredPane();
        initializeSquares();
        initializePieces();
        gameModel.addObserver(this);
    }

    public void submitMoveRequest(char originFile, int originRank, char destinationFile, int destinationRank) {
        try {
            if (getSquarePanel(originFile, originRank).getComponent(0) != null) {
                getSquarePanel(originFile, originRank).getComponent(0).setVisible(true);
                gameModel.onMoveRequest(originFile, originRank, destinationFile, destinationRank);
            }
        }
        catch(ArrayIndexOutOfBoundsException e){
            System.out.println("submitMoveRequest Exception, Empty Field");
        }
    }

    public void executeMove(Move move) {
        JPanel originSquarePanel = getSquarePanel(move.getOriginFile(), move.getOriginRank());
        JPanel destinationSquarePanel = getSquarePanel(move.getDestinationFile(), move.getDestinationRank());
        destinationSquarePanel.removeAll();
        destinationSquarePanel.add(originSquarePanel.getComponent(0));
        destinationSquarePanel.repaint();
        originSquarePanel.removeAll();
        originSquarePanel.repaint();
    }

    public void preDrag(char originFile, int originRank, int dragX, int dragY) {
        Piece originPiece = gameModel.queryPiece(originFile, originRank);
        MoveValidator.setDraggedColor(originPiece.getColor());
        if (originPiece != null) {
            getSquarePanel(originFile, originRank).getComponent(0).setVisible(false);
            JLabel draggedPieceImageLabel = getPieceImageLabel(originPiece);
            draggedPieceImageLabel.setLocation(dragX, dragY);
            draggedPieceImageLabel.setSize(SQUARE_DIMENSION, SQUARE_DIMENSION);
            boardLayeredPane.add(draggedPieceImageLabel, JLayeredPane.DRAG_LAYER);
            if(MoveValidator.currentMoveColor == MoveValidator.getDraggedColor()){
                putHelperColor(originPiece);
            }
        }
    }

    public void executeDrag(int dragX, int dragY) {
        try{
        JLabel draggedPieceImageLabel = (JLabel) boardLayeredPane.getComponentsInLayer(JLayeredPane.DRAG_LAYER)[0];
        if (draggedPieceImageLabel != null) {
            draggedPieceImageLabel.setLocation(dragX, dragY);
        }
        }catch(ArrayIndexOutOfBoundsException e){
            System.out.println("executeDrag Exception, Empty Drag");
        }
    }

    public void postDrag() {
        try {
            JLabel draggedPieceImageLabel = (JLabel) boardLayeredPane.getComponentsInLayer(JLayeredPane.DRAG_LAYER)[0];
            boardLayeredPane.remove(draggedPieceImageLabel);
            boardLayeredPane.repaint();
            removeHelperColor();
        }catch(ArrayIndexOutOfBoundsException e){
            System.out.println("postDrag Exception, Empty Drag");
        }
    }

    public JPanel getSquarePanel(char file, int rank) {
        file = Character.toLowerCase(file);
        if (file < 'a' || file > 'h' || rank < 1 || rank > 8) {
            return null;
        } else {
            return squarePanels[file - 'a'][rank - 1];
        }
    }

    private void initializeSquares() {
        squarePanels = new JPanel[8][8];
        if (boardReversed) {
            for (int r = 0; r < 8; r ++) {
                for (int f = 7; f >= 0; f--) {
                    initializeSingleSquarePanel(f, r);
                }
            }
        } else {
            for (int r = 7; r >= 0; r --) {
                for (int f = 0; f < 8; f++) {
                    initializeSingleSquarePanel(f, r);
                }
            }
        }
    }

    private void initializeSingleSquarePanel(int f, int r) {
        squarePanels[f][r] = new JPanel(new GridLayout(1, 1));
        squarePanels[f][r].setPreferredSize(new Dimension(SQUARE_DIMENSION, SQUARE_DIMENSION));
        squarePanels[f][r].setSize(new Dimension(SQUARE_DIMENSION, SQUARE_DIMENSION));
        squarePanels[f][r].setBackground(f % 2 == r % 2 ? Color.GRAY : Color.WHITE);
        boardPanel.add(squarePanels[f][r]);
    }

//this will set the reachable and attackable squares to another color.
    private  boolean otherPiecesKingSquare(Square kingSquare) {
        for (Piece otherPieces : PieceSet.getAvailablePieces(MoveValidator.notCurrentMoveColor)) {
            if (otherPieces.hasDangered(kingSquare)) {
                return true;
            }
        }
        return false;
    }
    private  boolean pawnHasKingSquare(Square kingSquare) {
        for (Piece otherPieces : PieceSet.getAvailablePieces(MoveValidator.notCurrentMoveColor, Piece.Type.PAWN )) {
            if (otherPieces.hasDangerousForKingSquares(kingSquare)) {
                return true;
            }
        }
        return false;
    }
    public void putHelperColor(Piece piece) {
        //TODO: if the piece is king then the dangered locations shouldnt get marked. Should get fixed
        if (piece.getType() == Piece.Type.KING) {
            //get all dangered pieces of the king.
            for (Square dangered : piece.getDangered()) {
                if (!otherPiecesKingSquare(dangered) && !pawnHasKingSquare(dangered)) {
                    if (dangered.getCurrentPiece() == null) {
                        getSquarePanel(dangered.getFile(), dangered.getRank()).setBackground(Color.GREEN);
                    } else if (dangered.getCurrentPiece().getColor() != piece.getColor()) {
                        getSquarePanel(dangered.getFile(), dangered.getRank()).setBackground(Color.RED);
                    }
                }
            }
        } else if (piece.getType() == Piece.Type.PAWN) {
            for (Square dangered : piece.getDangered()) {
                if (dangered.getCurrentPiece() == null) {
                    getSquarePanel(dangered.getFile(), dangered.getRank()).setBackground(Color.GREEN);
                } else if (dangered.getCurrentPiece().getColor() != piece.getColor()) {
                    getSquarePanel(dangered.getFile(), dangered.getRank()).setBackground(Color.RED);
                }
            }
            for (Square reachable : piece.getReachable()) {
                if (reachable.getCurrentPiece() == null) {
                    getSquarePanel(reachable.getFile(), reachable.getRank()).setBackground(Color.GREEN);
                }
            }
        } else {
            for (Square dangered : piece.getDangered()) {
                if (dangered.getCurrentPiece() == null) {
                    getSquarePanel(dangered.getFile(), dangered.getRank()).setBackground(Color.GREEN);
                } else if (dangered.getCurrentPiece().getColor() != piece.getColor()) {
                    getSquarePanel(dangered.getFile(), dangered.getRank()).setBackground(Color.RED);
                }
            }
        }
    }

//this will remove the set color by the putHelperColor.
    public void removeHelperColor(){
        if (boardReversed) {
            for (int r = 0; r < 8; r ++) {
                for (int f = 7; f >= 0; f--) {
                    squarePanels[f][r].setBackground(f % 2 == r % 2 ? Color.GRAY : Color.WHITE);
                }
            }
        } else {
            for (int r = 7; r >= 0; r --) {
                for (int f = 0; f < 8; f++) {
                    squarePanels[f][r].setBackground(f % 2 == r % 2 ? Color.GRAY : Color.WHITE);
                }
            }
        }
    }
    //This method is for promoting the pawn after the choice from jpanel got made.
    public void initializePromotePieces(Move move , int chosenPiece) {
        Piece originPiece = move.getPiece();
        Piece.Color color = move.getPiece().getColor();
        Square currentSquare = board.Board.getSquare(move.getDestinationFile(), move.getDestinationRank());
        JPanel originSquarePanel = getSquarePanel(move.getDestinationFile(), move.getDestinationRank());
        //to remove the current Piece and panel
        originPiece.setCapture(true);
        currentSquare.setCurrentPiece(null);
        originSquarePanel.removeAll();
        originSquarePanel.repaint();
        //Its for changing the piece to the one that got chosen:
        switch(chosenPiece){
            case 0:
                currentSquare.setCurrentPiece(new Queen(color));
                originSquarePanel.add(getPieceImageLabel(new Queen(color)));
                break;
            case 1:
                currentSquare.setCurrentPiece(new Knight(color));
                originSquarePanel.add(getPieceImageLabel(new Knight(color)));
                break;
            case 2:
                currentSquare.setCurrentPiece(new Rook(color));
                originSquarePanel.add(getPieceImageLabel(new Rook(color)));
                break;
            case 3:
                currentSquare.setCurrentPiece(new Bishop(color));
                originSquarePanel.add(getPieceImageLabel(new Bishop(color)));
                break;
        }
        originSquarePanel.repaint();
    }


    public void initializeLoadedPieces() {
        for(Piece removingPiece: PieceSet.getAvailablePieces()){
            Square currentSquare = board.Board.getSquare(removingPiece.getFile(), removingPiece.getRank());
            JPanel currentSquarePannel = getSquarePanel(removingPiece.getFile(), removingPiece.getRank());
            //to remove the current Piece and panel
            currentSquare.setCurrentPiece(null);
            currentSquarePannel.removeAll();
            currentSquarePannel.repaint();
        }
        for(Piece loadingPiece: SaveingAndLoading.getLoadedPieces()){
            Square currentSquare = board.Board.getSquare(loadingPiece.getFile(), loadingPiece.getRank());
            JPanel currentSquarePannel = getSquarePanel(loadingPiece.getFile(), loadingPiece.getRank());
            currentSquare.setCurrentPiece(loadingPiece);
            currentSquarePannel.add(getPieceImageLabel(loadingPiece));
            currentSquarePannel.repaint();
        }
    }

    private void initializePieces() {
        /*
        TODO-piece
            Initialize pieces on board
            Check following code to implement other pieces
            Highly recommended to use same template!
         */
        // rooks
        Iterator<Piece> whiteRooksIterator = PieceSet.getPieces(Piece.Color.WHITE, Piece.Type.ROOK).iterator();
        Iterator<Piece> blackRooksIterator = PieceSet.getPieces(Piece.Color.BLACK, Piece.Type.ROOK).iterator();
        getSquarePanel('a', 1).add(getPieceImageLabel(whiteRooksIterator.next()));
        getSquarePanel('h', 1).add(getPieceImageLabel(whiteRooksIterator.next()));
        getSquarePanel('a', 8).add(getPieceImageLabel(blackRooksIterator.next()));
        getSquarePanel('h', 8).add(getPieceImageLabel(blackRooksIterator.next()));
        // Knight
        Iterator<Piece> whiteKnightsIterator = PieceSet.getPieces(Piece.Color.WHITE, Piece.Type.KNIGHT).iterator();
        Iterator<Piece> blackKnightsIterator = PieceSet.getPieces(Piece.Color.BLACK, Piece.Type.KNIGHT).iterator();
        getSquarePanel('b', 1).add(getPieceImageLabel(whiteKnightsIterator.next()));
        getSquarePanel('g', 1).add(getPieceImageLabel(whiteKnightsIterator.next()));
        getSquarePanel('b', 8).add(getPieceImageLabel(blackKnightsIterator.next()));
        getSquarePanel('g', 8).add(getPieceImageLabel(blackKnightsIterator.next()));
        // Bishop
        Iterator<Piece> whiteBishopsIterator = PieceSet.getPieces(Piece.Color.WHITE, Piece.Type.BISHOP).iterator();
        Iterator<Piece> blackBishopsIterator = PieceSet.getPieces(Piece.Color.BLACK, Piece.Type.BISHOP).iterator();
        getSquarePanel('c', 1).add(getPieceImageLabel(whiteBishopsIterator.next()));
        getSquarePanel('f', 1).add(getPieceImageLabel(whiteBishopsIterator.next()));
        getSquarePanel('c', 8).add(getPieceImageLabel(blackBishopsIterator.next()));
        getSquarePanel('f', 8).add(getPieceImageLabel(blackBishopsIterator.next()));
        // Queen
        Iterator<Piece> whiteQueenIterator = PieceSet.getPieces(Piece.Color.WHITE, Piece.Type.QUEEN).iterator();
        Iterator<Piece> blackQueenIterator = PieceSet.getPieces(Piece.Color.BLACK, Piece.Type.QUEEN).iterator();
        getSquarePanel('d', 1).add(getPieceImageLabel(whiteQueenIterator.next()));
        getSquarePanel('d', 8).add(getPieceImageLabel(blackQueenIterator.next()));
        // King
        Iterator<Piece> whiteKingIterator = PieceSet.getPieces(Piece.Color.WHITE, Piece.Type.KING).iterator();
        Iterator<Piece> blackKingIterator = PieceSet.getPieces(Piece.Color.BLACK, Piece.Type.KING).iterator();
        getSquarePanel('e', 1).add(getPieceImageLabel(whiteKingIterator.next()));
        getSquarePanel('e', 8).add(getPieceImageLabel(blackKingIterator.next()));
        // Pawn
        Iterator<Piece> whitePawnsIterator = PieceSet.getPieces(Piece.Color.WHITE, Piece.Type.PAWN).iterator();
        Iterator<Piece> blackPawnsIterator = PieceSet.getPieces(Piece.Color.BLACK, Piece.Type.PAWN).iterator();
        getSquarePanel('a', 2).add(getPieceImageLabel(whitePawnsIterator.next()));
        getSquarePanel('b', 2).add(getPieceImageLabel(whitePawnsIterator.next()));
        getSquarePanel('c', 2).add(getPieceImageLabel(whitePawnsIterator.next()));
        getSquarePanel('d', 2).add(getPieceImageLabel(whitePawnsIterator.next()));
        getSquarePanel('e', 2).add(getPieceImageLabel(whitePawnsIterator.next()));
        getSquarePanel('f', 2).add(getPieceImageLabel(whitePawnsIterator.next()));
        getSquarePanel('g', 2).add(getPieceImageLabel(whitePawnsIterator.next()));
        getSquarePanel('h', 2).add(getPieceImageLabel(whitePawnsIterator.next()));

        getSquarePanel('a', 7).add(getPieceImageLabel(blackPawnsIterator.next()));
        getSquarePanel('b', 7).add(getPieceImageLabel(blackPawnsIterator.next()));
        getSquarePanel('c', 7).add(getPieceImageLabel(blackPawnsIterator.next()));
        getSquarePanel('d', 7).add(getPieceImageLabel(blackPawnsIterator.next()));
        getSquarePanel('e', 7).add(getPieceImageLabel(blackPawnsIterator.next()));
        getSquarePanel('f', 7).add(getPieceImageLabel(blackPawnsIterator.next()));
        getSquarePanel('g', 7).add(getPieceImageLabel(blackPawnsIterator.next()));
        getSquarePanel('h', 7).add(getPieceImageLabel(blackPawnsIterator.next()));
    }

    private void initializeBoardLayeredPane() {
        boardPanel = new JPanel(new GridLayout(8, 8));
        boardPanel.setBounds(0, 0, 800, 800);
        boardLayeredPane = new JLayeredPane();
        boardLayeredPane.setPreferredSize(new Dimension(800, 800));
        boardLayeredPane.add(boardPanel, JLayeredPane.DEFAULT_LAYER);
        PieceDragAndDropListener pieceDragAndDropListener = new PieceDragAndDropListener(this);
        boardLayeredPane.addMouseListener(pieceDragAndDropListener);
        boardLayeredPane.addMouseMotionListener(pieceDragAndDropListener);
        boardLayeredPane.setVisible(true);
        this.add(boardLayeredPane, BorderLayout.CENTER);
    }

    private JLabel getPieceImageLabel(Piece piece) {
        Image pieceImage = new ImageIcon(getClass().getResource(piece.getImageFileName())).getImage();
        pieceImage = pieceImage.getScaledInstance(SQUARE_DIMENSION, SQUARE_DIMENSION, Image.SCALE_SMOOTH);
        JLabel pieceImageLabel = new JLabel(new ImageIcon(pieceImage));
        return pieceImageLabel;
    }

    public boolean isBoardReversed() {
        return boardReversed;
    }

    @Override
    public void update(Observable o, Object arg) {
        executeMove((Move) arg);
    }
}
