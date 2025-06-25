package it.polimi.ingsw.galaxytruckerproject.network.RMI.Client;

import it.polimi.ingsw.galaxytruckerproject.client.ClientController;
import it.polimi.ingsw.galaxytruckerproject.client.ClientState;
import it.polimi.ingsw.galaxytruckerproject.client.CoordReqType;
import it.polimi.ingsw.galaxytruckerproject.model.GameInfo;
import it.polimi.ingsw.galaxytruckerproject.model.GameMode;
import it.polimi.ingsw.galaxytruckerproject.model.cards.Card;
import it.polimi.ingsw.galaxytruckerproject.model.player.Player;
import it.polimi.ingsw.galaxytruckerproject.model.player.PlayersColor;
import it.polimi.ingsw.galaxytruckerproject.model.tiles.Coordinates;
import it.polimi.ingsw.galaxytruckerproject.model.tiles.Tile;
import it.polimi.ingsw.galaxytruckerproject.network.VirtualView;
import it.polimi.ingsw.galaxytruckerproject.view.DisplayableView;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Map;
import java.util.Scanner;

/**
 * RMI implementation of the VirtualView interface.
 * This class communicates with the client controller and updates the client view based on the game state.
 */
public class VirtualViewRMI extends UnicastRemoteObject implements VirtualView, Remote {

    /** The client controller managing the game logic */
    private final ClientController clientController;

    /** The view to be updated with the game state */
    private final DisplayableView view;

    /**
     * Constructs a VirtualViewRMI object.
     *
     * @param clientController The client controller that manages the game logic.
     * @param view The view to be updated with the game state.
     * @throws RemoteException If there is a remote communication issue.
     */
    public VirtualViewRMI(ClientController clientController, DisplayableView view) throws RemoteException {
        super();
        this.clientController = clientController;
        this.view = view;
    }

    /**
     * Runs the command-line interface (CLI) for the client.
     * Reads commands and processes them.
     *
     * @throws RemoteException If there is a remote communication issue.
     */
    public void runCli() throws RemoteException {
        Scanner scan = new Scanner(System.in);
        while (true) {
            System.out.print(">  ");
            int command = scan.nextInt();
            // Command processing logic to be adapted as per the incoming commands.
        }
    }

    /**
     * Shows the result of the login attempt to the client.
     *
     * @param success Whether the login attempt was successful.
     * @throws RemoteException If there is a remote communication issue.
     */
    @Override
    public void showLoginResponse(boolean success) throws RemoteException {
        view.showLoginResponse(success);
    }

    /**
     * Sets the current state of the client.
     *
     * @param newState The new client state.
     * @throws RemoteException If there is a remote communication issue.
     */
    @Override
    public void setClientState(ClientState newState) throws RemoteException {
        clientController.setState(newState);
    }

    /**
     * Displays the list of joinable games to the client.
     *
     * @param joinableGames List of joinable games.
     * @throws RemoteException If there is a remote communication issue.
     */
    @Override
    public void showJoinableGamesList(ArrayList<GameInfo> joinableGames) throws RemoteException {
        clientController.setGameInfo(joinableGames);
        view.showJoinableGamesList(joinableGames);
    }

    /**
     * Displays an error message to the client.
     *
     * @param errorMessage The error message to display.
     * @throws RemoteException If there is a remote communication issue.
     */
    @Override
    public void showErrorMessage(String errorMessage) throws RemoteException {
        view.showErrorMessage(errorMessage);
    }

    /**
     * Displays a drawn tile to the client.
     *
     * @param drawnTile The tile that was drawn.
     * @throws RemoteException If there is a remote communication issue.
     */
    @Override
    public void showDrawnTile(Tile drawnTile) throws RemoteException {
        view.showDrawnTile(drawnTile);
        clientController.setTileInHand(drawnTile);
    }

    /**
     * Notifies the client about a new turned tile.
     *
     * @param tile The new turned tile.
     * @throws RemoteException If there is a remote communication issue.
     */
    public void notifyNewTurnedTile(Tile tile) throws RemoteException {
        clientController.addTurnedTile(tile);
    }

    /**
     * Notifies the client that a turned tile has been removed.
     *
     * @param tile The tile that was removed.
     * @throws RemoteException If there is a remote communication issue.
     */
    @Override
    public void notifyRemoveTurnedTile(Tile tile) throws RemoteException {
        clientController.removeTurnedTile(tile);
    }

