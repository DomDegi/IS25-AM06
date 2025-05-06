package it.polimi.ingsw.galaxytruckerproject.network.Socket.ServerMessage;

import it.polimi.ingsw.galaxytruckerproject.network.Socket.ServerHandler;

import java.rmi.RemoteException;

public class AskToRollTheDicesMessage extends ServerMessage{

    public AskToRollTheDicesMessage() {}

    @Override
    public void processMessage(ServerHandler serverHandler) {
        try {
            serverHandler.getView().asksToRollTheDices();
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }
}
