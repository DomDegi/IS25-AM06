package it.polimi.ingsw.galaxytruckerproject.network.RMI.Server;

import it.polimi.ingsw.galaxytruckerproject.controller.Controller;
import it.polimi.ingsw.galaxytruckerproject.model.GameMode;
import it.polimi.ingsw.galaxytruckerproject.model.player.PlayersColor;
import it.polimi.ingsw.galaxytruckerproject.model.tiles.CargoHold;
import it.polimi.ingsw.galaxytruckerproject.model.tiles.Coordinates;
import it.polimi.ingsw.galaxytruckerproject.model.tiles.Tile;
import it.polimi.ingsw.galaxytruckerproject.network.VirtualController;
import it.polimi.ingsw.galaxytruckerproject.network.VirtualView;

import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

/**
 * The VirtualControllerRMI class provides the implementation for the remote control of the game server,
 * interacting with the game logic through the Controller class and managing the communication with the client via RMI.
 *
 * This class handles all remote interactions related to game actions like login, game creation, tile handling, and
 * player actions.
 */
public class VirtualControllerRMI extends UnicastRemoteObject implements VirtualController, Remote, Serializable {

    private Controller controller;

    /**
     * Constructs a new VirtualControllerRMI object and initializes the server controller.
     *
     * @param controller The game controller that handles the game logic and client interactions.
     * @throws RemoteException If there is an issue during remote object creation.
     */
    public VirtualControllerRMI(Controller controller) throws RemoteException {
        super();
        this.controller = controller;
    }

    /**
     * Logs in the player with the given player name.
     *
     * @param playerName The name of the player attempting to log in.
     * @throws RemoteException If there is a remote communication issue.
     */
    public void login(String playerName) throws RemoteException {
        controller.login(playerName);
    }

    /**
     * Creates a new game with the specified settings.
     *
     * @param gameName The name of the game.
     * @param playerCount The number of players for the game.
     * @param chooseMode The chosen game mode (e.g., normal, trial).
     * @throws RemoteException If there is a remote communication issue.
     */
    public void createGame(String gameName, int playerCount, GameMode chooseMode) throws RemoteException {
        controller.createGame(gameName, playerCount, chooseMode);
    }

    /**
     * Joins an existing game with the specified game name.
     *
     * @param gameName The name of the game to join.
     * @throws RemoteException If there is a remote communication issue.
     */
    public void joinGame(String gameName) throws RemoteException {
        controller.joinGame(gameName);
    }

    /**
     * Leaves the current game.
     *
     * @throws RemoteException If there is a remote communication issue.
     */
    public void leaveGame() throws RemoteException {
        controller.leaveGame();
    }

    /**
     * Allows the player to choose a color for their character.
     *
     * @param color The chosen player color.
     * @throws RemoteException If there is a remote communication issue.
     */
    public void chooseColor(PlayersColor color) throws RemoteException {
        controller.chooseColor(color);
    }

    /**
     * Sends the command for the player to use a double cannon with a specified strength.
     *
     * @param doublePower The strength of the double cannon.
     * @param batteries The coordinates of the batteries used for the cannon.
     * @throws RemoteException If there is a remote communication issue.
     */
    public void sendDoubleCannonUsed(float doublePower, ArrayList<Coordinates> batteries) throws RemoteException {
        controller.useCannons(doublePower, batteries);
    }

    /**
     * Sends the command for the player to use a specified number of double engines.
     *
     * @param numEngine The number of engines used.
     * @param batteries The coordinates of the batteries used for the engines.
     * @throws RemoteException If there is a remote communication issue.
     */
    public void sendNumDoubleEngineUsed(int numEngine, ArrayList<Coordinates> batteries) throws RemoteException {
        controller.useEngines(numEngine, batteries);
    }

    /**
     * Sends a "Yes" response to the game logic (e.g., to confirm a decision).
     *
     * @throws RemoteException If there is a remote communication issue.
     */
    public void sendYes() throws RemoteException {
        controller.makeAChoice(true);
    }

    /**
     * Sends a "No" response to the game logic (e.g., to reject a decision).
     *
     * @throws RemoteException If there is a remote communication issue.
     */
    public void sendNo() throws RemoteException {
        controller.makeAChoice(false);
    }

    // --- TILE METHODS ---

    /**
     * Notifies the game logic that a tile has been set.
     *
     * @param tile The tile that has been set.
     * @throws RemoteException If there is a remote communication issue.
     */
    public void notifySetTile(Tile tile) throws RemoteException {
        controller.setTile(tile);
    }

    /**
     * Notifies the game logic that a tile has been booked.
     *
     * @throws RemoteException If there is a remote communication issue.
     */
    public void notifyTileBooking() throws RemoteException {
        controller.bookTile();
    }

    /**
     * Notifies the game logic that a tile has been refused.
     *
     * @throws RemoteException If there is a remote communication issue.
     */
    public void notifyRefusedTile() throws RemoteException {
        controller.refuseTile();
    }

    /**
     * Requests the drawing of a tile from the stack.
     *
     * @throws RemoteException If there is a remote communication issue.
     */
    public void reqDrawTileFromStack() throws RemoteException {
        controller.drawTileFromStack();
    }

    /**
     * Requests the drawing of a tile from the turned tiles.
     *
     * @param index The index of the turned tile to draw.
     * @throws RemoteException If there is a remote communication issue.
     */
    public void reqDrawTileFromTurned(int index) throws RemoteException {
        controller.drawTileFromTurned(index);
    }

    // --- CARD METHODS ---

