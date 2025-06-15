package it.polimi.ingsw.galaxytruckerproject.network.Socket;

import it.polimi.ingsw.galaxytruckerproject.client.ClientState;
import it.polimi.ingsw.galaxytruckerproject.client.CoordReqType;
import it.polimi.ingsw.galaxytruckerproject.controller.interfaces.ControllerInterface;
import it.polimi.ingsw.galaxytruckerproject.model.GameInfo;
import it.polimi.ingsw.galaxytruckerproject.model.GameMode;
import it.polimi.ingsw.galaxytruckerproject.model.cards.Card;
import it.polimi.ingsw.galaxytruckerproject.model.player.Player;
import it.polimi.ingsw.galaxytruckerproject.model.player.PlayersColor;
import it.polimi.ingsw.galaxytruckerproject.model.tiles.Coordinates;
import it.polimi.ingsw.galaxytruckerproject.model.tiles.Tile;
import it.polimi.ingsw.galaxytruckerproject.network.Socket.ServerMessage.*;
import it.polimi.ingsw.galaxytruckerproject.network.Socket.ServerMessage.LoginResponseMessage;
import it.polimi.ingsw.galaxytruckerproject.network.Socket.ServerMessage.SetClientStateMessage;
import it.polimi.ingsw.galaxytruckerproject.network.Socket.ServerMessage.ShowJoinableGamesMessage;
import it.polimi.ingsw.galaxytruckerproject.network.VirtualView;
import it.polimi.ingsw.galaxytruckerproject.view.DisplayableView;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Map;

/**
 * Implementation of the VirtualView interface that communicates with the server
 * through the client handler. It is used to notify the client about various game events.
 */
public class VirtualViewSocket implements VirtualView {

    /** The handler for communication with the client */
    private final ClientHandler clientHandler;

    /** The controller responsible for managing the game's logic */
    private ControllerInterface controller;

    /**
     * Constructs a VirtualViewSocket instance.
     *
     * @param clientHandler The handler used to communicate with the client.
     */
    public VirtualViewSocket(ClientHandler clientHandler) {
        this.clientHandler = clientHandler;
    }

    /**
     * Sets the controller responsible for managing the game logic.
     *
     * @param controller The controller to set.
     */
    public void setController(ControllerInterface controller) {
        this.controller = controller;
    }

    /**
     * Notifies the client about a new turned tile.
     *
     * @param tile The tile that has been turned.
     * @throws RemoteException If there is an error during the remote communication.
     */
    @Override
    public void notifyNewTurnedTile(Tile tile) throws RemoteException {
        clientHandler.sendServerMessageToClient(new NewTurnedTileMessage(tile.send()));
    }

    /**
     * Notifies the client that a turned tile has been removed.
     *
     * @param tile The tile that has been removed.
     * @throws RemoteException If there is an error during the remote communication.
     */
    @Override
    public void notifyRemoveTurnedTile(Tile tile) throws RemoteException {
        clientHandler.sendServerMessageToClient(new RemoveTurnedTileMessage(tile));
    }

    /**
     * Notifies the client about a player's movement in the game.
     *
     * @param playerName The name of the player.
     * @param playersColor The color of the player.
     * @param playerPosition The player's position in the game.
     * @param playerRanking The player's ranking.
     * @throws RemoteException If there is an error during the remote communication.
     */
    @Override
    public void notifyPlayerMovement(String playerName, PlayersColor playersColor, int playerPosition, int playerRanking) throws RemoteException {
        clientHandler.sendServerMessageToClient(new PlayerMovementMessage(playerName, playersColor, playerPosition, playerRanking));
    }

    /**
     * Notifies the client that a player has positioned a tile.
     *
     * @param playerName The name of the player.
     * @param tile The tile that was positioned.
     * @throws RemoteException If there is an error during the remote communication.
     */
    @Override
    public void notifyPositionedTile(String playerName, Tile tile) throws RemoteException {
        clientHandler.sendServerMessageToClient(new PositionedTileMessage(playerName, tile));
    }

    /**
     * Notifies the client that a player has booked a tile.
     *
     * @param playerName The name of the player.
     * @param tile The tile that was booked.
     * @throws RemoteException If there is an error during the remote communication.
     */
    @Override
    public void notifyBookedTile(String playerName, Tile tile) throws RemoteException {
        clientHandler.sendServerMessageToClient(new BookedTileMessage(playerName, tile));
    }

    /**
     * Notifies the client that a booked tile has been removed.
     *
     * @param playerName The name of the player.
     * @param tile The tile that was removed.
     * @throws RemoteException If there is an error during the remote communication.
     */
    @Override
    public void notifyRemovedBookedTile(String playerName, Tile tile) throws RemoteException {
        clientHandler.sendServerMessageToClient(new RemovedBookedTileMessage(playerName, tile));
    }

