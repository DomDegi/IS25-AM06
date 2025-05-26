package it.polimi.ingsw.galaxytruckerproject.view;

import it.polimi.ingsw.galaxytruckerproject.client.ClientState;
import it.polimi.ingsw.galaxytruckerproject.client.CoordReqType;
import it.polimi.ingsw.galaxytruckerproject.model.GameInfo;
import it.polimi.ingsw.galaxytruckerproject.model.GameMode;
import it.polimi.ingsw.galaxytruckerproject.model.cards.Card;
import it.polimi.ingsw.galaxytruckerproject.model.player.Player;
import it.polimi.ingsw.galaxytruckerproject.model.tiles.Tile;

import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Map;

/**
 * Interface for the view in the game, which handles all interactions and updates the client with the game's state.
 * All methods in this interface are remotely called to update the view from the server.
 */
public interface ViewInterface extends Remote, Serializable {

    /**
     * Displays the result of the login attempt.
     *
     * @param success indicates whether the login was successful or not.
     * @throws RemoteException if there is an issue with remote communication.
     */
    void showLoginResponse(boolean success) throws RemoteException;

    /**
     * Updates the current state of the client.
     *
     * @param newState the new state to set for the client.
     * @throws RemoteException if there is an issue with remote communication.
     */
    void setClientState(ClientState newState) throws RemoteException;

    /**
     * Displays the list of games available to join during the starting phase.
     * Prompts the player to choose whether to join an existing game or create a new one.
     *
     * @param joinableGames list of games that can be joined.
     * @throws RemoteException if there is an issue with remote communication.
     */
    void showJoinableGamesList(ArrayList<GameInfo> joinableGames) throws RemoteException;

    /**
     * Displays an error message from the server to the player.
     *
     * @param errorMessage the error message to display.
     * @throws RemoteException if there is an issue with remote communication.
     */
    void showErrorMessage(String errorMessage) throws RemoteException;

    /**
     * Displays a tile drawn by the player.
     *
     * @param drawnTile the tile that was drawn.
     * @throws RemoteException if there is an issue with remote communication.
     */
    void showDrawnTile(Tile drawnTile) throws RemoteException;

    /**
     * Displays a message indicating that the player entered a wrong input.
     *
     * @throws RemoteException if there is an issue with remote communication.
     */
    void showWrongInputMessage() throws RemoteException;

    /**
     * Asks the player to confirm whether they want to roll the dice.
     *
     * @throws RemoteException if there is an issue with remote communication.
     */
    void asksToRollTheDices() throws RemoteException;

    /**
     * Displays the result of the dice roll.
     *
     * @param diceRoll the value of the dice roll.
     * @throws RemoteException if there is an issue with remote communication.
     */
    void showDiceRoll(int diceRoll) throws RemoteException;

    /**
     * Prompts the player to choose a starting position from 1 to the player count.
     *
     * @throws RemoteException if there is an issue with remote communication.
     */
    void asksToChooseStartingPosition() throws RemoteException;

    /**
     * Asks the player to input coordinates for a specific action, based on the request type.
     *
     * @param coordReqType the type of coordinate request (e.g., movement or action).
     * @throws RemoteException if there is an issue with remote communication.
     */
    void asksToInputCoordinates(CoordReqType coordReqType) throws RemoteException;

    /**
     * Notifies the player that they can draw a card from the deck.
     *
     * @throws RemoteException if there is an issue with remote communication.
     */
    void notifyYouCanDrawThisCardDeck() throws RemoteException;

    /**
     * Notifies the player that their ship configuration is correct.
     *
     * @throws RemoteException if there is an issue with remote communication.
     */
    void notifyYourShipIsCorrect() throws RemoteException;

    /**
     * Asks the player to make a choice on the flightboard.
     *
     * @throws RemoteException if there is an issue with remote communication.
     */
    void asksToMakeAChoice() throws RemoteException;

    /**
     * Displays the final scores of all players at the end of the game.
     *
     * @param scores a map of player names to their final scores.
     * @throws RemoteException if there is an issue with remote communication.
     */
    void showScores(Map<String, Integer> scores) throws RemoteException;

    /**
     * Notifies the player of the card that has been drawn.
     *
     * @param card the card that was drawn.
     * @throws RemoteException if there is an issue with remote communication.
     */
    void notifyDrawnCard(Card card) throws RemoteException;

    /**
     * Notifies the player that another player has landed on a planet.
     *
     * @param playerName the name of the player who landed.
     * @param planet the planet on which the player landed.
     * @throws RemoteException if there is an issue with remote communication.
     */
    void notifyPlayerLandedOnPlanet(String playerName, int planet) throws RemoteException;

    /**
     * Confirms that the client is connected to the server.
     *
     * @throws RemoteException if there is an issue with remote communication.
     */
    void connected() throws RemoteException;

    /**
     * Sets the game mode for the current game session.
     *
     * @param gameMode the game mode to be set.
     * @throws RemoteException if there is an issue with remote communication.
     */
    void setGameMode(GameMode gameMode) throws RemoteException;

    /**
     * Notifies the player that the hourglass has turned (time is up for the current phase).
     *
     * @param i the current round or phase number.
     * @throws RemoteException if there is an issue with remote communication.
     */
    void notifyTurnedHourglass(int i) throws RemoteException;

    /**
     * Notifies the player that time has run out for the current phase.
     *
     * @throws RemoteException if there is an issue with remote communication.
     */
    void notifyEndOfTime() throws RemoteException;

    /**
     * Notifies the player that early landing has occurred.
     *
     * @throws RemoteException if there is an issue with remote communication.
     */
    void notifyEarlyLanding() throws RemoteException;

    /**
     * Notifies the player of the combat zone strength for a given player.
     *
     * @param playerName the name of the player.
     * @param strength the strength value for the combat zone.
     * @throws RemoteException if there is an issue with remote communication.
     */
    void notifyCombatZoneStrength(String playerName, float strength) throws RemoteException;

    /**
     * Notifies the player of the combat zone engine strength for a given player.
     *
     * @param playerName the name of the player.
     * @param strength the engine strength value for the combat zone.
     * @throws RemoteException if there is an issue with remote communication.
     */
    void notifyCombatZoneEngine(String playerName, float strength) throws RemoteException;

    /**
     * Notifies the player of the combat zone crew strength for a given player.
     *
     * @param playerName the name of the player.
     * @param crew the crew strength for the combat zone.
     * @throws RemoteException if there is an issue with remote communication.
     */
    void notifyCombatZoneCrew(String playerName, int crew) throws RemoteException;

    /**
     * Displays the podium with the players' rankings at the end of the game.
     *
     * @param players the list of players ranked by their final scores.
     * @throws RemoteException if there is an issue with remote communication.
     */
    void notifyPodium(ArrayList<Player> players) throws RemoteException;

    /**
     * Provides the view that is currently being displayed to the user.
     * This method is only necessary for testing purposes.
     *
     * @return the current view displayed to the user.
     * @throws RemoteException if there is an issue with remote communication.
     */
    DisplayableView getDisplayedView() throws RemoteException;

}
