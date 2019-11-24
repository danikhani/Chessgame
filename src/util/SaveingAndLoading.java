package util;

import java.io.*;
import java.util.Timer;

import board.*;
import pieces.*;
import ui.*;
import util.*;


public class SaveingAndLoading {
    String filename = "file.ser";
    GameModel gamemodel = new GameModel();
    BoardPanel board = new BoardPanel(gamemodel);
    TimerPanel timer = new TimerPanel(gamemodel);
    public void save() {
        // Serialization
        try {
            //Saving of object in a file
            FileOutputStream file = new FileOutputStream(filename);
            ObjectOutputStream out = new ObjectOutputStream(file);

            // Method for serialization of object
            out.writeObject(gamemodel);
            //out.writeObject(board);
            //out.writeObject(timer);

            out.close();
            file.close();

            System.out.println("Object has been serialized");

        } catch (
                IOException ex) {
            System.out.println("IOException is caught");
        }
        //object1 = null;



    }
    public void load(){
        // Deserialization
        try
        {
            // Reading the object from a file
            FileInputStream file = new FileInputStream(filename);
            ObjectInputStream in = new ObjectInputStream(file);

            // Method for deserialization of object
            gamemodel = (GameModel)in.readObject();
            //board = (BoardPanel)in.readObject();
            //timer = (TimerPanel)in.readObject();

            in.close();
            file.close();

            System.out.println("Object has been deserialized ");
        }

        catch(IOException ex)
        {
            System.out.println("IOException is caught");
        }

        catch(ClassNotFoundException ex)
        {
            System.out.println("ClassNotFoundException is caught");
        }

    }
}
