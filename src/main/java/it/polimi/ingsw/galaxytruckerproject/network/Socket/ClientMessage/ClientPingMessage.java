package it.polimi.ingsw.galaxytruckerproject.network.Socket.ClientMessage;

import it.polimi.ingsw.galaxytruckerproject.network.Socket.ClientHandler;

import java.rmi.RemoteException;

public class ClientPingMessage extends ClientMessage {

    public ClientPingMessage() {}


    @Override
    public void processMessage(ClientHandler clientHandler) {
        try {
            clientHandler.getController().ping();
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }
}
