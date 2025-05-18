package it.polimi.ingsw.galaxytruckerproject.network;

import it.polimi.ingsw.galaxytruckerproject.model.GameMode;
import it.polimi.ingsw.galaxytruckerproject.model.cards.Card;
import it.polimi.ingsw.galaxytruckerproject.model.player.PlayersColor;
import it.polimi.ingsw.galaxytruckerproject.model.tiles.Coordinates;
import it.polimi.ingsw.galaxytruckerproject.model.tiles.Tile;
import it.polimi.ingsw.galaxytruckerproject.view.ViewInterface;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Map;

public interface VirtualView extends ViewInterface, Serializable {

    void notifyNewTurnedTile(Tile tile) throws RemoteException;//add a new turned tile to the map in the clients
    void notifyRemoveTurnedTile(Tile tile) throws RemoteException;//remove a turned tile to the map in the clients
    void notifyPlayerMovement(String playerName, PlayersColor color, int playerPosition, int playerRanking) throws RemoteException;
    void notifyPositionedTile(String playerName,Tile tile) throws RemoteException;//notify that a tile was placed by a player
    void notifyBookedTile (String playerName, Tile tile) throws RemoteException;
    void notifyRemovedBookedTile(String playerName, Tile tile) throws RemoteException;
    void notifyNotAvailableCardDeck(ArrayList<Integer> lockedSmallDecks) throws RemoteException; //notify which cards deck are not looked at
    void notifyNotAvailableColor(PlayersColor color) throws RemoteException; //notify which cards deck are not looked at
    void notifyModifiedTiles(String playerName, ArrayList<Tile> tiles) throws RemoteException;
    void notifyGainedCredits (String playerName, int totalCredits) throws RemoteException;
    void notifyBrokenTile(String playerName, ArrayList<Coordinates> coordinates)throws RemoteException;
    void notifyChangesWhileGone(String currentGameStatus) throws RemoteException;
    void notifyFlightBoardCards(Map<Integer, ArrayList<Card>> cards) throws RemoteException;
    void initializeShipBoards(GameMode gameMode) throws RemoteException;
    void victimOfThePenalty(String playerName) throws RemoteException;
    void ping() throws RemoteException;
    void notifyPlayerJoined(int expected, int current, boolean reconnected) throws RemoteException;
}