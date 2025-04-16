package it.polimi.ingsw.galaxytruckerproject.network;

import it.polimi.ingsw.galaxytruckerproject.model.tiles.Tile;
import it.polimi.ingsw.galaxytruckerproject.view.ViewInterface;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Map;

public interface VirtualView extends ViewInterface {

    void notifyNewTurnedTile(Tile tile) throws RemoteException;//add a new turned tile to the map in the clients
    void notifyRemoveTurnedTile(Tile tile) throws RemoteException;//remove a turned tile to the map in the clients
    void notifyPlayerMovement(String playerName, int playerPosition, int playerRanking) throws RemoteException;
    void notifyPositionedTile(String playerName,Tile tile) throws RemoteException;//notify that a tile was placed by a player
    void notifyBookedTile (String playerName, Tile tile) throws RemoteException;
    void notifyAvailableCardDeck(Map<String,Integer> lockedSmallDecks) throws RemoteException;
}
