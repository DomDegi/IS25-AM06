package it.polimi.ingsw.galaxytruckerproject.network.RMI.Server;

import it.polimi.ingsw.galaxytruckerproject.controller.Controller;
import it.polimi.ingsw.galaxytruckerproject.model.tiles.Coordinates;
import it.polimi.ingsw.galaxytruckerproject.network.RMI.VirtualController;
import it.polimi.ingsw.galaxytruckerproject.network.SOCKET.message.AcceptMessage;
import it.polimi.ingsw.galaxytruckerproject.network.SOCKET.message.Message;
import it.polimi.ingsw.galaxytruckerproject.network.SOCKET.message.RefuseMessage;
import it.polimi.ingsw.galaxytruckerproject.network.SOCKET.message.SendCoordinatesResponse;

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
    void sendStrenghtDoubleCannonUsed( String playerName ,float Strength) throws RemoteException{
        Message message = new (playerName)
        clients.get(playerName).playerChoiceThroughMessage(message);

    }
    void sendNumDoubleEngineUsed(String playerName , int NumEngine, ArrayList<Coordinates> coordinates) throws RemoteException{
        Message message = new  (playerName);
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




}
