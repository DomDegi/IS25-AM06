package it.polimi.ingsw.galaxytruckerproject.network.Socket;

import it.polimi.ingsw.galaxytruckerproject.model.GameMode;
import it.polimi.ingsw.galaxytruckerproject.model.player.PlayersColor;
import it.polimi.ingsw.galaxytruckerproject.model.tiles.CargoHold;
import it.polimi.ingsw.galaxytruckerproject.model.tiles.Coordinates;
import it.polimi.ingsw.galaxytruckerproject.model.tiles.Tile;
import it.polimi.ingsw.galaxytruckerproject.network.Socket.ClientMessage.*;
import it.polimi.ingsw.galaxytruckerproject.network.VirtualController;
import it.polimi.ingsw.galaxytruckerproject.network.VirtualView;
import it.polimi.ingsw.galaxytruckerproject.view.DisplayableView;

import java.rmi.RemoteException;
import java.util.ArrayList;

/**
 * The `VirtualControllerSocket` class is an implementation of the `VirtualController` interface for handling
 * client-server communication in the socket-based architecture of the game. It is responsible for processing
 * various client requests and sending corresponding messages to the server using the `ServerHandler`.
 * <p>
 * This class acts as a bridge between the client's actions (such as logging in, creating a game, or drawing tiles)
 * and the server's responses. Each method in this class sends a specific type of message to the server, which will
 * process the message and update the game state accordingly.
 */
public class VirtualControllerSocket implements VirtualController {

    /**
     * The view associated with this controller, which is used for displaying information to the user.
     */
    private DisplayableView view;

    /**
     * The server handler that handles communication between the client and server.
     */
    private ServerHandler serverHandler;

    /**
     * Constructs a new `VirtualControllerSocket` with the specified view and server handler.
     *
     * @param view The view to be associated with this controller.
     * @param serverHandler The server handler used for communication with the server.
     */
    public VirtualControllerSocket(DisplayableView view, ServerHandler serverHandler) {
        this.view = view;
        this.serverHandler = serverHandler;
    }

    /**
     * Logs in the client with the specified player name.
     *
     * @param playerName The name of the player attempting to log in.
     * @throws RemoteException If an error occurs during the remote communication.
     */
    @Override
    public void login(String playerName) throws RemoteException {
        serverHandler.sendClientMessage(new LoginMessage(playerName));
    }

    /**
     * Creates a new game with the specified game name, number of players, and game mode.
     *
     * @param gameName The name of the new game.
     * @param playerCount The number of players for the game.
     * @param chooseMode The mode of the game (e.g., Trial, Level 2).
     * @throws RemoteException If an error occurs during the remote communication.
     */
    @Override
    public void createGame(String gameName, int playerCount, GameMode chooseMode) throws RemoteException {
        CreateGameMessage message = new CreateGameMessage(gameName, playerCount, chooseMode);
        serverHandler.sendClientMessage(message);
    }

    /**
     * Joins an existing game with the specified game name.
     *
     * @param gameName The name of the game to join.
     * @throws RemoteException If an error occurs during the remote communication.
     */
    @Override
    public void joinGame(String gameName) throws RemoteException {
        JoinGameMessage message = new JoinGameMessage(gameName);
        serverHandler.sendClientMessage(message);
    }

    /**
     * Leaves the current game.
     *
     * @throws RemoteException If an error occurs during the remote communication.
     */
    @Override
    public void leaveGame() throws RemoteException {
        LeaveGameMessage message = new LeaveGameMessage();
        serverHandler.sendClientMessage(message);
    }

    /**
     * Chooses the color for the client.
     *
     * @param color The color chosen by the client.
     * @throws RemoteException If an error occurs during the remote communication.
     */
    @Override
    public void chooseColor(PlayersColor color) throws RemoteException {
        serverHandler.sendClientMessage(new ChooseColorMessage(color));
    }

    /**
     * Sends a message to the server notifying the server of a tile being set.
     *
     * @param tile The tile that is being set.
     * @throws RemoteException If an error occurs during the remote communication.
     */
    @Override
    public void notifySetTile(Tile tile) throws RemoteException {
        SetTileMessage message = new SetTileMessage(tile);
        serverHandler.sendClientMessage(message);
    }

    /**
     * Notifies the server that a tile was refused.
     *
     * @throws RemoteException If an error occurs during the remote communication.
     */
    @Override
    public void notifyRefusedTile() throws RemoteException {
        RefuseTileMessage message = new RefuseTileMessage();
        serverHandler.sendClientMessage(message);
    }

