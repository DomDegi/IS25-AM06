package it.polimi.ingsw.galaxytruckerproject.network.RMI.Server;

import it.polimi.ingsw.galaxytruckerproject.controller.Controller;
import it.polimi.ingsw.galaxytruckerproject.controller.MultiGameController;
import it.polimi.ingsw.galaxytruckerproject.model.tiles.Tile;
import it.polimi.ingsw.galaxytruckerproject.network.RMI.VirtualController;
import it.polimi.ingsw.galaxytruckerproject.network.SOCKET.message.*;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
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
    }
    public void reset () throws RemoteException {
        System.err.println("RMI server reset ");
        synchronized (this.clients) {

        }
    }
    */

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


    public void drawTilefromPile(String playerName) throws RemoteException {
        DrawTileFromStackRequest message = new DrawTileFromStackRequest(playerName);
        clients.get(playerName).playerChoiceThroughMessage(message);
    }

    public void drawTilefromTable(String playerName, int index) throws RemoteException {
        DrawTileFromTurnedRequest message = new DrawTileFromTurnedRequest(playerName, index);
        clients.get(playerName).playerChoiceThroughMessage(message);
    }

    public void sendTurnHourGlass(String playerName) throws RemoteException {
        TurnHourglassRequest message = new TurnHourglassRequest(playerName);
        clients.get(playerName).playerChoiceThroughMessage(message);
    }





}
