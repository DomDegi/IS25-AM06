package it.polimi.ingsw.galaxytruckerproject.network.Socket.ServerMessage;

import it.polimi.ingsw.galaxytruckerproject.network.Socket.ServerHandler;

import java.rmi.RemoteException;

public class GenericServerMessage extends ServerMessage {

    String genericMessage;

    public GenericServerMessage(String genericMessage) {
        this.genericMessage = genericMessage;
    }

    @Override
    public void processMessage(ServerHandler serverHandler) {
            serverHandler.getView().showGenericMessage(genericMessage);
    }
}
