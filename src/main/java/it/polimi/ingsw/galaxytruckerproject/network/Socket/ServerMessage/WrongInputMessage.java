package it.polimi.ingsw.galaxytruckerproject.network.Socket.ServerMessage;

import it.polimi.ingsw.galaxytruckerproject.network.Socket.ServerHandler;

import java.rmi.RemoteException;

public class WrongInputMessage extends ServerMessage{

    public WrongInputMessage() {}

    @Override
    public void processMessage(ServerHandler serverHandler) {
        try {
            serverHandler.getView().showWrongInputMessage();
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
        serverHandler.getClientController().rollBackState();
    }
}
