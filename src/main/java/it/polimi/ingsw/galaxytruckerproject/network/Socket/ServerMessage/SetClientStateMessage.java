package it.polimi.ingsw.galaxytruckerproject.network.Socket.ServerMessage;

import it.polimi.ingsw.galaxytruckerproject.client.ClientState;
import it.polimi.ingsw.galaxytruckerproject.network.Socket.ServerHandler;

import java.rmi.RemoteException;

public class SetClientStateMessage extends ServerMessage {

    ClientState clientState;

    public SetClientStateMessage(ClientState clientState) {
        this.clientState = clientState;
    }

    @Override
    public void processMessage(ServerHandler serverHandler) {
        try {
            serverHandler.getView().setClientState(clientState);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}
