package it.polimi.ingsw.galaxytruckerproject.model.persistence;


import it.polimi.ingsw.galaxytruckerproject.controller.GameController;
import it.polimi.ingsw.galaxytruckerproject.model.Game;

import java.io.*;

public class GameSerializerDeserializer {

    String filename;

    public GameSerializerDeserializer(String filename) {
        this.filename = filename;
    }

    public void save(Game game) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename, true))) {
            //Saves gameMode playerCount cardsLeft to draw
            String gameData = game.toStringData();
            writer.write(gameData);
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

            // Reads player line
            String gameLine = reader.readLine();
            currentLine++;

            if (gameLine == null) {
                throw new IllegalStateException("startLine is over the end of file");
            }

            String[] gameData = gameLine.split(" ");
            Game game = new Game();
            game.gameLoader(gameData);
            gameController.setGameInterface(game);

            return currentLine;

        } catch (IOException e) {
            System.out.println("Error reading game from file");
            throw new RuntimeException(e);
        }
    }
}
