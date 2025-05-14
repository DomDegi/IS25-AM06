package it.polimi.ingsw.galaxytruckerproject.model.persistence;

import it.polimi.ingsw.galaxytruckerproject.controller.GameController;
import it.polimi.ingsw.galaxytruckerproject.model.GameInterface;
import it.polimi.ingsw.galaxytruckerproject.model.player.Player;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class GameSaver {


    private static final String userHome =  System.getProperty("user.home");

    private static final String BASE_SAVE_PATH = userHome + "/GalaxyTrucker/savedGames";


    private GameSaver() {}

    private static String dateGame(String gameName) {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter dtf  = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");
        String timeStamp = now.format(dtf);

        return gameName + "_" + timeStamp + "_" + ".txt";
    }

    public static void save(GameController gameController) {

        File fDirectory = new File(BASE_SAVE_PATH);

        if (!fDirectory.exists()) {
            fDirectory.mkdirs();
        }

        String fileName = dateGame(gameController.getGameName());
        File file = new File(fDirectory, fileName);

        deleteExistingSaveAndCreateNewFile(fDirectory,file);

        ControllerSerializerDeserializer.save(gameController, file);

        GameInterface game = gameController.getGame();
        GameSerializerDeserializer.save(game,file);

        for (Player player: game.getListOfAllPlayer()) {
            PlayerSerializerDeserializer.save(player,file);
        }
    }

    private static void deleteExistingSaveAndCreateNewFile(File fDirectory, File newSave) {
        String[] newFileName = newSave.getName().split("_");
        for (File currentFile : Objects.requireNonNull(fDirectory.listFiles())) {
            String[] currentFileName = currentFile.getName().split("_");
            if (currentFileName[0].equals(newFileName[0]) && currentFileName.length == newFileName.length) {
                if (currentFile.delete()) {
                    try {
                        if (newSave.createNewFile()) {
                            System.out.println("Successfully created new save file " + newSave.getName() + " at " + newSave.getAbsolutePath());
                        }
                        else {
                            System.out.println("Old save still exists: find out why");
                        }
                    } catch (IOException e) {
                        System.out.println("Wasn't able to create new save " + newSave.getName());
                        throw new RuntimeException(e);
                    }
                } else {
                    System.out.println("Can't delete old save  " + currentFile.getName());
                }
            }
        }
    }

}
