package ui;

import util.GameModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observable;
import java.util.Observer;

public class ControlPanel extends JPanel implements Observer {

    private GameModel gameModel;

    private JButton undoButton;
    private JButton saveButton;
    private JButton loadButton;

    public ControlPanel(GameModel gameModel) {
        this.gameModel = gameModel;
        initialize();
        gameModel.addObserver(this);
    }

    private void initialize() {
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.setLayout(new GridLayout(0, 1));

        undoButton = new JButton("Request Undo");
        undoButton.setEnabled(false);
        saveButton = new JButton("Save Game");
        saveButton.setEnabled(false);
        loadButton = new JButton("Load Game");
        loadButton.setEnabled(false);

        this.add(undoButton);
        this.add(saveButton);
        this.add(loadButton);
        this.setPreferredSize(new Dimension(300, 200));
    }

    @Override
    public void update(Observable o, Object arg) {

    }

}
