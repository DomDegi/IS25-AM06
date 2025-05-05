package it.polimi.ingsw.galaxytruckerproject.network.Socket.ServerMessage;

import it.polimi.ingsw.galaxytruckerproject.network.Socket.ServerHandler;

import java.rmi.RemoteException;

public class TimeExpiredMessage extends ServerMessage {

    public TimeExpiredMessage() {}

    @Override
    public void processMessage(ServerHandler serverHandler) {
        try {
            serverHandler.getView().notifyTurnedHourglass();
        } catch (RemoteException ignored) {
        }
    }
}
