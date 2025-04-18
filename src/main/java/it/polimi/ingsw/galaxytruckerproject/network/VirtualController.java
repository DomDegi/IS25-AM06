package it.polimi.ingsw.galaxytruckerproject.network;

import it.polimi.ingsw.galaxytruckerproject.model.GameMode;
import it.polimi.ingsw.galaxytruckerproject.model.player.PlayersColor;
import it.polimi.ingsw.galaxytruckerproject.model.tiles.CargoHold;
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
    void joinGame(String gameName, String playerName) throws RemoteException;
    void leaveGame(String playerName) throws RemoteException;
    void leave(String playerName) throws RemoteException;
    void chooseColor(String playerName, PlayersColor color) throws RemoteException;

    void reqDrawTileRequest(String playerName) throws RemoteException;
    void reqDrawTileFromTurned(String playerName) throws RemoteException;
    void refuseTile(int index) throws RemoteException;
    void lookGameCards(int index) throws RemoteException;
    void stopLookingAtCards(int index) throws RemoteException;


    void sendCoordinates(   String playerName,ArrayList<Coordinates> coordinates) throws RemoteException;
    void sendDoubleCannonUsed(String playerName , float Strength, ArrayList<Coordinates> coordinates) throws RemoteException;
    void sendNumDoubleEngineUsed(String playerName , int NumEngine, ArrayList<Coordinates> coordinates) throws RemoteException;
    void notifySetTile( String playerName, Coordinates coordinates, Tile tile) throws RemoteException;
    void sendYes( String playerName) throws RemoteException;
    void sendNo( String playerName) throws RemoteException;
    void sendEndShipBoardCreation(String playerName) throws RemoteException;
    void sendTurnHourGlass(String playerName) throws RemoteException;
    void reqDrawTileFromPile(String playerName) throws RemoteException;
    void reqDrawTileFromTable(String playerName, int index) throws RemoteException;//pescaggio tile scoperte
    void notifyTileBooking(String playerName) throws RemoteException;//per ora metto key però forse è meglio tile
    void notifyRefusedTile(String playerName) throws RemoteException;
    void planetChoiceRequest(String playerName,int chose) throws RemoteException;
    void stopLookingAtCardsRequest(String playerName) throws RemoteException;
    /*BOOLEAN
    boolean checkCorrectionShipboard(String playerName) throws RemoteException;
     */
    void notifyEarlyLanding(String playerName) throws RemoteException;

    void notifyNewGoodsArrangement(String playerName,int clientGoodsValue, ArrayList<CargoHold> modifiedCargoHold) throws RemoteException;
    //pescaggio carta durante shipboard
    //notifica fine lettura carte
    //scelta pianeta
    //invio array di tile con i nuovi good sendNewGoodsSetUp
    //
}
