package it.polimi.ingsw.galaxytruckerproject.network;

import it.polimi.ingsw.galaxytruckerproject.model.GameMode;
import it.polimi.ingsw.galaxytruckerproject.model.player.PlayersColor;
import it.polimi.ingsw.galaxytruckerproject.model.tiles.CargoHold;
import it.polimi.ingsw.galaxytruckerproject.model.tiles.Coordinates;
import it.polimi.ingsw.galaxytruckerproject.model.tiles.Tile;

import java.rmi.RemoteException;
import java.util.ArrayList;

/**
 * The {@code VirtualController} interface defines the remote methods available for the controller
 * in the Galaxy Trucker game. These methods are invoked by the client to perform game actions such
 * as logging in, creating and joining games, managing tiles, handling cards, and interacting with game objects.
 * <p>
 * This interface is designed to be used in a distributed setting where clients interact with a server
 * via Remote Method Invocation (RMI). The methods cover various aspects of game management, including
 * player actions, tile manipulations, and interaction with game elements like cargo holds, crew, and dice rolls.
 * </p>
 */
public class MockVirtualController implements VirtualController{
    ArrayList<Object> results = new ArrayList<>();

    // ------------------------------ PHASE OF LOGIN/CREATION OF MATCHES METHODS ------------------------------

    /**
     * Logs in the player with the specified player name.
     *
     * @param playerName The name of the player to log in.
     * @throws RemoteException If there is an error during the remote method invocation.
     */
    @Override
    public void login(String playerName) throws RemoteException{}

    /**
     * Creates a new game with the given parameters.
     *
     * @param gameName    The name of the game.
     * @param playerCount The number of players in the game.
     * @param chooseMode  The chosen game mode (e.g., Level1, Level2, etc.).
     * @throws RemoteException If there is an error during the remote method invocation.
     */
    @Override
    public void createGame(String gameName, int playerCount, GameMode chooseMode) throws RemoteException{
        results.clear();
        results.add(gameName);
        results.add(playerCount);
        results.add(chooseMode);
    }

    /**
     * Joins an existing game with the specified game name.
     *
     * @param gameName The name of the game to join.
     * @throws RemoteException If there is an error during the remote method invocation.
     */
    @Override
    public void joinGame(String gameName) throws RemoteException{
        results.clear();
        results.add(gameName);
    }

    /**
     * Leaves the current game.
     *
     * @throws RemoteException If there is an error during the remote method invocation.
     */
    @Override
    public void leaveGame() throws RemoteException{}

    /**
     * Chooses the player's color for the game.
     *
     * @param color The chosen color for the player.
     * @throws RemoteException If there is an error during the remote method invocation.
     */
    @Override
    public void chooseColor(PlayersColor color) throws RemoteException{
        results.clear();
        results.add(color);
    }

    // ------------------------------ TILE RELATED METHODS ------------------------------

    /**
     * Notifies that a tile has been placed on the game board.
     *
     * @param tile The tile that has been placed.
     * @throws RemoteException If there is an error during the remote method invocation.
     */
    @Override
    public void notifySetTile(Tile tile) throws RemoteException{
        results.clear();
        results.add(tile);
    }

    /**
     * Notifies that a tile has been refused.
     *
     * @throws RemoteException If there is an error during the remote method invocation.
     */
    @Override
    public void notifyRefusedTile() throws RemoteException{}

    /**
     * Requests to draw a tile from the turned tiles stack.
     *
     * @param index The index of the tile to draw.
     * @throws RemoteException If there is an error during the remote method invocation.
     */
    @Override
    public void reqDrawTileFromTurned(int index) throws RemoteException{
        results.clear();
        results.add(index);
    }

    /**
     * Requests to draw a tile from the regular tile stack.
     *
     * @throws RemoteException If there is an error during the remote method invocation.
     */
    @Override
    public void reqDrawTileFromStack() throws RemoteException{}

    /**
     * Notifies that a tile has been booked.
     *
     * @throws RemoteException If there is an error during the remote method invocation.
     */
    @Override
    public void notifyTileBooking() throws RemoteException{}

    // ------------------------------ CARDS RELATED METHODS ------------------------------

    /**
     * Requests to look at a specific card deck.
     *
     * @param deckToLookAt The index of the deck to look at.
     * @throws RemoteException If there is an error during the remote method invocation.
     */
    @Override
    public void lookCardsRequest(int deckToLookAt) throws RemoteException{
        results.clear();
        results.add(deckToLookAt);
    }

    /**
     * Requests to stop looking at the current card deck.
     *
     * @throws RemoteException If there is an error during the remote method invocation.
     */
    @Override
    public  void stopLookingAtCardsRequest() throws RemoteException{}

    /**
     * Requests to draw a card.
     *
     * @throws RemoteException If there is an error during the remote method invocation.
     */
    @Override
    public void drawCards() throws RemoteException{}

    // ------------------------------ COORDINATES RELATED METHODS ------------------------------

    /**
     * Sends a signal that a double cannon has been used with the given strength at specified coordinates.
     *
     * @param Strength    The strength of the cannon.
     * @param coordinates The coordinates where the cannon was used.
     * @throws RemoteException If there is an error during the remote method invocation.
     */
    @Override
    public void sendDoubleCannonUsed(float Strength, ArrayList<Coordinates> coordinates) throws RemoteException{
        results.clear();
        results.add(Strength);
        results.add(coordinates);
    }

