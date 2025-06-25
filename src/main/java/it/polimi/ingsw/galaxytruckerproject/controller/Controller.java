package it.polimi.ingsw.galaxytruckerproject.controller;

import it.polimi.ingsw.galaxytruckerproject.controller.interfaces.ControllerInterface;
import it.polimi.ingsw.galaxytruckerproject.model.GameMode;
import it.polimi.ingsw.galaxytruckerproject.model.player.PlayersColor;
import it.polimi.ingsw.galaxytruckerproject.model.tiles.CargoHold;
import it.polimi.ingsw.galaxytruckerproject.model.tiles.Coordinates;
import it.polimi.ingsw.galaxytruckerproject.model.tiles.Tile;
import it.polimi.ingsw.galaxytruckerproject.network.VirtualView;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.ArrayList;

/**
 * This controller class represents the server-side object bound to each connected client.
 * It implements {@link ControllerInterface} and handles communication between a specific player
 * and the game logic managed by the {@link MultiGameController} and {@link GameController}.
 *
 * <p>
 * Each player has their own instance of this class, which acts as a proxy for game operations.
 * It is also responsible for delegating commands and state updates to the appropriate controller
 * and view components.
 * </p>
 */
public class Controller implements ControllerInterface, Serializable {

    /**
     * The nickname of the player associated with this controller.
     */
    private String nickname;

    /**
     * Reference to the shared controller that manages multiple game instances.
     */
    private MultiGameController multiGameController;

    /**
     * Reference to the virtual view of the player, used to communicate updates to the client.
     */
    private VirtualView view;

    /**
     * The specific game controller assigned to the player after joining or creating a game.
     */
    private GameController gameController;

    /**
     * Constructs a controller for a client, binding it to the central {@link MultiGameController}.
     *
     * @param multiGameController the shared controller handling all games
     */
    public Controller(MultiGameController multiGameController) {
        this.multiGameController = multiGameController;
    }

    /**
     * Sets the player's virtual view, used for communication with the client.
     *
     * @param view the player's {@link VirtualView}
     */
    public void setView(VirtualView view) {
        this.view = view;
    }

    /**
     * Sets the game controller for the joined game.
     *
     * @param gameController the {@link GameController} to assign
     */
    @Override
    public void setGameController(GameController gameController) {
        this.gameController = gameController;
    }

    /**
     * Handles login request and registers the player via the multi-game controller.
     *
     * @param nickname the nickname of the player
     */
    @Override
    public void login(String nickname) {
        if (multiGameController.login(nickname, view, this)) {
            this.nickname = nickname;
        }
    }

    /**
     * Requests creation of a new game via the multi-game controller.
     *
     * @param gameName name of the new game
     * @param playerCount maximum number of players
     * @param chosenMode selected {@link GameMode}
     */
    @Override
    public void createGame(String gameName, int playerCount, GameMode chosenMode) {
        multiGameController.createGame(nickname, gameName, playerCount, this, chosenMode);
    }

    /**
     * Requests to join an existing game via the multi-game controller.
     *
     * @param gameName the name of the game to join
     */
    @Override
    public void joinGame(String gameName) {
        multiGameController.joinGame(nickname, gameName, this);
    }

    /**
     * Requests to leave the current game (if in lobby) via the multi-game controller.
     */
    @Override
    public void leaveGame() {
        multiGameController.leaveGame(nickname);
    }

    /**
     * Sends the player's chosen color to the game controller.
     *
     * @param color the chosen {@link PlayersColor}
     */
    @Override
    public void chooseColor(PlayersColor color) {
        if (gameController.isRestarted()) {
            return;
        }
        if (gameController.checkColorAvailable(nickname, view, color)) {
            gameController.playerAddition(nickname, color);
        }
    }

    /**
     * Signals that the player has turned the hourglass, possibly triggering a timer or urgency effect.
     */
    @Override
    public void turnHourglass() {
        gameController.turnHourglass(this.nickname);
    }

    /**
     * Draws a tile from the shared central stack and sends it to the player.
     */
    @Override
    public void drawTileFromStack() {
        gameController.drawTile(view, nickname, 0, false);
    }

    /**
     * Draws a tile from the available turned (face-up) tile list by index.
     *
     * @param index the index of the tile to draw from the turned list
     */
    @Override
    public void drawTileFromTurned(int index) {
        gameController.drawTile(view, nickname, index, true);
    }

    /**
     * Refuses or discards the currently held tile.
     */
    @Override
    public void refuseTile() {
        gameController.refuseTile(nickname);
    }

    /**
     * Allows the player to view a game card during gameplay.
     *
     * @param index the index of the card to view
     */
    @Override
    public void lookGameCards(int index) {
        gameController.lookGameCards(nickname, view, index);
    }

    /**
     * Stops the viewing of game cards and returns control to normal gameplay.
     */
    @Override
    public void stopLookingAtCards() {
        gameController.stopLookingAtCards(view, nickname);
    }

    /**
     * Attempts to place the given tile on the player's ship.
     *
     * @param tile the tile to place
     */
    @Override
    public void setTile(Tile tile) {
        gameController.setTile(view, nickname, tile);
    }

    /**
     * Books the currently held tile, allowing the player to reserve it for later.
     */
    @Override
    public void bookTile() {
        gameController.bookTile(view, nickname);
    }

