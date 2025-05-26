package it.polimi.ingsw.galaxytruckerproject.controller.interfaces;

import it.polimi.ingsw.galaxytruckerproject.controller.GameController;
import it.polimi.ingsw.galaxytruckerproject.model.GameMode;
import it.polimi.ingsw.galaxytruckerproject.model.player.PlayersColor;
import it.polimi.ingsw.galaxytruckerproject.model.tiles.CargoHold;
import it.polimi.ingsw.galaxytruckerproject.model.tiles.Coordinates;
import it.polimi.ingsw.galaxytruckerproject.model.tiles.Tile;

import java.rmi.RemoteException;
import java.util.ArrayList;

/**
 * Interface defining the methods that a player can invoke to interact with the game server.
 * This includes login, game creation/joining, in-game actions, and state updates.
 */
public interface ControllerInterface {

    /**
     * Verifies that the chosen nickname is unique and binds the player's view to the controller.
     * If the nickname is already taken, a message is sent to the player.
     * If the nickname belongs to a disconnected player, reconnects them.
     *
     * @param nickname the player's chosen name
     * @throws Exception if the login process fails
     */
    void login(String nickname) throws Exception;

    /**
     * Creates a new game session with the specified name, player count, and game mode.
     * Notifies the player if the game name is already in use.
     *
     * @param gameName name of the new game lobby
     * @param playerCount maximum number of players
     * @param mode the selected {@link GameMode}
     * @throws Exception if creation fails or name is already taken
     */
    void createGame(String gameName, int playerCount, GameMode mode) throws Exception;

    /**
     * Attempts to join an existing game lobby by name.
     * If the lobby is full after joining, the game starts.
     * Notifies the player if the lobby doesn't exist.
     *
     * @param gameName the name of the game lobby to join
     * @throws Exception if the lobby cannot be joined
     */
    void joinGame(String gameName) throws Exception;

    /**
     * Allows the player to leave the current game during the lobby phase.
     * Updates their view to the list of available lobbies.
     *
     * @throws Exception if leaving the game fails
     */
    void leaveGame() throws Exception;

    /**
     * Allows the player to choose their color in the game.
     * If the color is already taken or invalid, the player is notified.
     *
     * @param color the chosen {@link PlayersColor}
     * @throws Exception if the color selection is invalid
     */
    void chooseColor(PlayersColor color) throws Exception;

    /**
     * Called when the player flips the hourglass to indicate time pressure.
     *
     * @throws Exception if the operation fails
     */
    void turnHourglass() throws Exception;

    /**
     * Draws a tile from the shared stack of components.
     *
     * @throws Exception if no tiles are available or operation fails
     */
    void drawTileFromStack() throws Exception;

    /**
     * Draws a tile from the available turned (face-up) tiles.
     *
     * @param index index of the turned tile to draw
     * @throws Exception if the tile cannot be drawn
     */
    void drawTileFromTurned(int index) throws Exception;

    /**
     * Refuses the currently held tile and places it back into the game area.
     *
     * @throws Exception if the refusal cannot be processed
     */
    void refuseTile() throws Exception;

    /**
     * Allows the player to view game cards during the game phase.
     *
     * @param index the index of the card to look at
     * @throws Exception if the operation fails
     */
    void lookGameCards(int index) throws Exception;

    /**
     * Ends the card-viewing session.
     *
     * @throws Exception if the operation fails
     */
    void stopLookingAtCards() throws Exception;

    /**
     * Attempts to place a tile on the player's ship.
     *
     * @param tile the tile to place
     * @throws Exception if the tile cannot be placed
     */
    void setTile(Tile tile) throws Exception;

    /**
     * Books the currently held tile for later use.
     *
     * @throws Exception if booking fails
     */
    void bookTile() throws Exception;

    /**
     * Handles tile removal after ship construction errors are found.
     *
     * @param toRemove list of coordinates to remove
     * @throws Exception if removal fails
     */
    void shipErrorManagement(ArrayList<Coordinates> toRemove) throws Exception;

