package ui;

import util.Core;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LaunchFrame extends JFrame {

    private JPanel bannerPanel;
    private JLabel bannerLabel;

    private JPanel buttonsPanel;
    private JPanel newGameButtonPanel;
    private JPanel loadGameButtonPanel;
    private JButton newGameButton;
    private JButton loadGameButton;
    private JFileChooser loadGameFileChooser;

    public LaunchFrame() {
        super("CSI2102 at YSU | Main");
        this.setIconImage(new ImageIcon(getClass().getResource("/ysu.jpg")).getImage());
        loadInterface();
    }

    private void loadInterface() {
        initializeBannerPanel();
        initializeButtonsPanel();

        this.setLayout(new BorderLayout());
        this.add(bannerPanel, BorderLayout.NORTH);
        this.add(buttonsPanel, BorderLayout.SOUTH);

        this.pack();
        this.setVisible(true);
        this.setResizable(false);
        this.setLocationRelativeTo(null);
    }

    private void initializeBannerPanel() {
        bannerLabel = new JLabel();
        bannerLabel.setIcon(new ImageIcon(getClass().getResource("/launch_banner.png")));
        bannerPanel = new JPanel();
        bannerPanel.add(bannerLabel);
        bannerPanel.setPreferredSize(new Dimension(600, 250));
        bannerPanel.setBackground(Color.LIGHT_GRAY);
    }

    private void initializeButtonsPanel() {
        newGameButton = new JButton("New Game");
        newGameButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                PreferencesFrame preferencesFrame = Core.getPreferencesFrame();
                preferencesFrame = new PreferencesFrame();
                setVisible(false);
            }
        });
        newGameButtonPanel = new JPanel(new GridLayout(1, 1));
        newGameButtonPanel.setBorder(BorderFactory.createEmptyBorder(40, 50, 40, 25));
        newGameButtonPanel.add(newGameButton);
        loadGameButton = new JButton("Load Game");
        loadGameButton.setEnabled(false);
        loadGameButtonPanel = new JPanel(new GridLayout(1, 1));
        loadGameButtonPanel.setBorder(BorderFactory.createEmptyBorder(40, 25, 40, 50));
        loadGameButtonPanel.add(loadGameButton);


        buttonsPanel = new JPanel(new GridLayout(1, 2));
        buttonsPanel.setPreferredSize(new Dimension(600, 150));
        buttonsPanel.add(newGameButtonPanel);
        buttonsPanel.add(loadGameButtonPanel);
    }

}
