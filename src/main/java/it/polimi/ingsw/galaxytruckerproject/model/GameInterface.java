package it.polimi.ingsw.galaxytruckerproject.model;

import it.polimi.ingsw.galaxytruckerproject.controller.interfaces.Observer;
import it.polimi.ingsw.galaxytruckerproject.model.cards.Card;
import it.polimi.ingsw.galaxytruckerproject.model.player.Player;
import it.polimi.ingsw.galaxytruckerproject.model.player.PlayersColor;
import it.polimi.ingsw.galaxytruckerproject.model.tiles.ShipBoard;
import it.polimi.ingsw.galaxytruckerproject.model.tiles.Tile;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;

/**
 * Represents the core interface for the Galaxy Trucker game logic.
 * It exposes all key game methods for managing players, ship creation,
 * tile drawing, card handling, and state transitions.
 */
public interface GameInterface extends Serializable {

    /**
     * Registers an observer for game events.
     * @param o the observer to register
     */
    void addObserver(Observer o);

    /**
     * Removes an observer from the game.
     * @param o the observer to remove
     */
    void removeObserver(Observer o);

    /**
     * Sets the number of players for the game.
     * @param playerCount total number of players
     */
    void setPlayerCount(int playerCount);

    /**
     * Adds a new player to the game.
     * @param playerName the name of the player
     * @param color the color assigned to the player
     */
    void addPlayer(String playerName, PlayersColor color);

    /**
     * Starts the ship creation phase for all players.
     */
    void startShipCreation();

    /**
     * Gets the total number of players.
     * @return number of players
     */
    int getPlayerCount();

    /**
     * Retrieves a player instance by name.
     * @param playerName the name of the player
     * @return the corresponding Player object
     */
    Player identifyPlayerByName(String playerName);

    /**
     * Gets the list of all players currently flying.
     * @return list of active in-flight players
     */
    ArrayList<Player> getListOfInFlightPlayers();

    /**
     * Sets the final rankings of the players.
     */
    void setPodium();

    /**
     * Gets the current game state.
     * @return the game state
     */
    GameState getGameState();

    /**
     * Gets the list of all players in the game.
     * @return list of all Player instances
     */
    ArrayList<Player> getListOfAllPlayer();

    /**
     * Gets the number of players.
     * @return number of players
     */
    int getNumberOfPlayers();

    /**
     * Retrieves a player's ship board by name.
     * @param s the name of the player
     * @return the corresponding ShipBoard
     */
    ShipBoard getPlayerShipBoard(String s);

    /**
     * Gets the current flight board.
     * @return the FlightBoard object
     */
    FlightBoard getFlightBoard();

    /**
     * Gets the current game mode.
     * @return the game mode
     */
    GameMode getMode();

    /**
     * Draws a tile from the stack for the specified player.
     * @param playerName the player's name
     * @return the drawn tile
     */
    Tile drawTile(String playerName);

    /**
     * Draws a specific turned tile by index.
     * @param playerName the player's name
     * @param index the index in the turned tile map
     * @return the drawn turned tile
     */
    Tile drawTurnedTile(String playerName, int index);

    /**
     * Draws and positions a booked tile for the player.
     * @param playerName the player's name
     * @param tile the tile to be placed
     * @return the placed tile
     */
    Tile drawAndPositionBookedTile(String playerName, Tile tile);

    /**
     * Player refuses the currently drawn tile.
     * @param playerName the player's name
     * @return the refused tile
     */
    Tile refuseTile(String playerName);

    /**
     * Ends the ship creation phase for all players.
     */
    void endShipCreation();

    /**
     * Ends the ship verification phase.
     */
    void endShipVerification();


    /**
     * Draws a new card from the deck.
     */
    void drawCard();

    /**
     * Ends the current card phase.
     */
    void endCardPhase();

    /**
     * Ends the current card event.
     */
    void endCardEvent();

    /**
     * Gets a number of cards currently in game.
     * @param number number of cards to retrieve
     * @return list of Card instances
     */
    ArrayList<Card> getANumberOfInGameCards(int number);

    /**
     * Gets the current turned tiles map.
     * @return map of turned tiles by ID
     */
    ConcurrentHashMap<Integer, Tile> getTurnedTiles();

    /**
     * Sets a tile for the given player on the ship board.
     * @param playerName the name of the player
     * @param tile the tile to be positioned
     * @return the placed tile
     */
    Tile playerSetTile(String playerName, Tile tile);

    /**
     * Books a tile for the given player.
     * @param playerName the player's name
     * @return the booked tile
     */
    Tile playerBookTile(String playerName);

    /**
     * Gets the number of cards left to draw.
     * @return number of cards left
     */
    int getCardsLeft();

    /**
     * Gets the card that has just been drawn.
     * @return the drawn card
     */
    Card getDrawnCard();

    /**
     * Sets the current game state.
     * @param gameState the new state
     */
    void setGameState(GameState gameState);

    /**
     * Gets the list of in-game cards.
     * @return list of all in-game cards
     */
    ArrayList<Card> getANumberOfInGameCards();

    /**
     * Gets the tile stack (available tiles to draw).
     * @return queue of tiles
     */
    ConcurrentLinkedDeque<Tile> getTileStack();

    /**
     * Returns game data in string format for serialization.
     * @return formatted string of game data
     */
    String toStringGameData();

    /**
     * Returns the current cards-to-draw list as a string.
     * @return card data string
     */
    String cardsToDrawData();

    /**
     * Returns the current tile stack as a string.
     * @return tile stack data string
     */
    String tileStackData();

    /**
     * Returns the turned tiles map as a string.
     * @return turned tile data string
     */
    String turnedTileData();

    /**
     * Sets the game state without notifying observers or performing side effects.
     * @param gameState the new state to set
     */
    void setGameStateWithoutUpdating(GameState gameState);


}
