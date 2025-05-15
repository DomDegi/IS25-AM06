package it.polimi.ingsw.galaxytruckerproject.model.persistence;


import it.polimi.ingsw.galaxytruckerproject.controller.GameController;
import it.polimi.ingsw.galaxytruckerproject.model.Game;
import it.polimi.ingsw.galaxytruckerproject.model.GameInterface;

import java.io.*;

public class GameSerializerDeserializer {


    private GameSerializerDeserializer() {
    }


    public static void save(GameInterface game, Writer writer) {
        try (BufferedWriter bufferedWriter = new BufferedWriter(writer)) {
            String gameData = game.toStringGameData(); //Saves gameMode playerCount gameState
            String cardData = game.cardsToDrawData(); //Saves cards left to draw ID
            String tileStackData = game.tileStackData(); //Saves tiles left in the stack keys
            String turnedTileData = game.turnedTileData(); //Saves tiles left in the stack map keys

            bufferedWriter.write(gameData);
            bufferedWriter.newLine();
            bufferedWriter.write(cardData);
            bufferedWriter.newLine();
            bufferedWriter.write(tileStackData);
            bufferedWriter.newLine();
            bufferedWriter.write(turnedTileData);
            bufferedWriter.newLine();
        } catch (IOException e) {
            System.out.println("Error writing game data");
            throw new RuntimeException(e);
        }
    }

    public static void saveForUpdates(GameInterface game, Writer writer) {
        try (BufferedWriter bufferedWriter = new BufferedWriter(writer)) {
            String gameData = game.toStringGameData(); //Saves gameMode playerCount gameSate
            String turnedTileData = game.turnedTileData(); //Saves tiles left in the stack map keys

            bufferedWriter.write(gameData);
            bufferedWriter.newLine();
            bufferedWriter.write(turnedTileData);
            bufferedWriter.newLine();
        } catch (IOException e) {
            System.out.println("Error writing game data");
            throw new RuntimeException(e);
        }
    }

    public static void save(GameInterface game, File file) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, true))) {
            save(game, writer);
        } catch (IOException e) {
            System.out.println("Error writing game to file");
            throw new RuntimeException(e);
        }
    }

    public static void load(GameController gameController, BufferedReader reader) {
        try {

            // Reads game line (mode, playerCount)
            String gameLine = reader.readLine();
            // Reads card line (cards left in ArrayList<Card> inGameCards)
            String cardLine = reader.readLine();
            // Reads tileStack keys line
            String tileStackLine = reader.readLine();
            // Reads turnedTiles keys line
            String turnedTileLine = reader.readLine();

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

        } catch (IOException e) {
            System.out.println("Error reading game from file");
            throw new RuntimeException(e);
        }
    }
}
