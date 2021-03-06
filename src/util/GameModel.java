package util;

import board.Board;
import pieces.*;
import ui.*;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observable;

public class GameModel extends Observable  {

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

        if (MoveValidator.notCurrentMoveColor == move.getPiece().getColor()) {
            blackPawnPromote(move);
            whitePawnPromote(move);
        }
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
        //Piece.Color currentColor = move.getPiece().getColor();
        if (MoveValidator.currentMoveColor == Piece.Color.BLACK) {
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
    //promotion for pawns
//it shows the promotion message if pawn moves to the location
    public void blackPawnPromote(Move move) {
        if (move.getDestinationRank() == 1 && move.getPiece().getColor() == Piece.Color.BLACK
                && move.getPiece().getType() == Piece.Type.PAWN) {
            //This shows the promotion if black pawn reaches the rank 1
            GameFrame.showPromotionDialog();
            //This sends the needed info to the board to change the Pawn to the chosen piece
            boardPanel.initializePromotePieces(move, GameFrame.selectedPromotionValue);
            //This method returns an array of all available pieces and sets all ranks and files of pieces.
            PieceSet.getAvailablePieces();
            //this sets all dangered paths.
            DangerPaths.setDangeredSquares();
        }
    }
    public void whitePawnPromote(Move move) {
        if (move.getDestinationRank() == 8 && move.getPiece().getColor() == Piece.Color.WHITE
                && move.getPiece().getType() == Piece.Type.PAWN) {
            //This shows the promotion if black pawn reaches the rank 1
            GameFrame.showPromotionDialog();
            //This sends the needed info to the board to change the Pawn to the chosen piece
            boardPanel.initializePromotePieces(move, GameFrame.selectedPromotionValue);
            //This method returns an array of all available pieces and sets all ranks and files of pieces.
            PieceSet.getAvailablePieces();
            //this sets all dangered paths.
            DangerPaths.setDangeredSquares();
        }
    }
    //
    public void loadGame(){
        //Read the text files for settings:
        SaveingAndLoading.loadSettings();
        //set current colors:
        MoveValidator.setCurrentMoveColor(SaveingAndLoading.getLoadedCurrentColor());
        MoveValidator.setNotCurrentMoveColor(SaveingAndLoading.getLoadedNotCurrentColor());
        MoveValidator.getKingSquare();
       //load time
        stopTimer();
        timerPanel.setWhiteTime(SaveingAndLoading.getLoadedWhiteTime());
        timerPanel.setBlackTime(SaveingAndLoading.getLoadedBlackTime());
        if (SaveingAndLoading.getLoadedCurrentColor() == Piece.Color.BLACK) {
            whiteTimer.stop();
            blackTimer.start();

        } else {
            blackTimer.stop();
            whiteTimer.start();
        }

        //get all current available pieces
        PieceSet.getAvailablePieces();
        //Read the text file for the piece locations
        SaveingAndLoading.loadGame();
        boardPanel.initializeLoadedPieces();
        //this sets all dangered paths.
        DangerPaths.setDangeredSquares();
        SaveingAndLoading.removeLoadedPieces();
    }
    public void giveTimerValuesToSaver() {
        SaveingAndLoading.setBlackTime(timerPanel.getBlackTime().toString());
        SaveingAndLoading.setWhiteTime(timerPanel.getWhiteTime().toString());
    }
}
