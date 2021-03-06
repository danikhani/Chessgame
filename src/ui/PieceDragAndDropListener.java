package ui;

import board.Board;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.Serializable;

public class PieceDragAndDropListener implements MouseListener, MouseMotionListener {

    private BoardPanel boardPanel;

    private boolean dragging;
    private char originFile;
    private int originRank;
    private int dragOffsetX;
    private int dragOffsetY;
    private char enteredFile;
    private int enteredRank;

    public PieceDragAndDropListener(BoardPanel boardPanel) {
        this.boardPanel = boardPanel;
    }

    @Override
    public void mousePressed(MouseEvent e) {
        originFile = calculateFile(e);
        originRank = calculateRank(e);

        if (boardPanel.isBoardReversed()) {
            dragOffsetX = e.getPoint().x - boardPanel.SQUARE_DIMENSION * ('h' - calculateFile(e));
            dragOffsetY = e.getPoint().y - boardPanel.SQUARE_DIMENSION * (calculateRank(e) - 1);
        } else {
            dragOffsetX = e.getPoint().x - boardPanel.SQUARE_DIMENSION * (calculateFile(e) - 'a');
            dragOffsetY = e.getPoint().y - boardPanel.SQUARE_DIMENSION * (8 - calculateRank(e));
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (dragging) {
            boardPanel.postDrag();
            boardPanel.submitMoveRequest(originFile, originRank, calculateFile(e), calculateRank(e));
        }
        dragging = false;
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        try {
            if (dragging) {
                boardPanel.executeDrag(e.getPoint().x - dragOffsetX, e.getPoint().y - dragOffsetY);
            } else {
                boardPanel.preDrag(originFile, originRank, e.getPoint().x - dragOffsetX, e.getPoint().y - dragOffsetY);
                dragging = true;
            }
        } catch (NullPointerException b) {
            System.out.println("There is no Piece");
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        // nothing
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        /*enteredFile = calculateFile(e);
        enteredRank = calculateRank(e);
        System.out.println("file is "+enteredFile + " rank is " + enteredRank);*/

    }

    @Override
    public void mouseExited(MouseEvent e) {
        // nothing
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        // nothing
    }

    private char calculateFile(MouseEvent e) {
        if (boardPanel.isBoardReversed()) {
            return (char) ('h' - e.getPoint().x / boardPanel.SQUARE_DIMENSION);
        } else {
            return (char) ('a' + e.getPoint().x / boardPanel.SQUARE_DIMENSION);
        }
    }

    private int calculateRank(MouseEvent e) {
        if (boardPanel.isBoardReversed()) {
            return 1 + e.getPoint().y / boardPanel.SQUARE_DIMENSION;
        } else {
            return 8 - e.getPoint().y / boardPanel.SQUARE_DIMENSION;
        }
    }

}