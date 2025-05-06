package it.polimi.ingsw.galaxytruckerproject.network.Socket.ServerMessage;

import it.polimi.ingsw.galaxytruckerproject.network.Socket.ServerHandler;

import java.rmi.RemoteException;

public class ShipIsCorrectMessage extends ServerMessage{

    public ShipIsCorrectMessage() {}

    @Override
    public void processMessage(ServerHandler serverHandler) {
        try {
            serverHandler.getView().notifyYourShipIsCorrect();
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }
}