    /**
     * Notifies the client that a player has positioned a tile.
     *
     * @param playerName The name of the player.
     * @param tile The tile that was positioned.
     * @throws RemoteException If there is a remote communication issue.
     */
    @Override
    public void notifyPositionedTile(String playerName, Tile tile) throws RemoteException {
        clientController.setTile(playerName, tile);
    }

    /**
     * Notifies the client that a player has booked a tile.
     *
     * @param playerName The name of the player.
     * @param tile The tile that was booked.
     * @throws RemoteException If there is a remote communication issue.
     */
    @Override
    public void notifyBookedTile(String playerName, Tile tile) throws RemoteException {
        clientController.addBookedTile(playerName, tile);
    }

    /**
     * Notifies the client that a booked tile has been removed.
     *
     * @param playerName The name of the player.
     * @param tile The tile that was removed.
     * @throws RemoteException If there is a remote communication issue.
     */
    @Override
    public void notifyRemovedBookedTile(String playerName, Tile tile) throws RemoteException {
        clientController.removeBookedTile(playerName, tile);
    }

    /**
     * Notifies the client that some card decks are not available.
     *
     * @param lockedSmallDecks The list of unavailable decks.
     * @throws RemoteException If there is a remote communication issue.
     */
    @Override
    public void notifyNotAvailableCardDeck(ArrayList<Integer> lockedSmallDecks) throws RemoteException {
        clientController.decksNotAvailable(lockedSmallDecks);
    }

    /**
     * Notifies the client that some colors are not available.
     *
     * @param color The list of unavailable colors.
     * @throws RemoteException If there is a remote communication issue.
     */
    @Override
    public void notifyNotAvailableColor(ArrayList<PlayersColor> color) throws RemoteException {
        clientController.colorsNotAvailable(color);
    }

    /**
     * Notifies the client that a card was drawn.
     *
     * @param card The drawn card.
     * @throws RemoteException If there is a remote communication issue.
     */
    @Override
    public void notifyDrawnCard(Card card) throws RemoteException {
        clientController.setDisplayedCard(card);
        view.notifyDrawnCard(card);
        clientController.setState(ClientState.WAIT);
    }

    /**
     * Notifies the client that a player has landed on a planet.
     *
     * @param playerName The name of the player.
     * @param planet The number of the planet.
     * @throws RemoteException If there is a remote communication issue.
     */
    @Override
    public void notifyPlayerLandedOnPlanet(String playerName, int planet) throws RemoteException {
        clientController.setPlanets(planet);
        view.notifyPlayerLandedOnPlanet(playerName, planet);
    }

    /**
     * Notifies the client that the connection has been established.
     *
     * @throws RemoteException If there is a remote communication issue.
     */
    @Override
    public void connected() throws RemoteException {
        view.connected();
        clientController.setConnected(true);
    }

    /**
     * Initializes the ship boards with the specified game mode.
     *
     * @param gameMode The game mode to be used.
     * @throws RemoteException If there is a remote communication issue.
     */
    @Override
    public void initializeShipBoards(GameMode gameMode) throws RemoteException {
        clientController.setGameMode(gameMode);
    }

    /**
     * Sets the game mode and updates the client.
     *
     * @param gameMode The game mode to be set.
     * @throws RemoteException If there is a remote communication issue.
     */
    @Override
    public void setGameMode(GameMode gameMode) throws RemoteException {
        view.setGameMode(gameMode);
        clientController.setGameMode(gameMode);
    }

    /**
     * Sends a ping to the server.
     *
     * @throws RemoteException If there is a remote communication issue.
     */
    @Override
    public void ping() throws RemoteException {
        clientController.ping();
    }

    /**
     * Notifies the client that the hourglass has turned.
     *
     * @param i The number of turns.
     * @throws RemoteException If there is a remote communication issue.
     */
    @Override
    public void notifyTurnedHourglass(int i) throws RemoteException {
        view.notifyTurnedHourglass(i);
        clientController.turnHourglass(i);
    }

    /**
     * Notifies the client that the time has expired.
     *
     * @throws RemoteException If there is a remote communication issue.
     */
    @Override
    public void notifyEndOfTime() throws RemoteException {
        view.notifyEndOfTime();
    }

