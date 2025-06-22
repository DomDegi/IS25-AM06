 package it.polimi.ingsw.galaxytruckerproject.persistence;
import it.polimi.ingsw.galaxytruckerproject.controller.GameController;
import it.polimi.ingsw.galaxytruckerproject.model.GameInterface;
import it.polimi.ingsw.galaxytruckerproject.model.player.Player;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
 /**
  * Utility class responsible for saving the current game state to disk.
  * It serializes the game controller, game data, and all players into a timestamped file.
  * <p>
  * Ensures that only one save file per game name exists at a time by deleting previous saves.
  */
public class GameSaver {


     /** The user's home directory, used as base for save path. */
     private static final String userHome = System.getProperty("user.home");

     /** The name of the OS needed to discern between MACOS and Windows */
     private static final String OS = System.getProperty("os.name").toLowerCase();

     /** Base directory for storing saved game files. */
     private static final String BASE_SAVE_PATH;

     // This method is used to decide the BASE_SAVE_PATH depending on the OS
     static {
         if (OS.contains("win")) {
             // Windows: C:\Users\<username>\GalaxyTrucker\savedGames
             BASE_SAVE_PATH = userHome + "\\GalaxyTrucker\\savedGames";
         } else if (OS.contains("mac")) {
             // macOS: /Users/<username>/Library/Application Support/GalaxyTrucker/savedGames
             BASE_SAVE_PATH = userHome + "/Library/Application Support/GalaxyTrucker/savedGames";
         } else {
             // Default (Linux or unknown): ~/GalaxyTrucker/savedGames
             BASE_SAVE_PATH = userHome + "/GalaxyTrucker/savedGames";
         }
     }

     /**
      * Utility class responsible for saving the current game state to disk.
      * It serializes the game controller, game data, and all players into a timestamped file.
      * <p>
      * Ensures that only one save file per game name exists at a time by deleting previous saves.
      */
     private GameSaver() {}
     /**
      * Generates a timestamped file name for the game save based on the current time.
      *
      * @param gameName the name of the game to be saved
      * @return a string representing the save file name, including date and time
      */
     private static String dateGame(String gameName) {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter dtf  = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");
        String timeStamp = now.format(dtf);

        return gameName + "_" + timeStamp + ".txt";
    }

     /**
      * Saves the current game state to a file.
      * This includes the controller state, game data, and all player states.
      * If a previous save exists for the same game name, it will be deleted and replaced.
      *
      * @param gameController the controller managing the current game state
      */
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
            PlayerSerializerDeserializer.saveToFile(player,file);
        }
    }
     /**
      * Deletes any existing save file that matches the new save's base name (i.e., game name),
      * and attempts to create the new save file.
      *
      * @param fDirectory the directory containing all save files
      * @param newSave    the new file to create (after deleting old one if exists)
      * @throws RuntimeException if the new file cannot be created
      */
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