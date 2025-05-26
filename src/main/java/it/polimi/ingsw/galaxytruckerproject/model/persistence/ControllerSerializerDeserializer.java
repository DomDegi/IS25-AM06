package it.polimi.ingsw.galaxytruckerproject.model.persistence;

import it.polimi.ingsw.galaxytruckerproject.controller.GameController;

import java.io.*;
/**
 * Utility class for serializing and deserializing the {@link GameController} state.
 * This class handles both full game state saves and lightweight updates (e.g., hourglass turns).
 * <p>
 * This class is not instantiable.
 */
public class ControllerSerializerDeserializer {
    /**
     * Utility class for serializing and deserializing the {@link GameController} state.
     * This class handles both full game state saves and lightweight updates (e.g., hourglass turns).
     * <p>
     * This class is not instantiable.
     */
    private ControllerSerializerDeserializer() {
    }
    /**
     * Serializes the entire state of the {@link GameController} into a string using
     * {@code GameController.toStringData()}, and writes it to the provided writer.
     *
     * @param gameController the game controller to serialize
     * @param writer         the destination writer
     * @throws RuntimeException if an I/O error occurs
     */
    public static void save(GameController gameController, Writer writer) {
        try (BufferedWriter bufferedWriter = new BufferedWriter(writer)) {
            String controllerData = gameController.toStringData();
            bufferedWriter.write(controllerData);
            bufferedWriter.newLine();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    /**
     * Serializes a lightweight version of the {@link GameController} state intended for client updates.
     * Currently, this includes only the hourglass turn counter.
     *
     * @param gameController the game controller to serialize
     * @param writer         the destination writer
     * @throws RuntimeException if an I/O error occurs
     */
    public static void saveForUpdates (GameController gameController, Writer writer) {
        try (BufferedWriter bufferedWriter = new BufferedWriter(writer)) {
            String controllerData = String.valueOf(gameController.getHourglassTurns());
            bufferedWriter.write(controllerData);
            bufferedWriter.newLine();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    /**
     * Serializes the {@link GameController} state and appends it to the specified file.
     *
     * @param gameController the game controller to serialize
     * @param file           the file to which the data will be written (appended)
     * @throws RuntimeException if an I/O error occurs
     */
    public static void save(GameController gameController, File file) {
        try {
            FileWriter fileWriter = new FileWriter(file, true); // append = true
            save(gameController, fileWriter);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    /**
     * Reads a serialized {@link GameController} state from a buffered reader and loads
     * the data into the controller using {@code dataLoader()}.
     *
     * @param gameController the controller to load data into
     * @param reader         the source reader containing the saved data
     * @throws RuntimeException if an I/O error occurs
     */
    public static void load(GameController gameController, BufferedReader reader) {
        try {

            String line = reader.readLine();

            String[] controllerData = line.split(" ");
            gameController.dataLoader(controllerData);


        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}