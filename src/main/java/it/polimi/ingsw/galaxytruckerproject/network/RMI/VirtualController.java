package it.polimi.ingsw.galaxytruckerproject.network.RMI;

import it.polimi.ingsw.galaxytruckerproject.model.tiles.Coordinates;
import it.polimi.ingsw.galaxytruckerproject.model.tiles.Tile;
import it.polimi.ingsw.galaxytruckerproject.view.ViewInterface;

import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

public interface VirtualController extends Remote, Serializable {
    void connect(ViewInterface client) throws RemoteException;
    void sendCoordinates(   String playerName,ArrayList<Coordinates> coordinates) throws RemoteException;
    void sendDoubleCannonUsed(String playerName , float Strength, ArrayList<Coordinates> coordinates) throws RemoteException;
    void sendNumDoubleEngineUsed(String playerName , int NumEngine, ArrayList<Coordinates> coordinates) throws RemoteException;
    void notifySetTile( String playerName, Coordinates coordinates, Tile tile) throws RemoteException;
    void sendYes( String playerName) throws RemoteException;
    void sendNo( String playerName) throws RemoteException;
    void sendEndShipboardCreation( String playerName) throws RemoteException;
    void sendTurnTime( String playerName) throws RemoteException;
    Tile reqDrawTileFromDeck( String playerName) throws RemoteException;
    Tile reqDrawTileFromTable( String playerName, int key) throws RemoteException;//pescaggio tile scoperte
    void notifyTileBooking( String playerName, int key) throws RemoteException;//per ora metto key però forse è meglio tile
    void notifyRefusedTile( String playerName, int key) throws RemoteException;
    boolean checkCorrectionShipboard( String playerName) throws RemoteException;
}