    /**
     * Requests to look at the specified deck of cards.
     *
     * @param deckToLookAt The deck to look at.
     * @throws RemoteException If there is a remote communication issue.
     */
    public void lookCardsRequest(int deckToLookAt) throws RemoteException {
        controller.lookGameCards(deckToLookAt);
    }

    /**
     * Stops looking at the current cards.
     *
     * @throws RemoteException If there is a remote communication issue.
     */
    public void stopLookingAtCardsRequest() throws RemoteException {
        controller.stopLookingAtCards();
    }

    /**
     * Requests to draw a card.
     *
     * @throws RemoteException If there is a remote communication issue.
     */
    public void drawCards() throws RemoteException {
        controller.drawCard();
    }

    // --- GAME ACTIONS ---

    /**
     * Sends the command to turn the hourglass.
     *
     * @throws RemoteException If there is a remote communication issue.
     */
    public void sendTurnHourGlass() throws RemoteException {
        controller.turnHourglass();
    }

    /**
     * Notifies that the player has landed on a planet.
     *
     * @throws RemoteException If there is a remote communication issue.
     */
    public void notifyEarlyLanding() throws RemoteException {
        controller.earlyLanding();
    }

    /**
     * Notifies that the player's ship has been completed.
     *
     * @throws RemoteException If there is a remote communication issue.
     */
    public void notifyCompleted() throws RemoteException {
        controller.completedShip();
    }

    /**
     * Sets the player's position.
     *
     * @param position The position to set.
     * @throws RemoteException If there is a remote communication issue.
     */
    public void notifySetPosition(int position) throws RemoteException {
        controller.setPosition(position);
    }

    /**
     * Requests the player to choose a planet.
     *
     * @param planet The planet to choose.
     * @throws RemoteException If there is a remote communication issue.
     */
    public void planetChoiceRequest(int planet) throws RemoteException {
        controller.choosePlanet(planet);
    }

    /**
     * Notifies the game logic of changes to the player's goods.
     *
     * @param creditsToVerify The number of credits to verify.
     * @param updatedCargos The updated cargo holds.
     * @throws RemoteException If there is a remote communication issue.
     */
    public void notifyNewGoodsArrangement(int creditsToVerify, ArrayList<CargoHold> updatedCargos) throws RemoteException {
        controller.manageGoods(creditsToVerify, updatedCargos);
    }

    /**
     * Notifies the game logic of changes to the player's crew arrangement.
     *
     * @param cabins The updated cabin tiles.
     * @throws RemoteException If there is a remote communication issue.
     */
    public void notifyNewCrewArrangement(ArrayList<Tile> cabins) throws RemoteException {
        controller.pickCrewMembers(cabins);
    }

    // --- PING AND OTHER METHODS ---

    /**
     * Sends a ping message to the server to keep the connection alive.
     *
     * @throws RemoteException If there is a remote communication issue.
     */
    public void ping() throws RemoteException {
        controller.ping();
    }

    /**
     * Sets the VirtualView for this controller, linking it to the game's view.
     *
     * @param virtualView The VirtualView object representing the game's view.
     * @throws RemoteException If there is a remote communication issue.
     */
    @Override
    public void setView(VirtualView virtualView) throws RemoteException {
        controller.setView(virtualView);
    }

    /**
     * Manages ship error handling by removing specific components from the ship.
     *
     * @param toRemove A list of coordinates indicating the components to be removed due to the ship error.
     * @throws RemoteException If there is a remote communication issue.
     */
    @Override
    public void shipErrorManagement(ArrayList<Coordinates> toRemove) throws RemoteException {
        controller.shipErrorManagement(toRemove);
    }

    /**
     * Removes goods from the specified coordinates on the ship.
     *
     * @param fromHere A list of coordinates indicating the locations where goods should be removed.
     * @throws RemoteException If there is a remote communication issue.
     */
    @Override
    public void removeGoods(ArrayList<Coordinates> fromHere) throws RemoteException {
        controller.removeGoods(fromHere);
    }

    /**
     * Chooses a specific branch for the player's path, typically used in decision-making during the game.
     *
     * @param thisOne A list of coordinates representing the branch that should be selected.
     * @throws RemoteException If there is a remote communication issue.
     */
    @Override
    public void chooseBranch(ArrayList<Coordinates> thisOne) throws RemoteException {
        controller.chooseBranch(thisOne);
    }

    /**
     * Uses the battery at the specified coordinates to power the ship's systems.
     *
     * @param batteries A list of coordinates indicating the locations of the batteries being used.
     * @throws RemoteException If there is a remote communication issue.
     */
    @Override
    public void useBattery(ArrayList<Coordinates> batteries) throws RemoteException {
        controller.useBatteries(batteries);
    }

    /**
     * Removes crew members from the specified coordinates on the ship.
     *
     * @param toRemoveFrom A list of coordinates indicating the locations where crew members should be removed.
     * @throws RemoteException If there is a remote communication issue.
     */
    @Override
    public void removeCrew(ArrayList<Coordinates> toRemoveFrom) throws RemoteException {
        controller.removeCrew(toRemoveFrom);
    }

    @Override
    public ArrayList<Object> getResults() throws RemoteException {
        return null;
    }

    /**
     * Rolls the dice as part of the game mechanics.
     * This method triggers the roll of the dice through the controller,
     * affecting the game state accordingly.
     *
     * @throws RemoteException If there is a communication issue during the remote call.
     */
    public void rollTheDices() throws RemoteException {
        controller.rollTheDices();
    }

}