    /**
     * Notifies the client that the player is landing early.
     *
     * @throws RemoteException If there is a remote communication issue.
     */
    @Override
    public void notifyEarlyLanding() throws RemoteException {
        view.notifyEarlyLanding();
        clientController.setState(ClientState.LANDING);
    }

    /**
     * Notifies the client about the combat zone strength for a player.
     *
     * @param playerName The name of the player.
     * @param strength The combat zone strength.
     * @throws RemoteException If there is a remote communication issue.
     */
    @Override
    public void notifyCombatZoneStrength(String playerName, float strength) throws RemoteException {
        view.notifyCombatZoneStrength(playerName, strength);
    }

    /**
     * Notifies the client about the combat zone engine strength for a player.
     *
     * @param playerName The name of the player.
     * @param strength The engine strength.
     * @throws RemoteException If there is a remote communication issue.
     */
    @Override
    public void notifyCombatZoneEngine(String playerName, float strength) throws RemoteException {
        view.notifyCombatZoneEngine(playerName, strength);
    }

    /**
     * Notifies the client about the combat zone crew for a player.
     *
     * @param playerName The name of the player.
     * @param crew The crew count.
     * @throws RemoteException If there is a remote communication issue.
     */
    @Override
    public void notifyCombatZoneCrew(String playerName, int crew) throws RemoteException {
        view.notifyCombatZoneCrew(playerName, crew);
    }

    /**
     * Notifies the client about the podium, i.e., the ranking of players.
     *
     * @param players The list of players in the podium.
     * @throws RemoteException If there is a remote communication issue.
     */
    @Override
    public void notifyPodium(ArrayList<Player> players) throws RemoteException {
        view.notifyPodium(players);
    }

    /**
     * Notifies the client that the player is a victim of a penalty.
     *
     * @param playerName The name of the player.
     * @throws RemoteException If there is a remote communication issue.
     */
    @Override
    public void victimOfThePenalty(String playerName) throws RemoteException {
        clientController.victimOfThePenalty(playerName);
    }

    /**
     * Returns the displayed view.
     *
     * @return The displayed view.
     * @throws RemoteException If there is a remote communication issue.
     */
    @Override
    public DisplayableView getDisplayedView() throws RemoteException {
        return view;
    }

    /**
     * Notifies the client about modified tiles.
     *
     * @param playerName The name of the player.
     * @param tiles The list of modified tiles.
     * @throws RemoteException If there is a remote communication issue.
     */
    @Override
    public void notifyModifiedTiles(String playerName, ArrayList<Tile> tiles) throws RemoteException {
        clientController.modifyTiles(playerName, tiles);
        if (playerName.equals(clientController.getName())) {
            view.printShipboard(clientController.getLightShipBoard());
        }
    }

    /**
     * Notifies the client about the credits gained by a player.
     *
     * @param playerName The name of the player.
     * @param totalCredits The total credits gained.
     * @throws RemoteException If there is a remote communication issue.
     */
    @Override
    public void notifyGainedCredits(String playerName, int totalCredits) throws RemoteException {
        clientController.gainCredit(playerName, totalCredits);
    }

    /**
     * Notifies the client about a player's movement in the game.
     *
     * @param playerName The name of the player.
     * @param color The color of the player.
     * @param playerPosition The player's position.
     * @param playerRanking The player's ranking.
     * @throws RemoteException If there is a remote communication issue.
     */
    @Override
    public void notifyPlayerMovement(String playerName, PlayersColor color, int playerPosition, int playerRanking) throws RemoteException {
        clientController.updateFlightboard(playerName, color, playerPosition, playerRanking);
    }

    /**
     * Displays the dice roll result to the client.
     *
     * @param diceRoll The result of the dice roll.
     * @throws RemoteException If there is a remote communication issue.
     */
    @Override
    public void showDiceRoll(int diceRoll) throws RemoteException {
        view.showDiceRoll(diceRoll);
    }

    /**
     * Handles incorrect input and notifies the client.
     *
     * @throws RemoteException If there is a remote communication issue.
     */
    @Override
    public void showWrongInputMessage() throws RemoteException {
        clientController.rollBackState();
        view.showWrongInputMessage();
    }

