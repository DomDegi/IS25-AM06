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

public interface ViewInterface extends Remote, Serializable {
    /**
     * show login response from server
     * @param success
         */
    void showLoginResponse(boolean success) throws RemoteException;

    void setClientState (ClientState newState) throws RemoteException;
    /**
     * Shows on the view the list of the games that are in starting phase.
     * Also asks player calls for the function asksJoinOrCreate().
     */
    void showJoinableGamesList(ArrayList<GameInfo> joinableGames) throws RemoteException;

    /**
     * shows on the view a generic message sent by the server
     * @param genericMessage the message to be shown
     */

    /**
     * shows on the view a error message sent by the server
     * @param errorMessage the message to be shown.
     */
    void showErrorMessage (String errorMessage) throws RemoteException;
    /**
     * show the drawnTile to the player
     * @param drawnTile
     */
    //DEVE ANCHE MANDARLA AL CLIENT
    void showDrawnTile (Tile drawnTile) throws RemoteException;
    void showWrongInputMessage () throws RemoteException;
    void asksToRollTheDices()throws RemoteException;//asks the player to confirm he wants to roll the dices
    void showDiceRoll(int diceRoll)throws RemoteException;
    /**
     * asks the player to choose a starting position from 1 to playerCount
     */
    void asksToChooseStartingPosition () throws RemoteException;

    /**
     * asks the player to input a series of coordinates of coord
     */
    void asksToInputCoordinates (CoordReqType coordReqType) throws RemoteException;
    void notifyYouCanDrawThisCardDeck() throws RemoteException;
    void notifyYourShipIsCorrect()throws RemoteException;//need to understand if is usefull
    /**
     * asks to set position on the flightboard
     */
    void asksToMakeAChoice () throws RemoteException;//need to understand if is usefull
    /**
     * at the end of the game shows every players score on the view
     */
    void showScores(Map<String,Integer> scores) throws RemoteException;
    /**
     * asks the player to choose the mode of the game to be created
     */
    void notifyDrawnCard(Card card) throws RemoteException; //tell the players which card has been drawn
    void notifyPlayerLandedOnPlanet(String playerName, int planet)  throws RemoteException;
    void connected() throws RemoteException;//need to understand if is usefull
    void setGameMode(GameMode gameMode) throws RemoteException;
    void notifyTurnedHourglass( int i) throws RemoteException;
    void notifyEndOfTime() throws RemoteException;
    void notifyEarlyLanding() throws RemoteException;
    void notifyCombatZoneStrength(String playerName, float strength) throws RemoteException;
    void notifyCombatZoneEngine(String playerName, float strength) throws RemoteException;
    void notifyCombatZoneCrew(String playerName, int crew) throws RemoteException;
    void notifyPodium(ArrayList<Player> players) throws RemoteException;

    //METHOD NECESSARY ONLY FOR TESTING
    DisplayableView getDisplayedView()throws RemoteException;

}
