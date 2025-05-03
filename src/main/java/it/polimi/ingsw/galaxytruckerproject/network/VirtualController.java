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
    //void connect(VirtualView client, String playerName) throws RemoteException;
    void createGame( String gameName, int playerCount, GameMode chooseMode) throws RemoteException;
    void joinGame(String gameName) throws RemoteException;
    void leaveGame() throws RemoteException;
    void leave() throws RemoteException;
    void chooseColor( PlayersColor color) throws RemoteException;


    //TILE RELATED METHODS
    void notifySetTile( Tile tile) throws RemoteException;
    void notifyRefusedTile() throws RemoteException;
    void reqDrawTileFromTurned( int index) throws RemoteException;
    void reqDrawTileFromStack() throws RemoteException;//pescaggio tile scoperte
    void notifyTileBooking() throws RemoteException;//per ora metto key però forse è meglio tile


    // CARDS RELATED METHODS
    void lookCardsRequest( int deckToLookAt) throws RemoteException;
    void stopLookingAtCardsRequest() throws RemoteException;
    void drawCards() throws RemoteException;


    // COORDINATES RELATED METHODS
    void sendCoordinates( ArrayList<Coordinates> coordinates) throws RemoteException;
    void sendDoubleCannonUsed( float Strength, ArrayList<Coordinates> coordinates) throws RemoteException;
    void sendNumDoubleEngineUsed( int NumEngine, ArrayList<Coordinates> coordinates) throws RemoteException;


    // YES/NO
    void sendYes() throws RemoteException;
    void sendNo() throws RemoteException;


    void sendTurnHourGlass( ) throws RemoteException;
    void rollTheDices() throws RemoteException;
    void planetChoiceRequest(int choice) throws RemoteException;

    /*BOOLEAN
    boolean checkCorrectionShipboard(String playerName) throws RemoteException;
     */
    void notifyEarlyLanding() throws RemoteException;
    void notifyCompleted() throws RemoteException;
    void notifySetPosition( int position) throws RemoteException;


    void notifyNewGoodsArrangement( int clientGoodsValue, ArrayList<CargoHold> updatedCargos) throws RemoteException;
    void notifyNewCrewArrangement( ArrayList<Tile> updatedCabin) throws RemoteException;

    void setView(VirtualView virtualView) throws RemoteException;

    void ping() throws RemoteException;


    //METODI CON USAGE CHE SONO STATI MODIFICATI(DA CONTROLLARE CON FEDERICO)
    //void notifySetTile( String playerName, Coordinates coordinates, Tile tile) throws RemoteException;
    //void sendEndShipBoardCreation(String playerName) throws RemoteException;

}
