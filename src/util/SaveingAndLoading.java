package util;

import java.io.*;
import java.util.ArrayList;
import java.util.Timer;

import board.*;
import pieces.*;
import ui.*;
import util.*;

import javax.swing.*;


public class SaveingAndLoading {
    private static ArrayList<Piece> loadedPieces = new ArrayList<Piece>();
    private static Piece.Color loadedCurrentColor;
    private static Piece.Color loadedNotCurrentColor;
    private static String blackTime;
    private static String whiteTime;
    private static String loadedBlackTime;
    private static String loadedWhiteTime;

    public static ArrayList<Piece> getLoadedPieces(){
        return loadedPieces;
    }
    public static void removeLoadedPieces(){
        loadedPieces.clear();
    }
    public static Piece.Color getLoadedCurrentColor(){
        return loadedCurrentColor;
    }
    public static Piece.Color getLoadedNotCurrentColor(){
        return loadedNotCurrentColor;
    }
    public static void setBlackTime(String time){
        blackTime = time;
    }
    public static void setWhiteTime(String time){
        whiteTime = time;
    }
    public static String getLoadedBlackTime(){
        return loadedBlackTime;
    }
    public static String getLoadedWhiteTime(){
        return loadedWhiteTime;
    }
// This will save the position of each piece and adds it to a file.

    //THis will load the position from the text file and makes a long string out of it.
    public static void loadGame(){
        loadSettings();
        loadPiecePosition();
    }
    public static void saveGame(){
        saveSetting();
        savePiecePosition();
    }

    private static void savePiecePosition() {
        try {
            File file = new File("Savings/PiecePosition.txt");
            BufferedWriter writer = new BufferedWriter(new FileWriter(file));
            for (Piece piece : PieceSet.getAvailablePieces()) {
                writer.write(piece.getType() + "/");
                writer.write(piece.getColor() + "/");
                writer.write(piece.getRank() + "/");
                writer.write(piece.getFile() + "\n");
            }
            writer.close();
        } catch (IOException ex) {
            System.out.println("couldn’t write the String out");
            //ex.printStackTrace();
        }
    }
    public static void saveSetting() {
        try {
            File file = new File("Savings/Setting.txt");
            BufferedWriter writer = new BufferedWriter(new FileWriter(file));
            writer.write(MoveValidator.currentMoveColor + "/");
            writer.write(whiteTime + "/");
            writer.write(blackTime + "/");
            writer.write(MoveValidator.notCurrentMoveColor + "/");

            writer.close();
        } catch (IOException ex) {
            System.out.println("couldn’t write the String out");
        }
    }

    public static void loadSettings() {
        try {
            File myFile = new File("Savings/Setting.txt");
            FileReader fileReader = new FileReader(myFile);
            BufferedReader reader = new BufferedReader(fileReader);
            String line = reader.readLine();
            String[] result = line.split("/");

            if (result[0].equals("WHITE")) {
                loadedCurrentColor = Piece.Color.WHITE;
            } else {
                loadedCurrentColor = Piece.Color.BLACK;
            }
            loadedWhiteTime = result[1];
            loadedBlackTime = result[2];
            if (result[3].equals("WHITE")) {
                loadedNotCurrentColor = Piece.Color.WHITE;
            } else {
                loadedNotCurrentColor = Piece.Color.BLACK;
            }
            reader.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    private static void loadPiecePosition() {
        try {
            File myFile = new File("Savings/PiecePosition.txt");
            FileReader fileReader = new FileReader(myFile);
            BufferedReader reader = new BufferedReader(fileReader);
            String line = null;
            while ((line = reader.readLine()) != null) {
                makePieceObjects(line);
            }
            reader.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    //This splits the loaded string
    private static void makePieceObjects(String lineToParse) {
        String[] result = lineToParse.split("/");
        Piece.Type type = null;
        Piece.Color color = null;
        Piece piece = null;
        int rank = Integer.parseInt(result[2]);
        char file = result[3].charAt(0);

        if (result[1].equals("WHITE")) {
            color = Piece.Color.WHITE;
        } else {
            color = Piece.Color.BLACK;
        }

        switch (result[0]) {
            case "PAWN":
                type = Piece.Type.PAWN;
                piece = new Pawn(color);
                break;
            case "ROOK":
                type = Piece.Type.ROOK;
                piece = new Rook(color);
                break;
            case "KNIGHT":
                type = Piece.Type.KNIGHT;
                piece = new Knight(color);
                break;
            case "BISHOP":
                type = Piece.Type.BISHOP;
                piece = new Bishop(color);
                break;
            case "KING":
                type = Piece.Type.KING;
                piece = new King(color);
                break;
            case "QUEEN":
                type = Piece.Type.QUEEN;
                piece = new Queen(color);
                break;
        }
        piece.setRank(rank);
        piece.setFile(file);
        loadedPieces.add(piece);
    }

}