    /**
     * Asks the client to roll the dice.
     *
     * @throws RemoteException If there is a remote communication issue.
     */
    @Override
    public void asksToRollTheDices() throws RemoteException {
        view.asksToRollTheDices();
    }

    /**
     * Asks the client to choose their starting position.
     *
     * @throws RemoteException If there is a remote communication issue.
     */
    @Override
    public void asksToChooseStartingPosition() throws RemoteException {
        view.asksToChooseStartingPosition();
    }

    /**
     * Asks the client to input coordinates based on the requested type.
     *
     * @param coordReqType The type of coordinate request.
     * @throws RemoteException If there is a remote communication issue.
     */
    @Override
    public void asksToInputCoordinates(CoordReqType coordReqType) throws RemoteException {
        clientController.setState(ClientState.COORD_REQUEST);
        clientController.getCoordInputManager().setCoordReqType(coordReqType);
        view.asksToInputCoordinates(coordReqType);
    }

    /**
     * Notifies the client about broken tiles.
     *
     * @param playerName The name of the player.
     * @param coordinates The coordinates of the broken tiles.
     * @throws RemoteException If there is a remote communication issue.
     */
    @Override
    public void notifyBrokenTile(String playerName, ArrayList<Coordinates> coordinates) throws RemoteException {
        clientController.brokenTiles(playerName, coordinates);
        if (playerName.equals(clientController.getName())) {
            view.printShipboard(clientController.getLightShipBoard());
        }
    }

    /**
     * Notifies the client about changes that occurred while the client was disconnected.
     *
     * @param currentGameStatus The current game status.
     * @throws RemoteException If there is a remote communication issue.
     */
    @Override
    public void notifyChangesWhileGone(String playerName,String currentGameStatus) throws RemoteException {
        clientController.updateModel(playerName,currentGameStatus);
    }

    /**
     * Notifies the client about the flight board cards.
     *
     * @param cards The map of flight board cards.
     * @throws RemoteException If there is a remote communication issue.
     */
    @Override
    public void notifyFlightBoardCards(Map<Integer, ArrayList<Card>> cards) throws RemoteException {
        clientController.setDeck(cards);
    }

    /**
     * Notifies the client that they can now draw a card from the deck.
     *
     * @throws RemoteException If there is a remote communication issue.
     */
    @Override
    public void notifyYouCanDrawThisCardDeck() throws RemoteException {
        view.showDeck(clientController.getDisplayedCard());
    }

    /**
     * Notifies the client that their ship is correct.
     *
     * @throws RemoteException If there is a remote communication issue.
     */
    @Override
    public void notifyYourShipIsCorrect() throws RemoteException {
        view.notifyYourShipIsCorrect();
    }

    /**
     * Asks the client to make a choice.
     *
     * @throws RemoteException If there is a remote communication issue.
     */
    @Override
    public void asksToMakeAChoice() throws RemoteException {
        view.asksToMakeAChoice();
        setClientState(ClientState.ACTION);
    }

    /**
     * Displays the scores to the client.
     *
     * @param scores The map of players and their scores.
     * @throws RemoteException If there is a remote communication issue.
     */
    @Override
    public void showScores(Map<String, Integer> scores) throws RemoteException {
        view.showScores(scores);
    }

    /**
     * Notifies the client when a player joins the game.
     *
     * @param expected The expected number of players.
     * @param current The current number of players.
     * @param reconnected Whether the player reconnected or joined for the first time.
     * @throws RemoteException If there is a remote communication issue.
     */
    @Override
    public void notifyPlayerJoined(int expected, int current, boolean reconnected) throws RemoteException {
        view.notifyPlayerJoined(expected, current, reconnected);
    }

    /**
     * Notifies the clients when a player leaves the game.
     *
     * @param playerName The name of the player.
     * @throws RemoteException If a remote exception occurs.
     */
    @Override
    public void notifyPlayerLeft(String playerName) throws RemoteException {
        view.notifyPlayerLeftTheGame(playerName);
    }

    /**
     * Notifies the clients when a player disconnects sending.
     *
     * @param playerName The name of the player
     * @throws RemoteException If a remote exception occurs.
     */
    @Override
    public void notifyPlayerDisconnected(String playerName) throws RemoteException {
        view.notifyPlayerDisconnected(playerName);
    }
}