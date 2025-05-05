package it.polimi.ingsw.galaxytruckerproject.network.Socket.ServerMessage;


import it.polimi.ingsw.galaxytruckerproject.network.Socket.ServerHandler;

import java.rmi.RemoteException;

public class TurnedHourglassMessage extends ServerMessage {

    public TurnedHourglassMessage() {}

    @Override
    public void processMessage(ServerHandler serverHandler) {
        try {
            serverHandler.getView().notifyTurnedHourglass();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}
