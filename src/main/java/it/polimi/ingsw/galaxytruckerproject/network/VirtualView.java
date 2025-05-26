package it.polimi.ingsw.galaxytruckerproject.network;

import it.polimi.ingsw.galaxytruckerproject.model.GameMode;
import it.polimi.ingsw.galaxytruckerproject.model.cards.Card;
import it.polimi.ingsw.galaxytruckerproject.model.player.PlayersColor;
import it.polimi.ingsw.galaxytruckerproject.model.tiles.Coordinates;
import it.polimi.ingsw.galaxytruckerproject.model.tiles.Tile;
import it.polimi.ingsw.galaxytruckerproject.view.ViewInterface;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Map;
/**
 * Virtual view interface for handling the communication between the game logic and the view layer
 * over the network. It extends {@link ViewInterface} and serializes the data for remote communication.
 */
public interface VirtualView extends ViewInterface, Serializable {

    /**
     * Notifies the clients about a newly turned tile.
     *
     * @param tile The new tile that has been turned.
     * @throws RemoteException If a remote exception occurs.
     */
    void notifyNewTurnedTile(Tile tile) throws RemoteException;

    /**
     * Notifies the clients about the removal of a turned tile.
     *
     * @param tile The tile to be removed from the map.
     * @throws RemoteException If a remote exception occurs.
     */
    void notifyRemoveTurnedTile(Tile tile) throws RemoteException;

    /**
     * Notifies the clients about the movement of a player, including the player's position and ranking.
     *
     * @param playerName The name of the player.
     * @param color The color representing the player.
     * @param playerPosition The new position of the player.
     * @param playerRanking The player's ranking after the move.
     * @throws RemoteException If a remote exception occurs.
     */
    void notifyPlayerMovement(String playerName, PlayersColor color, int playerPosition, int playerRanking) throws RemoteException;

    /**
     * Notifies the clients that a tile has been placed by a player.
     *
     * @param playerName The name of the player who placed the tile.
     * @param tile The tile that was placed.
     * @throws RemoteException If a remote exception occurs.
     */
    void notifyPositionedTile(String playerName, Tile tile) throws RemoteException;

    /**
     * Notifies the clients that a tile has been booked by a player.
     *
     * @param playerName The name of the player who booked the tile.
     * @param tile The tile that was booked.
     * @throws RemoteException If a remote exception occurs.
     */
    void notifyBookedTile(String playerName, Tile tile) throws RemoteException;

    /**
     * Notifies the clients that a booked tile has been removed.
     *
     * @param playerName The name of the player who removed the booked tile.
     * @param tile The tile that was removed from the booking.
     * @throws RemoteException If a remote exception occurs.
     */
    void notifyRemovedBookedTile(String playerName, Tile tile) throws RemoteException;

    /**
     * Notifies the clients which card decks are not available for drawing.
     *
     * @param lockedSmallDecks A list of locked card deck IDs.
     * @throws RemoteException If a remote exception occurs.
     */
    void notifyNotAvailableCardDeck(ArrayList<Integer> lockedSmallDecks) throws RemoteException;

    /**
     * Notifies the clients about the unavailable player colors.
     *
     * @param color A list of player colors that are unavailable.
     * @throws RemoteException If a remote exception occurs.
     */
    void notifyNotAvailableColor(ArrayList<PlayersColor> color) throws RemoteException;

    /**
     * Notifies the clients about modified tiles placed by a player.
     *
     * @param playerName The name of the player who modified the tiles.
     * @param tiles A list of tiles that were modified.
     * @throws RemoteException If a remote exception occurs.
     */
    void notifyModifiedTiles(String playerName, ArrayList<Tile> tiles) throws RemoteException;

    /**
     * Notifies the clients about the credits gained by a player.
     *
     * @param playerName The name of the player.
     * @param totalCredits The total credits the player has gained.
     * @throws RemoteException If a remote exception occurs.
     */
    void notifyGainedCredits(String playerName, int totalCredits) throws RemoteException;

    /**
     * Notifies the clients that a tile was broken.
     *
     * @param playerName The name of the player who broke the tile.
     * @param coordinates A list of coordinates for the broken tile.
     * @throws RemoteException If a remote exception occurs.
     */
    void notifyBrokenTile(String playerName, ArrayList<Coordinates> coordinates) throws RemoteException;

    /**
     * Notifies the clients about the changes that occurred while the client was gone.
     *
     * @param currentGameStatus The current status of the game.
     * @throws RemoteException If a remote exception occurs.
     */
    void notifyChangesWhileGone(String currentGameStatus) throws RemoteException;

    /**
     * Notifies the clients about the current flight board cards.
     *
     * @param cards A map of deck IDs to lists of cards currently available.
     * @throws RemoteException If a remote exception occurs.
     */
    void notifyFlightBoardCards(Map<Integer, ArrayList<Card>> cards) throws RemoteException;

    /**
     * Initializes the ship boards based on the current game mode.
     *
     * @param gameMode The game mode to initialize the boards for.
     * @throws RemoteException If a remote exception occurs.
     */
    void initializeShipBoards(GameMode gameMode) throws RemoteException;

    /**
     * Notifies the clients that a player has been penalized.
     *
     * @param playerName The name of the penalized player.
     * @throws RemoteException If a remote exception occurs.
     */
    void victimOfThePenalty(String playerName) throws RemoteException;

    /**
     * Pings the server to check connectivity.
     *
     * @throws RemoteException If a remote exception occurs.
     */
    void ping() throws RemoteException;

    /**
     * Notifies the clients when a player joins the game.
     *
     * @param expected The expected number of players.
     * @param current The current number of players.
     * @param reconnected Indicates if the player is reconnecting.
     * @throws RemoteException If a remote exception occurs.
     */
    void notifyPlayerJoined(int expected, int current, boolean reconnected) throws RemoteException;
}
