package it.polimi.ingsw.galaxytruckerproject.model.persistence;


import it.polimi.ingsw.galaxytruckerproject.controller.GameController;
import it.polimi.ingsw.galaxytruckerproject.model.Game;
import it.polimi.ingsw.galaxytruckerproject.model.GameInterface;

import java.io.*;

/**
 * Utility class for serializing and deserializing the {@link Game} object and related game state data.
 * Provides methods for saving to and loading from files or writers, supporting both full and lightweight (update) saves.
 *
 * <p>This class is not instantiable.</p>
 */

public class GameSerializerDeserializer {

    /**
     * Private constructor to prevent instantiation of this utility class.
     */
    private GameSerializerDeserializer() {
    }

    /**
     * Serializes the full game state to the given writer.
     * Includes game mode, player count, game state, card IDs, tile stack, and turned tile map.
     *
     * @param game the game instance to serialize
     * @param writer the destination writer
     * @throws RuntimeException if an I/O error occurs
     */
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

    /**
     * Serializes a lightweight version of the game state for update purposes.
     * Excludes tile stack data to reduce size.
     *
     * @param game the game instance to serialize
     * @param writer the destination writer
     * @throws RuntimeException if an I/O error occurs
     */
    public static void saveForUpdates(GameInterface game, Writer writer) {
        try (BufferedWriter bufferedWriter = new BufferedWriter(writer)) {
            String gameData = game.toStringGameData(); //Saves gameMode playerCount gameSate
            String cardData = game.cardsToDrawData(); //Saves cards left to draw ID
            String turnedTileData = game.turnedTileData(); //Saves tiles left in the stack map keys

            bufferedWriter.write(gameData);
            bufferedWriter.newLine();
            bufferedWriter.write(cardData);
            bufferedWriter.newLine();
            bufferedWriter.write(turnedTileData);
            bufferedWriter.newLine();
        } catch (IOException e) {
            System.out.println("Error writing game data");
            throw new RuntimeException(e);
        }
    }

    /**
     * Appends the serialized full game state to the given file.
     *
     * @param game the game instance to serialize
     * @param file the file where the game data will be saved
     * @throws RuntimeException if an I/O error occurs
     */
    public static void save(GameInterface game, File file) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, true))) {
            save(game, writer);
        } catch (IOException e) {
            System.out.println("Error writing game to file");
            throw new RuntimeException(e);
        }
    }

    /**
     * Loads and reconstructs the game state from the given reader and sets it on the provided {@link GameController}.
     * Reads four lines representing:
     *  - game metadata (mode, player count, state),
     *  - in-game cards,
     *  - tile stack keys,
     *  - turned tile keys.
     *
     * @param gameController the controller to assign the reconstructed game to
     * @param reader the source reader containing serialized game data
     * @throws RuntimeException if an I/O error occurs
     */
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
