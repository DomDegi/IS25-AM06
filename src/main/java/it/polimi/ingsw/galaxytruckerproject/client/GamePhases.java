package it.polimi.ingsw.galaxytruckerproject.client;

/**
 * Enum representing the different phases of the game.
 * <p>
 * The phases control the flow of the game and determine the current action or state
 * within the game. This enum is used to track which phase the game is in at any given
 * time, such as login, shipboard management, and card management.
 * </p>
 */
public enum GamePhases {
    /**
     * The phase where the player is logging in to the game.
     */
    LOGIN,

    /**
     * The phase where the player manages their shipboard, such as placing tiles and
     * organizing the ship.
     */
    SHIPBOARD,

    /**
     * The phase where the player is interacting with and managing cards.
     */
    CARDS
}
