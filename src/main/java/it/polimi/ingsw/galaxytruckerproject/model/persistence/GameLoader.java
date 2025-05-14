package it.polimi.ingsw.galaxytruckerproject.model.persistence;

import it.polimi.ingsw.galaxytruckerproject.controller.GameController;
import it.polimi.ingsw.galaxytruckerproject.model.GameState;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.Optional;

public class GameLoader {

    private static final String userHome =  System.getProperty("user.home");
    private static final String BASE_SAVE_PATH = userHome + "/GalaxyTrucker/savedGames";

    private GameLoader() {}

    public static GameController loadGame(File file) {

        int currentLine =  0;
        String gameName = file.getName().split("_")[0];
        GameController restartedGame = new GameController(gameName);

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {

            ControllerSerializerDeserializer.load(restartedGame,reader);
            GameSerializerDeserializer.load(restartedGame, reader);

            for (int i = 0; i < restartedGame.getGame().getPlayerCount() ; i++) {
                PlayerSerializerDeserializer.load(restartedGame, reader);
            }

            if (restartedGame.getGameState() != GameState.START_GAME
                    && restartedGame.getGameState() != GameState.SHIPS_CREATION)
                restartedGame.getGame().getFlightBoard().loadFlightBoard(restartedGame.getAllPlayers());

            return restartedGame;

        } catch (IOException e) {
            System.out.println("Error in loading game save " + file.getName());
            throw new RuntimeException(e);
        }
    }

    public static Optional<GameController> findSavedGame(String gameName) {
        File fileDir =  new File(BASE_SAVE_PATH);
        if (!fileDir.exists() || fileDir.listFiles() == null) {
            System.out.println("No saved games found in: " + BASE_SAVE_PATH);
            return Optional.empty();
        }

        for (File file : Objects.requireNonNull(fileDir.listFiles())) {
            String[] splitName =  file.getName().split("_");
            if (splitName.length > 1) {
                checkDataAndDelete(file);
                continue;
            }
            if (splitName[0].equals(gameName)) {
                return Optional.of(loadGame(file));
            }
        }
        return Optional.empty();
    }

    private static void checkDataAndDelete(File file) {
        String[] splitName =  file.getName().split("_");

        if (splitName.length < 3) {
            System.out.println("Invalid file name format: " + file.getName());
            return;
        }

        try {
            // Extracts date and hour
            String datePart = splitName[1];
            String timePart = splitName[2].replace(".txt", "");

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");
            LocalDateTime fileDateTime = LocalDateTime.parse(datePart + "_" + timePart, formatter);

            LocalDateTime now = LocalDateTime.now();

            // Checks if the file is too old
            boolean isBeforeToday = fileDateTime.toLocalDate().isBefore(now.toLocalDate());
            boolean isOlderThan2Hours = fileDateTime.isBefore(now.minusHours(2));

            // Deletes if one of the check is true
            if (isBeforeToday || isOlderThan2Hours) {
                if (file.delete()) {
                    System.out.println("Deleted old file: " + file.getName());
                } else {
                    System.out.println("Failed to delete file: " + file.getName());
                }
            }

        } catch (Exception e) {
            System.out.println("Error parsing file date: " + file.getName() + " -> " + e.getMessage());
        }
    }
}
