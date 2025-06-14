package it.polimi.ingsw.galaxytruckerproject.model.persistence;

import it.polimi.ingsw.galaxytruckerproject.controller.GameController;
import it.polimi.ingsw.galaxytruckerproject.model.GameInfo;
import it.polimi.ingsw.galaxytruckerproject.model.GameMode;
import it.polimi.ingsw.galaxytruckerproject.model.GameState;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
/**
 * Utility class responsible for loading saved game files from disk.
 * It can deserialize a full {@link GameController} object from a file and also
 * clean up outdated save files.
 *
 * The class cannot be instantiated.
 */
public class GameLoader {

    /** The user's home directory path, used as the base for saving/loading. */
    private static final String userHome = System.getProperty("user.home");

    /** Base directory for saved game files. */
    private static final String BASE_SAVE_PATH = userHome + "/GalaxyTrucker/savedGames";

    /**
     * Private constructor to prevent instantiation of this utility class.
     */
    private GameLoader() {}
    /**
     * Loads a complete game from the given file and reconstructs the {@link GameController} object.
     * It also re-initializes the flight board and associated player data.
     *
     * @param file the file containing the saved game data
     * @return a reconstructed GameController instance
     * @throws RuntimeException if an I/O error occurs during loading
     */
    public static GameController loadGame(File file) {

        String[] nameData = file.getName().split("_");
        String gameName;
        if (nameData.length > 1) {
            gameName = nameData[0];
        }
        else {
            nameData = file.getName().split("\\.");
            gameName = nameData[0];
        }
        GameController restartedGame = new GameController(gameName);

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {

            ControllerSerializerDeserializer.load(restartedGame,reader);
            GameSerializerDeserializer.load(restartedGame, reader);

            for (int i = 0; i < restartedGame.getGame().getPlayerCount() ; i++) {
                PlayerSerializerDeserializer.load(restartedGame, reader);
            }

            restartedGame.getGame().getFlightBoard().loadFlightBoard(restartedGame.getAllPlayers());

            return restartedGame;

        } catch (IOException e) {
            System.out.println("Error in loading game save " + file.getName());
            throw new RuntimeException(e);
        }
    }
    /**
     * Searches for a saved game file in the default save directory that matches the provided game name.
     * Also checks and deletes outdated files during the search.
     *
     * @param gameName the name of the game to load
     * @return an Optional containing the loaded GameController if found, otherwise empty
     */
    public static Optional<GameController> findSavedGame(String gameName) {
        File fileDir =  new File(BASE_SAVE_PATH);
        if (!fileDir.exists() || fileDir.listFiles() == null) {
            System.out.println("No saved games found in: " + BASE_SAVE_PATH);
            return Optional.empty();
        }

        for (File file : Objects.requireNonNull(fileDir.listFiles())) {
            String[] splitName =  file.getName().split("_");
            if (splitName.length > 1) {
                if (checkDataAndDelete(file))
                    continue;
            }
            else {
                splitName =  splitName[0].split("\\.");
                if (splitName.length < 2) {
                    System.out.println("Recheck how game loading looks up gameName");
                }
            }
            if (splitName[0].equals(gameName)) {
                return Optional.of(loadGame(file));
            }
        }
        return Optional.empty();
    }

    /**
     * this method checks if a save file is older than today's date or, if it was made today, if it's older than 2 hours
     * if one of those conditions is true, deletes the file
     * @param file the file that gets checked
     * @return true if the file gets deleted
     */
    private static boolean checkDataAndDelete(File file) {
        String[] splitName =  file.getName().split("_");

        if (splitName.length < 3) {
            System.out.println("Invalid file name format: " + file.getName());
            return false;
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
                    return true;
                } else {
                    System.out.println("Failed to delete file: " + file.getName());
                }
            }
        } catch (Exception e) {
            System.out.println("Error parsing file date: " + file.getName() + " -> " + e.getMessage());
        }
        return false;
    }

    /**
     * Retrieves a list of {@link GameInfo} objects representing saved games
     * in which the specified player is present.
     * <p>
     * This method scans the save directory defined by {@code BASE_SAVE_PATH},
     * checks each valid game file to see if it contains the given player's name,
     * and constructs a {@link GameInfo} object for each matching file.
     * Invalid or outdated save files may be deleted during the process via {@code checkDataAndDelete(File)}.
     * </p>
     *
     * @param playerName the name of the player to search for in saved games
     * @return a list of {@code GameInfo} objects corresponding to games the player is part of
     */
    public static ArrayList<GameInfo> savedGamesWithPlayersName(String playerName) {
        File fileDir = new File(BASE_SAVE_PATH);
        ArrayList<GameInfo> games = new ArrayList<>();

        if (!fileDir.exists() || fileDir.listFiles() == null) {
            System.out.println("No saved games found in: " + BASE_SAVE_PATH);
            return games;
        }

        for (File file : Objects.requireNonNull(fileDir.listFiles())) {
            String[] gameName = file.getName().split("_");
            if (gameName.length > 1) {
                if (checkDataAndDelete(file))
                    continue;
            }
            if (playerIsInGame(file, playerName)) {
                games.add(gameInfoFromFile(file));
            }
        }
        return games;
    }


    /**
     * Checks whether a specific player is listed in the game file.
     * The method only inspects lines at specific positions: 6, 44, 82, and 120 (1-based index)
     * where the players' info are saved.
     *
     * @param file the file containing game data
     * @param playerName the name of the player to search for
     * @return true if the player is found in any of the specified lines; false otherwise
     * @throws RuntimeException if an I/O error occurs while reading the file
     */
    public static boolean playerIsInGame(File file, String playerName) {
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String currentLine;
            int index = 0;
            while ((currentLine = reader.readLine()) != null) {
                index++;
                if (index == 6 || index == 44 || index == 82 || index == 120) {
                    String[] splitLine = currentLine.split(" ");
                    if (splitLine.length > 0 && splitLine[0].equals(playerName)) {
                        return true;
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Error reading file: " + file.getName(), e);
        }
        return false;
    }

    /**
     * Reconstructs a {@link GameInfo} object from a saved game file.
     * <p>
     * This method reads the first two lines of the file to extract the game's name,
     * mode, and maximum player count. The current player count is not inferred and is
     * set to 0 because the game isn't loaded. The returned {@code GameInfo} object is marked as restarted.
     * </p>
     *
     * @param file the file containing the saved game data
     * @return a {@code GameInfo} object built from the file contents
     * @throws RuntimeException if an I/O error occurs while reading the file
     */
    public static GameInfo gameInfoFromFile(File file) {
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String controllerLine = reader.readLine();
            String gameName = file.getName().split("_")[0];

            String gameLine = reader.readLine();
            GameMode gameMode = GameMode.valueOf(gameLine.split(" ")[0]);
            int maxPlayerCount = Integer.parseInt(gameLine.split(" ")[1]);

            GameInfo info = new GameInfo(gameName,gameMode,maxPlayerCount,0);
            info.setRestarted();
            return info;
        }
        catch (IOException e) {
            throw new RuntimeException("Error reading file: " + file.getName(), e);
        }
    }


}