    /**
     * Sends a request to the server to draw a tile from the turned tiles stack.
     *
     * @param index The index of the tile to be drawn.
     * @throws RemoteException If an error occurs during the remote communication.
     */
    @Override
    public void reqDrawTileFromTurned(int index) throws RemoteException {
        DrawTileTurnedMessage message = new DrawTileTurnedMessage(index);
        serverHandler.sendClientMessage(message);
    }

    /**
     * Sends a request to the server to draw a tile from the stack of available tiles.
     *
     * @throws RemoteException If an error occurs during the remote communication.
     */
    @Override
    public void reqDrawTileFromStack() throws RemoteException {
        DrawTileStackMessage message = new DrawTileStackMessage();
        serverHandler.sendClientMessage(message);
    }

    /**
     * Notifies the server that the client has booked a tile.
     *
     * @throws RemoteException If an error occurs during the remote communication.
     */
    @Override
    public void notifyTileBooking() throws RemoteException {
        TileBookingMessage message = new TileBookingMessage();
        serverHandler.sendClientMessage(message);
    }

    /**
     * Requests the server to allow the client to look at the cards of a specific deck.
     *
     * @param deckToLookAt The index of the deck the client wants to look at.
     * @throws RemoteException If there is a problem during the remote communication.
     */
    @Override
    public void lookCardsRequest(int deckToLookAt) throws RemoteException {
        LookGameCardMessage message = new LookGameCardMessage(deckToLookAt);
        serverHandler.sendClientMessage(message);
    }

    /**
     * Notifies the server that the client has finished looking at the cards.
     *
     * @throws RemoteException If there is a problem during the remote communication.
     */
    @Override
    public void stopLookingAtCardsRequest() throws RemoteException {
        StopLookingCardsMessage message = new StopLookingCardsMessage();
        serverHandler.sendClientMessage(message);
    }

    /**
     * Requests the server to draw a card from the available deck.
     *
     * @throws RemoteException If there is a problem during the remote communication.
     */
    @Override
    public void drawCards() throws RemoteException {
        serverHandler.sendClientMessage(new DrawCardMessage());
    }

    /**
     * Sends the server a message that the client has used a double cannon with the specified strength.
     *
     * @param Strength The strength of the double cannon used.
     * @param coordinates The coordinates where the cannon is used.
     * @throws RemoteException If there is a problem during the remote communication.
     */
    @Override
    public void sendDoubleCannonUsed(float Strength, ArrayList<Coordinates> coordinates) throws RemoteException {
        serverHandler.sendClientMessage(new CannonMessage(Strength, coordinates));
    }

    /**
     * Sends the server a message that the client has used the specified number of double engines.
     *
     * @param NumEngine The number of double engines used.
     * @param coordinates The coordinates where the engines are used.
     * @throws RemoteException If there is a problem during the remote communication.
     */
    @Override
    public void sendNumDoubleEngineUsed(int NumEngine, ArrayList<Coordinates> coordinates) throws RemoteException {
        serverHandler.sendClientMessage(new EngineMessage(NumEngine, coordinates));
    }

    /**
     * Sends a "Yes" response to the server.
     *
     * @throws RemoteException If there is a problem during the remote communication.
     */
    @Override
    public void sendYes() throws RemoteException {
        SendYesMessage message = new SendYesMessage();
        serverHandler.sendClientMessage(message);
    }

    /**
     * Sends a "No" response to the server.
     *
     * @throws RemoteException If there is a problem during the remote communication.
     */
    @Override
    public void sendNo() throws RemoteException {
        SendNoMessage message = new SendNoMessage();
        serverHandler.sendClientMessage(message);
    }

    /**
     * Notifies the server that the client has turned the hourglass.
     *
     * @throws RemoteException If there is a problem during the remote communication.
     */
    @Override
    public void sendTurnHourGlass() throws RemoteException {
        TurnHourglassMessage message = new TurnHourglassMessage();
        serverHandler.sendClientMessage(message);
    }

    /**
     * Requests the server to roll the dice.
     *
     * @throws RemoteException If there is a problem during the remote communication.
     */
    @Override
    public void rollTheDices() throws RemoteException {
        RollDicesMessage message = new RollDicesMessage();
        serverHandler.sendClientMessage(message);
    }

    /**
     * Sends the server a request for a planet choice.
     *
     * @param choice The planet choice made by the client.
     * @throws RemoteException If there is a problem during the remote communication.
     */
    @Override
    public void planetChoiceRequest(int choice) throws RemoteException {
        serverHandler.sendClientMessage(new PlanetChoiceMessage(choice));
    }

