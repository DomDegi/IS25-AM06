package it.polimi.ingsw.galaxytruckerproject.network.Socket.ServerMessage;

import it.polimi.ingsw.galaxytruckerproject.client.ClientState;
import it.polimi.ingsw.galaxytruckerproject.network.Socket.ServerHandler;

import java.rmi.RemoteException;

public class EarlyLandingMessage extends ServerMessage {
    @Override
    public void processMessage(ServerHandler serverHandler) {
        try {
            serverHandler.getView().notifyEarlyLanding();
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
        serverHandler.getClientController().setState(ClientState.WAIT);
    }
}