    /**
     * Notifies the client about unavailable card decks.
     *
     * @param lockedSmallDecks The list of unavailable small decks.
     * @throws RemoteException If there is an error during the remote communication.
     */
    @Override
    public void notifyNotAvailableCardDeck(ArrayList<Integer> lockedSmallDecks) throws RemoteException {
        clientHandler.sendServerMessageToClient(new NotAvailableCardDeckMessage(lockedSmallDecks));
    }

    /**
     * Notifies the client about unavailable player colors.
     *
     * @param color The list of unavailable colors.
     * @throws RemoteException If there is an error during the remote communication.
     */
    @Override
    public void notifyNotAvailableColor(ArrayList<PlayersColor> color) throws RemoteException {
        clientHandler.sendServerMessageToClient(new NotAvailableColorMessage(color));
    }

    /**
     * Notifies the client about modified tiles.
     *
     * @param playerName The name of the player.
     * @param tiles The list of modified tiles.
     * @throws RemoteException If there is an error during the remote communication.
     */
    @Override
    public void notifyModifiedTiles(String playerName, ArrayList<Tile> tiles) throws RemoteException {
        clientHandler.sendServerMessageToClient(new ModifiedTilesMessage(playerName, tiles));
    }

    /**
     * Notifies the client about credits gained by a player.
     *
     * @param playerName The name of the player.
     * @param totalCredits The total credits gained by the player.
     * @throws RemoteException If there is an error during the remote communication.
     */
    @Override
    public void notifyGainedCredits(String playerName, int totalCredits) throws RemoteException {
        clientHandler.sendServerMessageToClient(new GainedCreditsMessage(playerName, totalCredits));
    }

    /**
     * Notifies the client about a broken tile on the ship.
     *
     * @param playerName The name of the player.
     * @param coordinates The coordinates of the broken tile.
     * @throws RemoteException If there is an error during the remote communication.
     */
    @Override
    public void notifyBrokenTile(String playerName, ArrayList<Coordinates> coordinates) throws RemoteException {
        clientHandler.sendServerMessageToClient(new BrokenTileMessage(playerName, coordinates));
    }

    /**
     * Notifies the client about changes in the game status while the client was gone.
     *
     * @param currentGameStatus The current status of the game.
     * @throws RemoteException If there is an error during the remote communication.
     */
    @Override
    public void notifyChangesWhileGone(String currentGameStatus) throws RemoteException {
        clientHandler.sendServerMessageToClient(new CurrentGameStatusMessage(currentGameStatus));
    }

    /**
     * Notifies the client about the flight board cards.
     *
     * @param cards The map of flight board cards.
     * @throws RemoteException If there is an error during the remote communication.
     */
    @Override
    public void notifyFlightBoardCards(Map<Integer, ArrayList<Card>> cards) throws RemoteException {
        clientHandler.sendServerMessageToClient(new FlightBoardCardsMessage(cards));
    }

    /**
     * Initializes the ship boards based on the game mode.
     *
     * @param gameMode The game mode to be used for initialization.
     * @throws RemoteException If there is an error during the remote communication.
     */
    @Override
    public void initializeShipBoards(GameMode gameMode) throws RemoteException {
        clientHandler.sendServerMessageToClient(new SetGameModeMessage(gameMode));
    }

    /**
     * Notifies the client that they are the victim of a penalty.
     *
     * @param playerName The name of the player.
     * @throws RemoteException If there is an error during the remote communication.
     */
    @Override
    public void victimOfThePenalty(String playerName) throws RemoteException {
        clientHandler.sendServerMessageToClient(new VictimOfThePenaltyMessage(playerName));
    }

    /**
     * Responds to the client with the result of their login attempt.
     *
     * @param success The result of the login attempt.
     * @throws RemoteException If there is an error during the remote communication.
     */
    @Override
    public void showLoginResponse(boolean success) throws RemoteException {
        clientHandler.sendServerMessageToClient(new LoginResponseMessage(success));
    }

    /**
     * Sets the client state in the view.
     *
     * @param newState The new state of the client.
     * @throws RemoteException If there is an error during the remote communication.
     */
    @Override
    public void setClientState(ClientState newState) throws RemoteException {
        clientHandler.sendServerMessageToClient(new SetClientStateMessage(newState));
    }

