package it.polimi.ingsw.galaxytruckerproject.model;

import it.polimi.ingsw.galaxytruckerproject.controller.GameController;

import java.io.Serializable;

/**
 * Represents information about a specific game session.
 * This class is used to encapsulate data about an active or restarted game,
 * including its name, mode, and player counts.
 */
public class GameInfo implements Serializable {

    /** The name of the game. */
    private final String gameName;

    /** The mode in which the game is being played (e.g., TRIAL, LEVEL2). */
    private final GameMode gameMode;

    /** The maximum number of players allowed in the game. */
    private final int maxPlayerCount;

    /** The current number of connected players in the game. */
    private final int currentPlayerCount;

    /** Flag indicating whether the game was restarted after a disconnection or save. */
    private boolean restarted = false;

    /**
     * Constructs a GameInfo object from a GameController instance.
     *
     * @param gameController the controller from which game data is extracted
     */
    public GameInfo(GameController gameController) {
        this.gameName = gameController.getGameName();
        this.gameMode = gameController.getGame().getMode();
        this.maxPlayerCount = gameController.getGame().getPlayerCount();
        this.currentPlayerCount = gameController.getPlayersViewMap().size();
    }

    /**
     * Constructs a {@link GameInfo} object with the specified game name, mode,
     * maximum number of players, and current number of players.
     *
     * @param gameName the name of the game
     * @param gameMode the mode in which the game is played (e.g., TRIAL, LEVEL2)
     * @param maxPlayerCount the maximum number of players allowed in the game
     * @param currentPlayerCount the number of players currently in the game
     */
    public GameInfo(String gameName, GameMode gameMode, int maxPlayerCount, int currentPlayerCount) {
        this.gameName = gameName;
        this.gameMode = gameMode;
        this.maxPlayerCount = maxPlayerCount;
        this.currentPlayerCount = currentPlayerCount;
    }

    /**
     * Returns the name of the game.
     *
     * @return the game's name
     */
    public String getGameName() {
        return gameName;
    }

    /**
     * Returns the mode of the game.
     *
     * @return the game mode
     */
    public GameMode getGameMode() {
        return gameMode;
    }

    /**
     * Returns the maximum number of players allowed in the game.
     *
     * @return the max player count
     */
    public int getMaxPlayerCount() {
        return maxPlayerCount;
    }

    /**
     * Returns the number of players currently connected to the game.
     *
     * @return the current player count
     */
    public int getCurrentPlayerCount() {
        return currentPlayerCount;
    }

    /**
     * Returns a string representation of the game info,
     * including whether the game is being restarted.
     *
     * @return a formatted string with game name, mode, and player count
     */
    @Override
    public String toString(){
        if (!restarted) {
            return gameName + " GameMode: " + gameMode + " " + currentPlayerCount + "/" + maxPlayerCount;
        } else {
            return gameName + " GameMode: " + gameMode + " " + currentPlayerCount + "/" + maxPlayerCount + " re-starting game";
        }
    }

    /**
     * Flags this game info as related to a restarted game session.
     */
    public void setRestarted() {
        this.restarted = true;
    }

}
