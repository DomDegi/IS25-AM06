package it.polimi.ingsw.galaxytruckerproject.network;

import it.polimi.ingsw.galaxytruckerproject.model.GameMode;
import it.polimi.ingsw.galaxytruckerproject.model.player.PlayersColor;
import it.polimi.ingsw.galaxytruckerproject.model.tiles.Cabin;
import it.polimi.ingsw.galaxytruckerproject.model.tiles.CargoHold;
import it.polimi.ingsw.galaxytruckerproject.model.tiles.Coordinates;
import it.polimi.ingsw.galaxytruckerproject.model.tiles.Tile;
import it.polimi.ingsw.galaxytruckerproject.view.ViewInterface;

import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

public interface VirtualController extends Remote, Serializable {

    //PHASE OF LOGIN/CREATION OF MATCHES METHODS
    void login(String playerName) throws RemoteException;
    void connect(VirtualView client, String playerName) throws RemoteException;
    void createGame(String gameName, int playerCount, GameMode chooseMode, String playerName) throws RemoteException;
    void joinGame(String gameName, String playerName) throws RemoteException;
    void leaveGame(String playerName) throws RemoteException;
    void leave(String playerName) throws RemoteException;
    void chooseColor(String playerName, PlayersColor color) throws RemoteException;


    //TILE RELATED METHODS
    void notifySetTile(String playerName, Tile tile) throws RemoteException;
    void notifyRefusedTile(String playerName) throws RemoteException;
    void reqDrawTileFromTurned(String playerName, int index) throws RemoteException;
    void reqDrawTileFromStack(String playerName) throws RemoteException;//pescaggio tile scoperte
    void notifyTileBooking(String playerName) throws RemoteException;//per ora metto key però forse è meglio tile


    // CARDS RELATED METHODS
    void lookCardsRequest(String playerName, int deckToLookAt) throws RemoteException;
    void stopLookingAtCardsRequest(String playerName) throws RemoteException;
    void drawCards(String playerName) throws RemoteException;


    // COORDINATES RELATED METHODS
    void sendCoordinates(String playerName, ArrayList<Coordinates> coordinates) throws RemoteException;
    void sendDoubleCannonUsed(String playerName, float Strength, ArrayList<Coordinates> coordinates) throws RemoteException;
    void sendNumDoubleEngineUsed(String playerName, int NumEngine, ArrayList<Coordinates> coordinates) throws RemoteException;


    // YES/NO
    void sendYes(String playerName) throws RemoteException;
    void sendNo(String playerName) throws RemoteException;


    void sendTurnHourGlass(String playerName) throws RemoteException;
    void rollTheDices(String playerName) throws RemoteException;
    void planetChoiceRequest(String playerName, int choice) throws RemoteException;

    /*BOOLEAN
    boolean checkCorrectionShipboard(String playerName) throws RemoteException;
     */
    void notifyEarlyLanding(String playerName) throws RemoteException;
    void notifyCompleted(String playerName) throws RemoteException;
    void notifySetPosition(String playerName, int position) throws RemoteException;


    void notifyNewGoodsArrangement(String playerName, int clientGoodsValue, ArrayList<CargoHold> updatedCargos) throws RemoteException;
    void notifyNewCrewArrangement(String playerName, ArrayList<Tile> updatedCabin) throws RemoteException;



    //METODI CON USAGE CHE SONO STATI MODIFICATI(DA CONTROLLARE CON FEDERICO)
    //void notifySetTile( String playerName, Coordinates coordinates, Tile tile) throws RemoteException;
    //void sendEndShipBoardCreation(String playerName) throws RemoteException;

}