    /**
     * Notifies the game controller of tiles that must be removed due to ship construction errors.
     *
     * @param toRemove list of coordinates of tiles to remove
     */
    @Override
    public void shipErrorManagement(ArrayList<Coordinates> toRemove) {
        gameController.shipErrorManagement(nickname, view, toRemove);
    }

    /**
     * Signals that the player has completed construction of their ship.
     */
    @Override
    public void completedShip() {
        gameController.completed(nickname, view);
    }

    /**
     * Draws the next card from the event deck.
     */
    @Override
    public void drawCard() {
        gameController.drawCard(nickname, view);
    }

    /**
     * Requests early landing for the player, skipping remaining travel events.
     */
    @Override
    public void earlyLanding() {
        gameController.earlyLanding(nickname, view);
    }

    /**
     * Activates the player's cannons, optionally using batteries.
     *
     * @param doublePower the power of the double cannon being used
     * @param batteries the coordinates of the batteries consumed
     */
    @Override
    public void useCannons(float doublePower, ArrayList<Coordinates> batteries) {
        gameController.playerUsesCannons(nickname, doublePower, batteries);
    }

    /**
     * Activates the player's engines, optionally using batteries.
     *
     * @param numberOfDoubleEngines the number of double engines to activate
     * @param batteries the coordinates of batteries to consume
     */
    @Override
    public void useEngines(int numberOfDoubleEngines, ArrayList<Coordinates> batteries) {
        gameController.playerUsesEngines(nickname, numberOfDoubleEngines, batteries);
    }

    /**
     * Validates and processes the player's declared goods and credits.
     *
     * @param clientCreditsToVerify the total credits declared by the player
     * @param updatedCargos the list of cargo holds with updated contents
     */
    @Override
    public void manageGoods(int clientCreditsToVerify, ArrayList<CargoHold> updatedCargos) {
        gameController.playerManagesGoods(nickname, clientCreditsToVerify, updatedCargos);
    }

    /**
     * Handles a binary choice (e.g., yes/no, take/pass) made by the player.
     *
     * @param choice the player's decision
     */
    @Override
    public void makeAChoice(boolean choice) {
        gameController.playerMakesAChoice(nickname, choice);
    }

    /**
     * Handles the player's choice of planet (e.g., for colonization or bonus selection).
     *
     * @param planet the index or ID of the selected planet
     */
    @Override
    public void choosePlanet(int planet) {
        gameController.playerChoosesPlanet(nickname, planet);
    }

    /**
     * Allows the player to assign crew members to specific cabins at the beginning of the flight.
     *
     * @param cabins list of cabin tiles to assign crew to
     */
    @Override
    public void pickCrewMembers(ArrayList<Tile> cabins) {
        gameController.playerPicksCrewMembers(nickname, view, cabins);
    }

    /**
     * Sets the player's position on the flight board (used in Level 2 mode).
     *
     * @param position the position (1-based index) assigned to the player
     */
    @Override
    public void setPosition(int position) {
        gameController.setPosition(nickname, view, position);
    }

    /**
     * Removes crew members from specific coordinates (e.g., due to damage or events).
     *
     * @param toRemoveFrom list of coordinates where crew should be removed
     */
    @Override
    public void removeCrew(ArrayList<Coordinates> toRemoveFrom) {
        gameController.playerRemovesCrew(nickname, toRemoveFrom);
    }


    /**
     * Requests removal of goods from the specified tiles.
     *
     * @param fromHere list of cargo hold coordinates to remove goods from
     */
    @Override
    public void removeGoods(ArrayList<Coordinates> fromHere) {
        gameController.playerRemovesGoods(nickname, fromHere);
    }


    /**
     * Requests to consume battery power from the specified battery tiles.
     *
     * @param batteries list of coordinates containing battery components
     */
    @Override
    public void useBatteries(ArrayList<Coordinates> batteries) {
        gameController.playerUsesBatteries(nickname, batteries);
    }


    /**
     * Called when the player requests to roll the dice (e.g. for combat).
     *
     * @throws RemoteException if the remote call fails
     */
    @Override
    public void rollTheDices() throws RemoteException {
        gameController.playerRollsTheDices(nickname);
    }

    /**
     * Lets the player choose a path or branch during an event or encounter.
     *
     * @param thisOne list of coordinates representing the chosen path
     */
    @Override
    public void chooseBranch(ArrayList<Coordinates> thisOne) {
        gameController.playerChoosesBranch(nickname, thisOne);
    }


    /**
     * Returns the virtual view object associated with this controller.
     * This view is used to communicate with the client.
     *
     * @return the {@link VirtualView} of the player
     */
    public VirtualView getView() {
        return view;
    }


    /**
     * Confirms the player's connection is still active by sending a "pong" to the game controller.
     * Used in connection health checks (e.g., from RMI clients).
     */
    public void ping() {
        gameController.pong(nickname);
    }

    //For testing purposes
    public Controller(String playerName, GameController game,VirtualView view) {
        this.nickname = playerName;
        this.gameController = game;
        this.view = view;
        this.multiGameController = null;
    }

    public void setMultiGameController(MultiGameController multiGameController) {
        this.multiGameController = multiGameController;
    }
}
