package it.polimi.ingsw.galaxytruckerproject.network.Socket.ServerMessage;

import it.polimi.ingsw.galaxytruckerproject.network.Socket.ServerHandler;

import java.rmi.RemoteException;

public class LoginResponseMessage extends ServerMessage {

    private boolean success;

    public LoginResponseMessage (boolean success) {
        this.success = success;
    }

    @Override
    public void processMessage(ServerHandler serverHandler) {
        try {
            serverHandler.getView().showLoginResponse(success);
        } catch (RemoteException e) {
            ;
        }
    }
}
