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
    void sendCoordinates(ArrayList<Coordinates> coordinates) throws RemoteException;
    void sendStrenghtDoubleCannonUsed(int Strenght) throws RemoteException;
    void sendNumDoubleEngineUsed(int NumDouble) throws RemoteException;
    void notifySetTile(Coordinates coordinates, Tile tile) throws RemoteException;
    void sendYes() throws RemoteException;
    void sendNo() throws RemoteException;
    void sendEndShipboardCreation() throws RemoteException;
    void sendTurnTime() throws RemoteException;
    Tile reqDrawTileFromDeck() throws RemoteException;
    Tile reqDrawTileFromTable(int key) throws RemoteException;//pescaggio tile scoperte
    void notifyTileBooking(int key) throws RemoteException;//per ora metto key però forse è meglio tile
    void notifyRefusedTile(int key) throws RemoteException;
    boolean checkCorrectionShipboard() throws RemoteException;
}
