package it.polimi.ingsw.galaxytruckerproject.model.persistence;


import it.polimi.ingsw.galaxytruckerproject.controller.GameController;
import it.polimi.ingsw.galaxytruckerproject.model.Game;
import it.polimi.ingsw.galaxytruckerproject.model.GameInterface;

import java.io.*;

public class GameSerializerDeserializer {

    String filename;

    public GameSerializerDeserializer(String filename) {
        this.filename = filename;
    }

    public void save(GameInterface game) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename, true))) {
            String gameData = game.toStringGameData(); //Saves gameMode playerCount
            String cardData = game.cardsToDrawData(); //Saves cards left to draw ID
            String tileStackData = game.tileStackData(); //Saves tiles left in the stack keys
            String turnedTileData = game.turnedTileData(); //Saves tiles left in the stack map keys

            //We'll save the 3 strings on 3 different lines
            writer.write(gameData);
            writer.newLine();
            writer.write(cardData);
            writer.newLine();
            writer.write(tileStackData);
            writer.newLine();
            writer.write(turnedTileData);
            writer.newLine();

        } catch (IOException e) {
            System.out.println("Error writing game to file");
            throw new RuntimeException(e);
        }
    }

    public int load(GameController gameController, int startLine) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {

            int currentLine = 0;

            while (currentLine < startLine && reader.readLine() != null) {
                currentLine++;
            }

            // Reads game line (mode, playerCount)
            String gameLine = reader.readLine();
            currentLine++;
            // Reads card line (cards left in ArrayList<Card> inGameCards)
            String cardLine = reader.readLine();
            currentLine++;
            // Reads tileStack keys line
            String tileStackLine = reader.readLine();
            currentLine++;
            // Reads turnedTiles keys line
            String turnedTileLine = reader.readLine();
            currentLine++;

            if (gameLine == null) {
                throw new IllegalStateException("startLine is over the end of file");
            }

            String[] gameData = gameLine.split(" ");
            String[] cardData = cardLine.split(" ");
            String[]  tileStackData = tileStackLine.split(" ");
            String[]  turnedTileData = turnedTileLine.split(" ");

            Game game = new Game();
            game.gameLoader(gameData);
            game.cardLoader(cardData);
            game.tileStackLoader(tileStackData);
            game.turnedTileLoader(turnedTileData);

            gameController.setGameInterface(game);

            return currentLine;

        } catch (IOException e) {
            System.out.println("Error reading game from file");
            throw new RuntimeException(e);
        }
    }
}
