package it.polimi.ingsw.galaxytruckerproject.model.persistence;

import it.polimi.ingsw.galaxytruckerproject.controller.GameController;

import java.io.*;

public class ControllerSerializerDeserializer {

    private ControllerSerializerDeserializer() {
    }

    public static void save(GameController gameController, Writer writer) {
        try (BufferedWriter bufferedWriter = new BufferedWriter(writer)) {
            String controllerData = gameController.toStringData();
            bufferedWriter.write(controllerData);
            bufferedWriter.newLine();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void saveForUpdates (GameController gameController, Writer writer) {
        try (BufferedWriter bufferedWriter = new BufferedWriter(writer)) {
            String controllerData = String.valueOf(gameController.getHourglassTurns());
            bufferedWriter.write(controllerData);
            bufferedWriter.newLine();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void save(GameController gameController, File file) {
        try {
            FileWriter fileWriter = new FileWriter(file, true); // append = true
            save(gameController, fileWriter);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

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
