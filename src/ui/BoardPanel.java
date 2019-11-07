package ui;

import pieces.Piece;
import pieces.PieceSet;
import util.Core;
import util.GameModel;
import util.Move;

import javax.swing.*;
import java.awt.*;
import java.util.Iterator;
import java.util.Observable;
import java.util.Observer;

public class BoardPanel extends JPanel implements Observer {

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
        if (getSquarePanel(originFile, originRank).getComponent(0) != null ) {
            getSquarePanel(originFile, originRank).getComponent(0).setVisible(true);
            gameModel.onMoveRequest(originFile, originRank, destinationFile, destinationRank);
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
        if (originPiece != null) {
            getSquarePanel(originFile, originRank).getComponent(0).setVisible(false);
            JLabel draggedPieceImageLabel = getPieceImageLabel(originPiece);
            draggedPieceImageLabel.setLocation(dragX, dragY);
            draggedPieceImageLabel.setSize(SQUARE_DIMENSION, SQUARE_DIMENSION);
            boardLayeredPane.add(draggedPieceImageLabel, JLayeredPane.DRAG_LAYER);
        }
    }

    public void executeDrag(int dragX, int dragY) {
        JLabel draggedPieceImageLabel = (JLabel) boardLayeredPane.getComponentsInLayer(JLayeredPane.DRAG_LAYER)[0];
        if (draggedPieceImageLabel != null) {
            draggedPieceImageLabel.setLocation(dragX, dragY);
        }
    }

    public void postDrag() {
        JLabel draggedPieceImageLabel = (JLabel) boardLayeredPane.getComponentsInLayer(JLayeredPane.DRAG_LAYER)[0];
        boardLayeredPane.remove(draggedPieceImageLabel);
        boardLayeredPane.repaint();
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
