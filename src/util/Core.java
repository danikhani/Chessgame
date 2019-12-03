package util;

import ui.GameFrame;
import ui.LaunchFrame;
import ui.PreferencesFrame;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class Core {

    private static Core coreInstance = new Core();
    private static boolean inGame;

    private static GameModel gameModel;
    private static Preferences preferences;

    private static LaunchFrame launchFrame;
    private static PreferencesFrame preferencesFrame;

    private Core() {
    }

    public static Core getInstance() {
        return coreInstance;
    }

    public static void launch() {
        inGame = false;
        preferences = new Preferences();
        launchFrame = new LaunchFrame();
    }

    public static void startGame() {
        inGame = true;
        gameModel = new GameModel();
    }
    //only for the loadgame button at the launch
    public static void loadGame() {
        inGame = true;
        preferences = new Preferences();
        gameModel = new GameModel();
        gameModel.loadGame();
    }
    //only for newgame button at the gameframe.
    public static void newGame() {
        inGame = true;
        preferences = new Preferences();
        gameModel = new GameModel();
    }

    public static String getLocalIPAddress() {
        try {
            return InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            return "127.0.0.1";
        }
    }

    public static Preferences getPreferences() {
        return preferences;
    }

    public static LaunchFrame getLaunchFrame() {
        return launchFrame;
    }

    public static PreferencesFrame getPreferencesFrame() {
        return preferencesFrame;
    }

    public static boolean isInGame() {
        return inGame;
    }

    /*public static void setGameModel(){
        new SaveingAndLoading().load(gameModel);
    }*/
}
