package it.polimi.ingsw.galaxytruckerproject.network;

import it.polimi.ingsw.galaxytruckerproject.model.tiles.Tile;
import it.polimi.ingsw.galaxytruckerproject.view.ViewInterface;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Map;

public interface VirtualView extends ViewInterface {

    void notifyNewTurnedTile(Tile tile) throws RemoteException;
    void notfyRemmoveTurnedTile(Tile tile) throws RemoteException;
    void notifyAvailableCardDeck(Map<String, Integer> lockedSmallDecks) throws RemoteException;
    void notifyPositionedTile(Tile tile) throws RemoteException;
}