    /**
     * Shows a list of joinable games to the client.
     *
     * @param joinableGames The list of joinable games.
     * @throws RemoteException If there is an error during the remote communication.
     */
    @Override
    public void showJoinableGamesList(ArrayList<GameInfo> joinableGames) throws RemoteException {
        clientHandler.sendServerMessageToClient(new ShowJoinableGamesMessage(joinableGames));
    }

    /**
     * Shows an error message to the client.
     *
     * @param errorMessage The error message to be displayed.
     * @throws RemoteException If there is an error during the remote communication.
     */
    @Override
    public void showErrorMessage(String errorMessage) throws RemoteException {
        clientHandler.sendServerMessageToClient(new ErrorMessage(errorMessage));
    }

    /**
     * Shows a drawn tile to the client.
     *
     * @param drawnTile The tile that has been drawn.
     * @throws RemoteException If there is an error during the remote communication.
     */
    @Override
    public void showDrawnTile(Tile drawnTile) throws RemoteException {
        clientHandler.sendServerMessageToClient(new ShowDrawnTileMessage(drawnTile));
    }

    /**
     * Shows a wrong input message to the client.
     *
     * @throws RemoteException If there is an error during the remote communication.
     */
    @Override
    public void showWrongInputMessage() throws RemoteException {
        clientHandler.sendServerMessageToClient(new WrongInputMessage());
    }

    /**
     * Requests the client to roll the dice.
     *
     * @throws RemoteException If there is an error during the remote communication.
     */
    @Override
    public void asksToRollTheDices() throws RemoteException {
        clientHandler.sendServerMessageToClient(new AskToRollTheDicesMessage());
    }

    /**
     * Shows the result of the dice roll to the client.
     *
     * @param diceRoll The result of the dice roll.
     * @throws RemoteException If there is an error during the remote communication.
     */
    @Override
    public void showDiceRoll(int diceRoll) throws RemoteException {
        clientHandler.sendServerMessageToClient(new ShowDiceRollMessage(diceRoll));
    }

    /**
     * Asks the client to choose their starting position.
     *
     * @throws RemoteException If there is an error during the remote communication.
     */
    @Override
    public void asksToChooseStartingPosition() throws RemoteException {
        clientHandler.sendServerMessageToClient(new AskToChooseStartingPositionMessage());
    }

    /**
     * Asks the client to input coordinates based on the coordinate request type.
     *
     * @param coordReqType The type of coordinate request.
     * @throws RemoteException If there is an error during the remote communication.
     */
    @Override
    public void asksToInputCoordinates(CoordReqType coordReqType) throws RemoteException {
        clientHandler.sendServerMessageToClient(new AskToInputCoordinatesMessage(coordReqType));
    }

    /**
     * Notifies the client that they can now draw a card from a specific deck.
     *
     * @throws RemoteException If there is an error during the remote communication.
     */
    @Override
    public void notifyYouCanDrawThisCardDeck() throws RemoteException {
        clientHandler.sendServerMessageToClient(new FlightboardCardsResponse());
    }

    /**
     * Notifies the client that their ship is correct.
     *
     * @throws RemoteException If there is an error during the remote communication.
     */
    @Override
    public void notifyYourShipIsCorrect() throws RemoteException {
        clientHandler.sendServerMessageToClient(new ShipIsCorrectMessage());
    }

    /**
     * Asks the client to make a choice.
     *
     * @throws RemoteException If there is an error during the remote communication.
     */
    @Override
    public void asksToMakeAChoice() throws RemoteException {

    }

    /**
     * Shows the final scores to the client.
     *
     * @param scores The map of player names and their final scores.
     * @throws RemoteException If there is an error during the remote communication.
     */
    @Override
    public void showScores(Map<String, Integer> scores) throws RemoteException {
        clientHandler.sendServerMessageToClient(new FinalScoresMessage(scores));
    }

    /**
     * Notifies the client that a card has been drawn.
     *
     * @param card The card that has been drawn.
     * @throws RemoteException If there is an error during the remote communication.
     */
    @Override
    public void notifyDrawnCard(Card card) throws RemoteException {
        clientHandler.sendServerMessageToClient(new DrawnCardMessage(card));
    }

    /**
     * Notifies the client that a player has landed on a planet.
     *
     * @param playerName The name of the player.
     * @param planet The number of the planet that the player landed on.
     * @throws RemoteException If there is an error during the remote communication.
     */
    @Override
    public void notifyPlayerLandedOnPlanet(String playerName, int planet) throws RemoteException {
        clientHandler.sendServerMessageToClient(new PlayerLandedOnPlanetMessage(playerName, planet));
    }

    /**
     * Notifies the client that they are connected to the server.
     *
     * @throws RemoteException If there is an error during the remote communication.
     */
    @Override
    public void connected() throws RemoteException {

    }

