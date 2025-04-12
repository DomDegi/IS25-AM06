package it.polimi.ingsw.galaxytruckerproject.network.RMI.Server;

import it.polimi.ingsw.galaxytruckerproject.controller.Controller;
import it.polimi.ingsw.galaxytruckerproject.controller.MultiGameController;
import it.polimi.ingsw.galaxytruckerproject.model.tiles.Tile;
import it.polimi.ingsw.galaxytruckerproject.model.tiles.Coordinates;
import it.polimi.ingsw.galaxytruckerproject.network.RMI.VirtualController;
import it.polimi.ingsw.galaxytruckerproject.network.SOCKET.message.*;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.HashMap;

public class VirtualControllerRMI extends UnicastRemoteObject implements VirtualController{


    final MultiGameController multiController;
    //final List<ViewInterface> clients = new ArrayList<>();
    private HashMap <String, Controller> clients;

    protected VirtualControllerRMI(MultiGameController multiController) throws RemoteException {
        super();
        this.multiController = multiController;
    }

     /*
    public void connect(ViewInterface client) throws RemoteException {
        synchronized (this.clients) {
            this.clients.add(client);
        }

     */

    /*
    public void reset () throws RemoteException {
        System.err.println("RMI server reset ");
        synchronized (this.clients) {

        }
    }*/

    public void sendCoordinates(String playerName, ArrayList<Coordinates> coordinates) throws RemoteException{
        Message message = new SendCoordinatesResponse(playerName, coordinates);
        clients.get(playerName).playerChoiceThroughMessage(message);
    }
    public void sendDoubleCannonUsed( String playerName ,float Strength, ArrayList<Coordinates> coordinates) throws RemoteException {
        Message message = new UseCannonResponse(playerName, Strength, coordinates);
        clients.get(playerName).playerChoiceThroughMessage(message);
    }

    public void sendNumDoubleEngineUsed(String playerName , int NumEngine, ArrayList<Coordinates> coordinates) throws RemoteException{
        Message message = new UseEngineResponse(playerName, NumEngine, coordinates);
        clients.get(playerName).playerChoiceThroughMessage(message);

    }


    public void sendYes( String playerName) throws RemoteException{
        Message message = new AcceptMessage(playerName);
        clients.get(playerName).playerChoiceThroughMessage(message);
    }

    public void sendNo(String playerName) throws RemoteException{
        Message message = new RefuseMessage(playerName);
        clients.get(playerName).playerChoiceThroughMessage(message);
    }

    public void notifySetTile(String playerName, Coordinates coordinates, Tile tile) throws RemoteException {
        Message message= new SetTileRequest(playerName, coordinates, tile);
        clients.get(playerName).playerChoiceThroughMessage(message);
    }

    public void sendEndShipBoardCreation(String playerName) throws RemoteException {
        AcceptMessage message = new AcceptMessage(playerName);
        clients.get(playerName).playerChoiceThroughMessage(message);
    }

    public void notifyTileBooking(String playerName) throws RemoteException {
        BookTileRequest message = new BookTileRequest(playerName);
        clients.get(playerName).playerChoiceThroughMessage(message);
    }

    public void notifyRefusedTile(String playerName) throws RemoteException {
        RefuseMessage message = new RefuseMessage(playerName);
        clients.get(playerName).playerChoiceThroughMessage(message);
    }


    public void drawTileRequest(String playerName) throws RemoteException {
        DrawTileFromStackRequest message = new DrawTileFromStackRequest(playerName);
        clients.get(playerName).playerChoiceThroughMessage(message);
    }


    public void reqDrawTileFromPile(String playerName) throws RemoteException {
        DrawTileFromStackRequest message = new DrawTileFromStackRequest(playerName);
        clients.get(playerName).playerChoiceThroughMessage(message);
    }

    public void reqDrawTileFromTable(String playerName, int index) throws RemoteException {
        DrawTileFromTurnedRequest message = new DrawTileFromTurnedRequest(playerName, index);
        clients.get(playerName).playerChoiceThroughMessage(message);
    }

    public void sendTurnHourGlass(String playerName) throws RemoteException {
        TurnHourglassRequest message = new TurnHourglassRequest(playerName);
        clients.get(playerName).playerChoiceThroughMessage(message);
    }

    public void notifyEarlyLanding(String playerName) throws RemoteException {
        EarlyLandingRequest message = new EarlyLandingRequest(playerName);
        clients.get(playerName).playerChoiceThroughMessage(message);
    }





}
