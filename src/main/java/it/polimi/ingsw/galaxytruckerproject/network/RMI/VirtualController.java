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
    void sendCoordinates(String playerName, ArrayList<Coordinates> coordinates) throws RemoteException;
    void sendStrenghtDoubleCannonUsed(String playerName, int Strenght) throws RemoteException;
    void sendNumDoubleEngineUsed(String playerName, int NumDouble) throws RemoteException;

    void notifySetTile(String playerName, Coordinates coordinates, Tile tile) throws RemoteException;

    void sendYes(String playerName) throws RemoteException;

    void sendNo(String playerName) throws RemoteException;

    //DONE
    void sendEndShipboardCreation(String playerName) throws RemoteException;

    //DONE
    void sendFlipHourGlass(String playerName) throws RemoteException;

    //DONE
    Tile reqDrawTileFromPile(String playerName) throws RemoteException;

    //DONE
    Tile reqDrawTileFromTable(String playerName) throws RemoteException;//pescaggio tile scoperte

    //DONE
    void notifyTileBooking(String playerName) throws RemoteException;//per ora metto key però forse è meglio tile

    //DONE
    void notifyRefusedTile(String playerName) throws RemoteException;

    //BOOLEAN
    boolean checkCorrectionShipboard(String playerName) throws RemoteException;
}
