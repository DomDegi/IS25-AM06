package it.polimi.ingsw.galaxytruckerproject.model.persistence;

import it.polimi.ingsw.galaxytruckerproject.controller.GameController;
import it.polimi.ingsw.galaxytruckerproject.model.GameState;
import it.polimi.ingsw.galaxytruckerproject.model.player.Player;

import java.io.*;

/**
 * Utility class responsible for serializing and deserializing {@link Player} objects.
 * Includes support for saving to a file or writer, and reconstructing players from saved data.
 * <p>This class is not instantiable.</p>
 */
public class PlayerSerializerDeserializer {

    /**
     * Private constructor to prevent instantiation of this utility class.
     */
    private PlayerSerializerDeserializer() {}

    /**
     * Serializes the given player and their ship board state into the provided writer.
     *
     * @param player the player to serialize
     * @param writer the destination writer
     */
    public static void save(Player player, Writer writer)  {
        try {
            BufferedWriter bufferedWriter = new BufferedWriter(writer);
            bufferedWriter.write(player.toStringData());
            bufferedWriter.newLine();
            ShipBoardSerializerDeserializer.save(player.getShipBoard(), bufferedWriter);
        }  catch (IOException e) {
            System.out.println("Was not able to serialize player");
        }
    }

    /**
     * Serializes the given player and their ship board state to the specified file, appending the data.
     *
     * @param player the player to serialize
     * @param file   the file to append the player data to
     */
    public static void save(Player player,File file) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, true))) {
            String line = player.toStringData();
            writer.write(line);
            writer.newLine();
            //saving player shipBoard right after player info (total should be 36 lines)
            ShipBoardSerializerDeserializer.save(player.getShipBoard(),writer);

        } catch (IOException e) {
            System.out.println("was not able to serialize player");
        }
    }

    /**
     * Wrapper around {@link #save(Player, Writer)} to save a player to a file using FileWriter.
     *
     * @param player the player to serialize
     * @param file   the file to append the player data to
     */
    public static void saveToFile(Player player, File file) {
        try (FileWriter fw = new FileWriter(file, true)) {
            save(player, fw);  // reuses the save() logic
        } catch (IOException e) {
            System.out.println("was not able to serialize player");
        }
    }
    /**
     * Deserializes a player and their ship board from the provided reader and adds them to
     * the appropriate map in the {@link GameController} (active or disconnected players),
     * based on the player's connection status.
     *
     * @param gameController the controller to update with the deserialized player
     * @param reader         the reader from which to read the serialized player data
     */
    public static void load(GameController gameController, BufferedReader reader){
        int currentLine = 0;

        try {
            //now we should be reading playerLine
            String playerLine = reader.readLine();


            // Rebuilds player from txt file through player loader method
            Player player = new Player();
            String[] playerData = playerLine.split(" ");
            player.playerLoader(playerData);

            boolean verify = !(gameController.getGameState().equals(GameState.SHIPS_CREATION) || !gameController.getGameState().equals(GameState.START_GAME));


            // Loads ShipBoard
            ShipBoardSerializerDeserializer.load(player, reader,verify);

            if (player.isDisconnected()) {
                gameController.getDisconnectedPlayers().put(player.getPlayerName(), player);
            } else {
                gameController.getActivePlayers().put(player.getPlayerName(), player);
            }

        } catch (IOException e) {
            System.out.println("was not able to deserialize player");
        }
    }
}

