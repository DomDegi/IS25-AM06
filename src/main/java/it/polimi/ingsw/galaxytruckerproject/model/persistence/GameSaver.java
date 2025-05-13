package it.polimi.ingsw.galaxytruckerproject.model.persistence;

import it.polimi.ingsw.galaxytruckerproject.controller.GameController;
import it.polimi.ingsw.galaxytruckerproject.model.GameInterface;
import it.polimi.ingsw.galaxytruckerproject.model.player.Player;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class GameSaver {


    private GameSaver() {}

    private static String dateGame(String gameName) {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");
        String timeStamp = now.format(dtf);

        return gameName + "_" + timeStamp + ".txt";
    }

    public static void save(GameController gameController) {
        String fileName = dateGame(gameController.getGameName());
        File file = new File(fileName);
        try {
            if (file.createNewFile()) {
                System.out.println("File created: " + fileName + " at " + file.getAbsolutePath());
            } else {
                System.out.println("File already exists.");
            }
        } catch (IOException e) {
            System.out.println("Error creating file: " + fileName + " " + e.getMessage());
        }

        ControllerSerializerDeserializer controllerSaver = new ControllerSerializerDeserializer(fileName);
        controllerSaver.save(gameController);

        GameSerializerDeserializer gameSaver = new GameSerializerDeserializer(fileName);
        GameInterface game = gameController.getGame();
        gameSaver.save(game);

        PlayerSerializerDeserializer playerSaver = new PlayerSerializerDeserializer(fileName);
        for (Player player: game.getListOfAllPlayer()) {
            playerSaver.save(player);
        }

    }

}
