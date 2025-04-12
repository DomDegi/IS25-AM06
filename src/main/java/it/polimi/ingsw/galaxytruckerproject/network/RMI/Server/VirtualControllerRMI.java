package it.polimi.ingsw.galaxytruckerproject.network.RMI.Server;

import it.polimi.ingsw.galaxytruckerproject.controller.Controller;
import it.polimi.ingsw.galaxytruckerproject.model.tiles.Coordinates;
import it.polimi.ingsw.galaxytruckerproject.model.tiles.Tile;
import it.polimi.ingsw.galaxytruckerproject.network.RMI.VirtualController;
import it.polimi.ingsw.galaxytruckerproject.network.SOCKET.message.*;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;

public class VirtualControllerRMI extends UnicastRemoteObject implements VirtualController{

    private HashMap<String,Controller> clients;

    public void sendCoordinates(String playerName, ArrayList<Coordinates> coordinates) throws RemoteException{
        Message message = new SendCoordinatesResponse(playerName, coordinates);
        clients.get(playerName).playerChoiceThroughMessage(message);
    }
    public void sendDoubleCannonUsed( String playerName ,float Strength, ArrayList<Coordinates> coordinates) throws RemoteException{
        Message message = new UseCannonResponse(playerName,Strength, coordinates);
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
}
