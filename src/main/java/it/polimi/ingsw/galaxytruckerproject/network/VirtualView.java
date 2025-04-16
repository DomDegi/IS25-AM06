package it.polimi.ingsw.galaxytruckerproject.network;

import it.polimi.ingsw.galaxytruckerproject.model.tiles.Tile;
import it.polimi.ingsw.galaxytruckerproject.view.ViewInterface;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface VirtualView extends ViewInterface {

    void notifyNewTurnedTile(Tile tile) throws RemoteException;//add a new turned tile to the map in the clients

    void notfyRemmoveTurnedTile(Tile tile) throws RemoteException;//remove a turned tile to the map in the clients



}