    /**
     * Notifies the server that the client has landed early.
     *
     * @throws RemoteException If there is a problem during the remote communication.
     */
    @Override
    public void notifyEarlyLanding() throws RemoteException {
        serverHandler.sendClientMessage(new EarlyLandingMessage());
    }

    /**
     * Notifies the server that the client has completed the shipboard setup.
     *
     * @throws RemoteException If there is a problem during the remote communication.
     */
    @Override
    public void notifyCompleted() throws RemoteException {
        serverHandler.sendClientMessage(new CompletedShipMessage());
    }

    /**
     * Sends the server the client's current position on the flight board.
     *
     * @param position The current position of the client on the flight board.
     * @throws RemoteException If there is a problem during the remote communication.
     */
    @Override
    public void notifySetPosition(int position) throws RemoteException {
        serverHandler.sendClientMessage(new SetFlightBoardPositionMessage(position));
    }

    /**
     * Notifies the server of the new goods arrangement for the client.
     *
     * @param clientGoodsValue The total value of the client's goods.
     * @param updatedCargos The updated list of cargo holds.
     * @throws RemoteException If there is a problem during the remote communication.
     */
    @Override
    public void notifyNewGoodsArrangement(int clientGoodsValue, ArrayList<CargoHold> updatedCargos) throws RemoteException {
        serverHandler.sendClientMessage(new GoodsArrangementMessage(clientGoodsValue, updatedCargos));
    }

    /**
     * Notifies the server of the new crew arrangement for the client.
     *
     * @param updatedCabin The updated list of crew tiles.
     * @throws RemoteException If there is a problem during the remote communication.
     */
    @Override
    public void notifyNewCrewArrangement(ArrayList<Tile> updatedCabin) throws RemoteException {
        serverHandler.sendClientMessage(new CrewArrangementMessage(updatedCabin));
    }

    /**
     * Sets the virtual view for this controller. This is used for sending updates to the client.
     *
     * @param virtualView The virtual view to be set.
     * @throws RemoteException If there is a problem during the remote communication.
     */
    @Override
    public void setView(VirtualView virtualView) throws RemoteException {
    }

    /**
     * Notifies the server to handle any ship errors and remove the specified tiles.
     *
     * @param toRemove The list of coordinates for the tiles to be removed.
     * @throws RemoteException If there is a problem during the remote communication.
     */
    @Override
    public void shipErrorManagement(ArrayList<Coordinates> toRemove) throws RemoteException {
        serverHandler.sendClientMessage(new ShipErrorManagementMessage(toRemove));
    }

    /**
     * Sends a ping message to the server.
     *
     * @throws RemoteException If there is a problem during the remote communication.
     */
    @Override
    public void ping() throws RemoteException {
        serverHandler.sendClientMessage(new ClientPingMessage());
    }

    /**
     * Notifies the server to remove goods from the specified coordinates.
     *
     * @param fromHere The list of coordinates to remove goods from.
     * @throws RemoteException If there is a problem during the remote communication.
     */
    @Override
    public void removeGoods(ArrayList<Coordinates> fromHere) throws RemoteException {
        serverHandler.sendClientMessage(new RemoveGoodsMessage(fromHere));
    }

    /**
     * Notifies the server to choose a branch from the specified coordinates.
     *
     * @param thisOne The list of coordinates for the chosen branch.
     * @throws RemoteException If there is a problem during the remote communication.
     */
    @Override
    public void chooseBranch(ArrayList<Coordinates> thisOne) throws RemoteException {
        serverHandler.sendClientMessage(new ChooseBranchMessage(thisOne));
    }

    /**
     * Notifies the server to use the specified batteries at the given coordinates.
     *
     * @param batteries The list of coordinates for the batteries to be used.
     * @throws RemoteException If there is a problem during the remote communication.
     */
    @Override
    public void useBattery(ArrayList<Coordinates> batteries) throws RemoteException {
        serverHandler.sendClientMessage(new UseBatteryMessage(batteries));
    }

    /**
     * Notifies the server to remove crew from the specified coordinates.
     *
     * @param toRemoveFrom The list of coordinates where crew should be removed from.
     * @throws RemoteException If there is a problem during the remote communication.
     */
    @Override
    public void removeCrew(ArrayList<Coordinates> toRemoveFrom) throws RemoteException {
        serverHandler.sendClientMessage(new RemoveCrewMessage(toRemoveFrom));
    }

    @Override
    public ArrayList<Object> getResults() throws RemoteException {
        return null;
    }
}
