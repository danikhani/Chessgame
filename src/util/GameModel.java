package util;

import board.Board;
import board.Square;
import pieces.*;
import ui.*;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Iterator;
import java.util.Observable;

//import static board.Board.initializePromotion;


public class GameModel extends Observable {

    private GameFrame gameFrame;
    private BoardPanel boardPanel;
    private TimerPanel timerPanel;
    private ControlPanel controlPanel;
    private MoveHistoryPanel moveHistoryPanel;

    private Timer whiteTimer;
    private Timer blackTimer;

    public GameModel() {
        initialize();
    }

    private void initialize() {
        initializeTimers();
        initializeUIComponents();
        //This method returns an array of all available pieces and sets all ranks and files of pieces.
        PieceSet.getAvailablePieces();
        //this sets all dangered paths.
        DangerPaths.setDangeredSquares();
    }

    public void onMoveRequest(char originFile, int originRank, char destinationFile, int destinationRank) {
        onLocalMoveRequest(originFile, originRank, destinationFile, destinationRank);
    }

    private void onLocalMoveRequest(char originFile, int originRank, char destinationFile, int destinationRank) {
        Move move = new Move(originFile, originRank, destinationFile, destinationRank);
        if (MoveValidator.validateMove(move)) {
            executeMove(move);
        } else {
            //
        }
    }

    private void executeMove(Move move) {
        MoveLogger.addMove(move);
        Board.executeMove(move);
        moveHistoryPanel.printMove(move);
        boardPanel.executeMove(move);
        switchTimer(move);
        //This method returns an array of all available pieces and sets all ranks and files of pieces.
        PieceSet.getAvailablePieces();
        //This method sets all ranks and files of pieces.
        //  PieceSet.setRankAndFile();
        //this sets all dangered paths.
        DangerPaths.setDangeredSquares();
        //check for promotions
        blackPawnPromotionable();
        if (MoveValidator.isCheckMove(move)) {
            if (MoveValidator.isCheckMate(move)) {
                stopTimer();
                gameFrame.showCheckmateDialog();
            } else {
                gameFrame.showCheckDialog();
            }
        }
    }

    public Piece queryPiece(char file, int rank) {
        return Board.getSquare(file, rank).getCurrentPiece();
    }

    private void initializeUIComponents() {
        boardPanel = new BoardPanel(this);
        timerPanel = new TimerPanel(this);
        controlPanel = new ControlPanel(this);
        moveHistoryPanel = new MoveHistoryPanel(this);
        gameFrame = new GameFrame(this);
    }

    private void initializeTimers() {
        whiteTimer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                timerPanel.whiteTimerTikTok();
            }
        });
        blackTimer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                timerPanel.blackTimerTikTok();
            }
        });
    }

    private void switchTimer(Move move) {
        Piece.Color currentColor = move.getPiece().getColor();
        if (move.getPiece().getColor() == Piece.Color.WHITE) {
            whiteTimer.stop();
            blackTimer.start();
        } else {
            whiteTimer.start();
            blackTimer.stop();
        }
    }

    private void stopTimer() {
        whiteTimer.stop();
        blackTimer.stop();
        // TODO-timer: stop timers
    }

    public BoardPanel getBoardPanel() {
        return boardPanel;
    }

    public TimerPanel getTimerPanel() {
        return timerPanel;
    }

    public ControlPanel getControlPanel() {
        return controlPanel;
    }

    public MoveHistoryPanel getMoveHistoryPanel() {
        return moveHistoryPanel;
    }

    public void blackPawnPromotionable() {
        for (Piece blackPawn : PieceSet.getAvailablePieces(Piece.Color.BLACK, Piece.Type.PAWN)) {
            if (blackPawn.getRank() == 6) {
                GameFrame.showPromotionDialog();
                changePawn(blackPawn);
            }
        }
    }
    private void changePawn(Piece piece){
        //Queen", "Knight", "Rook", "Bishop"
//TODO: Promotion graphic should get passed
        piece.getColor();
        Square currentSquare = board.Board.getSquare( piece.getFile(), piece.getRank());

        switch(GameFrame.selectedPromotionValue){
            case 0:
                System.out.println("we want a queen");
                PieceSet.addCapturedPiece(currentSquare.getCurrentPiece());
                BoardPanel board = new BoardPanel(this);
                JPanel destinationSquarePanel = board.getSquarePanel(piece.getFile(), piece.getRank());
                //destinationSquarePanel.getComponent(0).setVisible(false);
                piece.setCapture(true);
                currentSquare.setCurrentPiece(null);
                destinationSquarePanel.removeAll();
                System.out.println(currentSquare.getCurrentPiece());
                currentSquare.setCurrentPiece(new Rook(Piece.Color.BLACK));
                System.out.println(currentSquare.getCurrentPiece());

                //JPanel originSquarePanel = board.getSquarePanel(piece.getFile(), piece.getRank());

                //destinationSquarePanel.add(getPieceImageLabel(bl()));

                //destinationSquarePanel.add();
                destinationSquarePanel.repaint();
                /*Iterator<Piece> whiteRooksIterator = PieceSet.getPieces(Piece.Color.BLACK, Piece.Type.ROOK).iterator();
                currentSquare.setCurrentPiece(whiteRooksIterator.next());*/

        }
    }

}
