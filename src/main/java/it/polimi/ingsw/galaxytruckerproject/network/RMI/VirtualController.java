package it.polimi.ingsw.galaxytruckerproject.network.RMI;

import it.polimi.ingsw.galaxytruckerproject.model.GameMode;
import it.polimi.ingsw.galaxytruckerproject.model.tiles.Coordinates;
import it.polimi.ingsw.galaxytruckerproject.model.tiles.Tile;
import it.polimi.ingsw.galaxytruckerproject.view.ViewInterface;

import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

public interface VirtualController extends Remote, Serializable {
    void connect(ViewInterface client, String playerName) throws RemoteException;
    void createGame(String gameName, int playerCount, GameMode chooseMode, String playerName) throws RemoteException;
    public void joinGame(String gameName, String playerName) throws RemoteException;
    public void leaveGame(String playerName) throws RemoteException;
    public void leave(String playerName) throws RemoteException;
    public void chooseColor(String playerName, String color) throws RemoteException;


    void sendCoordinates(   String playerName,ArrayList<Coordinates> coordinates) throws RemoteException;
    void sendDoubleCannonUsed(String playerName , float Strength, ArrayList<Coordinates> coordinates) throws RemoteException;
    void sendNumDoubleEngineUsed(String playerName , int NumEngine, ArrayList<Coordinates> coordinates) throws RemoteException;
    void notifySetTile( String playerName, Coordinates coordinates, Tile tile) throws RemoteException;
    void sendYes( String playerName) throws RemoteException;
    void sendNo( String playerName) throws RemoteException;


    //DONE
    void sendEndShipboardCreation(String playerName) throws RemoteException;

    //DONE
    void sendTurnHourGlass(String playerName) throws RemoteException;

    //DONE
    void reqDrawTileFromPile(String playerName) throws RemoteException;

    //DONE
    void reqDrawTileFromTable(String playerName, int index) throws RemoteException;//pescaggio tile scoperte

    //DONE
    void notifyTileBooking(String playerName) throws RemoteException;//per ora metto key però forse è meglio tile

    //DONE
    void notifyRefusedTile(String playerName) throws RemoteException;

    /*BOOLEAN
    boolean checkCorrectionShipboard(String playerName) throws RemoteException;
     */
    void notifyEarlyLanding(String playerName) throws RemoteException;

}