    /**
     * Draws the next card in the deck during the game phase.
     *
     * @throws Exception if drawing fails
     */
    void drawCard() throws Exception;

    /**
     * Triggers an early landing request from the player.
     *
     * @throws Exception if the request cannot be processed
     */
    void earlyLanding() throws Exception;

    /**
     * Sets the game controller associated with this player.
     *
     * @param gameController the game controller to bind
     */
    void setGameController(GameController gameController);

    /**
     * Called when the player has completed ship construction.
     *
     * @throws Exception if confirmation fails
     */
    void completedShip() throws Exception;

    /**
     * Triggers the firing of cannons with the specified power and battery usage.
     *
     * @param doublePower the power of the double cannon
     * @param batteries coordinates of batteries used
     * @throws RemoteException if the command fails
     */
    void useCannons(float doublePower, ArrayList<Coordinates> batteries) throws RemoteException;

    /**
     * Triggers engine use with the specified number of double engines and battery support.
     *
     * @param numberOfDoubleEngines number of engines to use
     * @param batteries coordinates of batteries used
     * @throws RemoteException if the command fails
     */
    void useEngines(int numberOfDoubleEngines, ArrayList<Coordinates> batteries) throws RemoteException;

    /**
     * Validates the cargo selected by the player and updates credits accordingly.
     *
     * @param clientCreditsToVerify total credits submitted by the client
     * @param cargosToUpdate list of cargo holds involved
     * @throws RemoteException if the validation fails
     */
    void manageGoods(int clientCreditsToVerify, ArrayList<CargoHold> cargosToUpdate) throws RemoteException;

    /**
     * Called when the player makes a binary choice (e.g., "Yes"/"No" decision).
     *
     * @param choice true or false based on player input
     * @throws Exception if the choice cannot be handled
     */
    void makeAChoice(boolean choice) throws Exception;

    /**
     * Called when the player chooses a planet during exploration or endgame.
     *
     * @param planet index of the chosen planet
     * @throws Exception if the choice is invalid
     */
    void choosePlanet(int planet) throws Exception;

    /**
     * Selects the crew members to board the ship at the beginning of a phase.
     *
     * @param cabins list of tiles representing the cabins
     * @throws Exception if crew assignment fails
     */
    void pickCrewMembers(ArrayList<Tile> cabins) throws Exception;

    /**
     * Removes crew members from specified locations.
     *
     * @param fromHere list of tile coordinates from which crew should be removed
     * @throws Exception if removal fails
     */
    void removeCrew(ArrayList<Coordinates> fromHere) throws Exception;

    /**
     * Removes goods from specified cargo tiles.
     *
     * @param fromHere list of cargo coordinates to remove goods from
     * @throws Exception if removal fails
     */
    void removeGoods(ArrayList<Coordinates> fromHere) throws Exception;

    /**
     * Consumes batteries from specified coordinates.
     *
     * @param batteries list of battery coordinates
     * @throws Exception if usage fails
     */
    void useBatteries(ArrayList<Coordinates> batteries) throws Exception;

    /**
     * Rolls the dice, typically used in combat or asteroid events.
     *
     * @throws Exception if the roll fails
     */
    void rollTheDices() throws Exception;

    /**
     * Allows the player to choose a direction or branch (e.g., in travel or encounter).
     *
     * @param thisOne list of coordinates representing the chosen path
     * @throws Exception if selection fails
     */
    void chooseBranch(ArrayList<Coordinates> thisOne) throws Exception;

    /**
     * Assigns the player a position on the flight board in Level 2 mode.
     *
     * @param position position index (1 to max player count)
     * @throws Exception if position assignment fails
     */
    void setPosition(int position) throws Exception;

    /**
     * Keeps the server connection alive by pinging periodically.
     *
     * @throws RemoteException if the server is unreachable
     */
    void ping() throws RemoteException;
}
