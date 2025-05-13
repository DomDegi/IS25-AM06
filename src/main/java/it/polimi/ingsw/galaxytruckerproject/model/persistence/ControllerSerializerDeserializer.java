package it.polimi.ingsw.galaxytruckerproject.model.persistence;

import it.polimi.ingsw.galaxytruckerproject.controller.GameController;

import java.io.*;

public class ControllerSerializerDeserializer {

    String filename;

    public ControllerSerializerDeserializer(String filename) {
        this.filename = filename;
    }

    public void save(GameController gameController) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename,true))) {

            String controllerData = gameController.toStringData();
            writer.write(controllerData);
            writer.newLine();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public int load(GameController gameController) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            int currentLine = 0;

            String line = reader.readLine();
            currentLine++;

            String[] controllerData = line.split(" ");
            gameController.dataLoader(controllerData);

            return currentLine;

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