    /**
     * Sets the game mode for the client.
     *
     * @param gameMode The game mode to be set.
     * @throws RemoteException If there is an error during the remote communication.
     */
    @Override
    public void setGameMode(GameMode gameMode) throws RemoteException {
        clientHandler.sendServerMessageToClient(new SetGameModeMessage(gameMode));
    }

    /**
     * Sends a ping message to the client.
     *
     * @throws RemoteException If there is an error during the remote communication.
     */
    @Override
    public void ping() throws RemoteException {
        clientHandler.sendServerMessageToClient(new ServerPingMessage());
    }

    /**
     * Notifies the client that the hourglass has been turned.
     *
     * @param turns The number of turns that the hourglass has been turned.
     * @throws RemoteException If there is an error during the remote communication.
     */
    @Override
    public void notifyTurnedHourglass(int turns) throws RemoteException {
        clientHandler.sendServerMessageToClient(new TurnedHourglassMessage(turns));
    }

    /**
     * Notifies the client that the time has expired.
     *
     * @throws RemoteException If there is an error during the remote communication.
     */
    @Override
    public void notifyEndOfTime() throws RemoteException {
        clientHandler.sendServerMessageToClient(new TimeExpiredMessage());
    }

    /**
     * Notifies the client about early landing.
     *
     * @throws RemoteException If there is an error during the remote communication.
     */
    @Override
    public void notifyEarlyLanding() throws RemoteException {
        clientHandler.sendServerMessageToClient(new EarlyLandingMessage());
    }

    /**
     * Notifies the client about combat zone strength.
     *
     * @param playerName The name of the player.
     * @param strength The strength of the combat zone.
     * @throws RemoteException If there is an error during the remote communication.
     */
    @Override
    public void notifyCombatZoneStrength(String playerName, float strength) throws RemoteException {
        clientHandler.sendServerMessageToClient(new CombatZoneStrengthMessage(playerName, strength));
    }

    /**
     * Notifies the client about combat zone engine strength.
     *
     * @param playerName The name of the player.
     * @param strength The engine strength.
     * @throws RemoteException If there is an error during the remote communication.
     */
    @Override
    public void notifyCombatZoneEngine(String playerName, float strength) throws RemoteException {
        clientHandler.sendServerMessageToClient(new NotifyCombatZoneEngineMessage(playerName, strength));
    }

    /**
     * Notifies the client about the combat zone crew strength.
     *
     * @param playerName The name of the player.
     * @param crew The crew strength.
     * @throws RemoteException If there is an error during the remote communication.
     */
    @Override
    public void notifyCombatZoneCrew(String playerName, int crew) throws RemoteException {
        clientHandler.sendServerMessageToClient(new NotifyCombatZoneCrewMessage(playerName, crew));
    }

    /**
     * Notifies the client about the podium standings.
     *
     * @param players The list of players in the podium.
     * @throws RemoteException If there is an error during the remote communication.
     */
    @Override
    public void notifyPodium(ArrayList<Player> players) throws RemoteException {
        clientHandler.sendServerMessageToClient(new PodiumMessage(players));
    }

    /**
     * Returns the displayed view.
     *
     * @return The displayed view.
     * @throws RemoteException If there is an error during the remote communication.
     */
    @Override
    public DisplayableView getDisplayedView() throws RemoteException {
        return null;
    }

    /**
     * Notifies the client that a player has joined the game.
     *
     * @param expected The expected number of players.
     * @param current The current number of players.
     * @param reconnected A flag indicating whether the player is reconnecting.
     * @throws RemoteException If there is an error during the remote communication.
     */
    @Override
    public void notifyPlayerJoined(int expected, int current, boolean reconnected) throws RemoteException {
        clientHandler.sendServerMessageToClient(new PlayerJoinedMessage(expected, current, reconnected));
    }

    /**
     * Notifies the clients when a player leaves the game sending PlayerLeftMessage server message
     *
     * @param playerName The name of the player.
     * @throws RemoteException If a remote exception occurs.
     */
    @Override
    public void notifyPlayerLeft(String playerName) throws RemoteException {
        clientHandler.sendServerMessageToClient(new PlayerLeavesMessage(playerName));
    }

    /**
     * Notifies the clients when a player disconnects sending a PlayerDisconnectedMessage server message
     *
     * @param playerName The name of the player
     * @throws RemoteException If a remote exception occurs.
     */
    @Override
    public void notifyPlayerDisconnected(String playerName) throws RemoteException {
        clientHandler.sendServerMessageToClient(new PlayerDisconnectedMessage(playerName));
    }
}
