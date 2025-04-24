package it.polimi.ingsw.galaxytruckerproject.view;

import it.polimi.ingsw.galaxytruckerproject.client.ClientState;
import it.polimi.ingsw.galaxytruckerproject.client.CoordReqType;
import it.polimi.ingsw.galaxytruckerproject.controller.GameController;
import it.polimi.ingsw.galaxytruckerproject.lightmodel.LightFlightboard;
import it.polimi.ingsw.galaxytruckerproject.lightmodel.LightShipBoard;
import it.polimi.ingsw.galaxytruckerproject.model.cards.Card;
import it.polimi.ingsw.galaxytruckerproject.model.cards.projectiles.Projectile;
import it.polimi.ingsw.galaxytruckerproject.model.goods.Goods;
import it.polimi.ingsw.galaxytruckerproject.model.player.Player;
import it.polimi.ingsw.galaxytruckerproject.model.tiles.ShipBoard;
import it.polimi.ingsw.galaxytruckerproject.model.tiles.Tile;

import java.io.IOException;
import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Map;

public interface ViewInterface extends Remote, Serializable {
    /**
     * asks the player to set a nickname
     * @throws IOException
     */
    void askNickname () throws IOException;
    /**
     * asks player to set a color for the starting cabin
     * @throws IOException
     */
    void askColor ();
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
    void showJoinableGamesList(Map<String, GameController> joinableGames);

    /**
     * asks player to set a player count for the created game
     * @throws IOException
     */
    void askPlayerCount () throws IOException;

    /**
     * asks the server to create a new game with the name chosen
     * @throws IOException
     */
    void createGame() throws IOException;

    /**
     * asks to join the game with the inputted name
     * @throws IOException
     */
    void joinGame() throws IOException;

    /**
     * shows on the view a generic message sent by the server
     * @param genericMessage the message to be shown
     */
    void showGenericMessage (String genericMessage);

    /**
     * shows on the view a error message sent by the server
     * @param errorMessage the message to be shown.
     */
    void showErrorMessage (String errorMessage);
    /**
     * shows on the view the list of players in the game with their status (in flight or landed)
     * @param players in game players
     */
    void showInGamePlayers (ArrayList<Player> players);
    /**
     * shows the chosen players shipboard
     * @param player
     * @param shipBoard
     */
    void showPlayersBoard(String player, ShipBoard shipBoard);
    /**
     * show the drawnTile to the player
     * @param drawnTile
     */
    //DEVE ANCHE MANDARLA AL CLIENT
    void showDrawnTile (Tile drawnTile) throws RemoteException;
    /**
     * shows the player the tiles that got turned from player refusing them
     * @param turnedTiles the current array in the model
     */
    void showTurnedTiles (Map<Integer,Tile> turnedTiles);
    /**
     * shows the tiles that the player booked on their shipboard
     * @param bookedTiles the booked tiles
     */
    void showBookedTiles (ArrayList<Tile> bookedTiles);
    void showWrongInputMessage () throws RemoteException;
    /**
     * prints on the view the cards that are on the shipboard to be seen during
     * ship building phase
     * @param inGameCards the cards returned by the model
     */
    void showInGameCards (ArrayList<Card> inGameCards);
    void asksToRollTheDices();//asks the player to confirm he wants to roll the dices
    void showDiceRoll(int diceRoll);
    /**
     * asks the player to choose a starting position from 1 to playerCount
     */
    void asksToChooseStartingPosition ();

    /**
     * asks the player to input a series of coordinates of coord
     */
    void asksToInputCoordinates (CoordReqType coordReqType) throws RemoteException;
    void asksToTurnTheHourglass ();
    void notifyYouCanDrawThisCardDeck();
    void notifyYourShipIsCorrect();
    /**
     * asks to set position on the flightboard
     */
    void asksToMakeAChoice () throws RemoteException;
    void asksToManageGoods(ArrayList<Goods> goods) throws RemoteException;
    void asksToRemoveGoods();
    void asksToRemoveCrew();
    /**
     * at the end of the game shows every players score on the view
     */
    void showScores(ArrayList<Player> players);
    /**
     * asks the player to choose the mode of the game to be created
     */
    void notifyDrawnCard(Card card) throws RemoteException; //tell the players which card has been drawn
    void notifyPlayerLandedOnPlanet(String playerName, int planet)  throws RemoteException;
    void wrongLocalInput();
    void showCard(Card card);
    void printFlightboard(LightFlightboard lightFlightboard);
    void printShipboard(LightShipBoard lightShipBoard);
    void printProjectile(Projectile projectile);

}