    /**
     * Sends a signal that a double engine has been used with the given number of engines at specified coordinates.
     *
     * @param NumEngine   The number of engines used.
     * @param coordinates The coordinates where the engines were used.
     * @throws RemoteException If there is an error during the remote method invocation.
     */
    @Override
    public void sendNumDoubleEngineUsed(int NumEngine, ArrayList<Coordinates> coordinates) throws RemoteException{
        results.clear();
        results.add(NumEngine);
        results.add(coordinates);
    }

    // ------------------------------ YES/NO METHODS ------------------------------

    /**
     * Sends a "Yes" response.
     *
     * @throws RemoteException If there is an error during the remote method invocation.
     */
    @Override
    public  void sendYes() throws RemoteException{}

    /**
     * Sends a "No" response.
     *
     * @throws RemoteException If there is an error during the remote method invocation.
     */
    @Override
    public void sendNo() throws RemoteException{}

    // ------------------------------ MISCELLANEOUS METHODS ------------------------------

    /**
     * Signals to turn the hourglass.
     *
     * @throws RemoteException If there is an error during the remote method invocation.
     */
    @Override
    public void sendTurnHourGlass() throws RemoteException{}

    /**
     * Rolls the dice for the game.
     *
     * @throws RemoteException If there is an error during the remote method invocation.
     */
    @Override
    public  void rollTheDices() throws RemoteException{}

    /**
     * Requests a planet choice from the player.
     *
     * @param choice The choice for the planet.
     * @throws RemoteException If there is an error during the remote method invocation.
     */
    @Override
    public void planetChoiceRequest(int choice) throws RemoteException{
        results.clear();
        results.add(choice);
    }

    /**
     * Notifies early landing event.
     *
     * @throws RemoteException If there is an error during the remote method invocation.
     */
    @Override
    public void notifyEarlyLanding() throws RemoteException{}

    /**
     * Notifies that the ship creation is completed.
     *
     * @throws RemoteException If there is an error during the remote method invocation.
     */
    @Override
    public void notifyCompleted() throws RemoteException{}

    /**
     * Notifies that a position has been set.
     *
     * @param position The position that has been set.
     * @throws RemoteException If there is an error during the remote method invocation.
     */
    @Override
    public void notifySetPosition(int position) throws RemoteException{
        results.clear();
        results.add(position);
    }

    /**
     * Notifies the updated arrangement of goods.
     *
     * @param clientGoodsValue The new value of the client's goods.
     * @param updatedCargos    The updated cargo hold.
     * @throws RemoteException If there is an error during the remote method invocation.
     */
    @Override
    public void notifyNewGoodsArrangement(int clientGoodsValue, ArrayList<CargoHold> updatedCargos) throws RemoteException{
        results.clear();
        results.add(clientGoodsValue);
        results.add(updatedCargos);
    }

    /**
     * Notifies the updated arrangement of crew members.
     *
     * @param updatedCabin The updated cabin arrangement.
     * @throws RemoteException If there is an error during the remote method invocation.
     */
    @Override
    public void notifyNewCrewArrangement(ArrayList<Tile> updatedCabin) throws RemoteException{
        results.clear();
        results.add(updatedCabin);
    }

    /**
     * Sets the view for the controller.
     *
     * @param virtualView The view to be set for the controller.
     * @throws RemoteException If there is an error during the remote method invocation.
     */
    @Override
    public  void setView(VirtualView virtualView) throws RemoteException{
        results.clear();
        results.add(virtualView);
    }

    /**
     * Manages ship error by removing the specified tiles.
     *
     * @param toRemove The list of coordinates to be removed.
     * @throws RemoteException If there is an error during the remote method invocation.
     */
    @Override
    public  void shipErrorManagement(ArrayList<Coordinates> toRemove) throws RemoteException{
        results.clear();
        results.add(toRemove);
    }

    /**
     * Sends a ping signal to check the connection.
     *
     * @throws RemoteException If there is an error during the remote method invocation.
     */
    @Override
    public void ping() throws RemoteException{}

    /**
     * Removes goods from the specified coordinates.
     *
     * @param fromHere The coordinates from which to remove the goods.
     * @throws RemoteException If there is an error during the remote method invocation.
     */
    @Override
    public void removeGoods(ArrayList<Coordinates> fromHere) throws RemoteException{
        results.clear();
        results.add(fromHere);
    }

    /**
     * Chooses a branch to be maintained.
     *
     * @param thisOne The branch to be maintained.
     * @throws RemoteException If there is an error during the remote method invocation.
     */
    @Override
    public void chooseBranch(ArrayList<Coordinates> thisOne) throws RemoteException{
        results.clear();
        results.add(thisOne);
    }

    /**
     * Uses the specified batteries.
     *
     * @param batteries The list of batteries to be used.
     * @throws RemoteException If there is an error during the remote method invocation.
     */
    @Override
    public void useBattery(ArrayList<Coordinates> batteries) throws RemoteException{
        results.clear();
        results.add(batteries);
    }

    /**
     * Removes crew members from the specified coordinates.
     *
     * @param toRemoveFrom The coordinates to remove crew members from.
     * @throws RemoteException If there is an error during the remote method invocation.
     */
    @Override
    public void removeCrew(ArrayList<Coordinates> toRemoveFrom) throws RemoteException{
        results.clear();
        results.add(toRemoveFrom);
    }

    @Override
    public ArrayList<Object> getResults() throws RemoteException {
        return null;
    }
}
