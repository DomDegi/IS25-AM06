package it.polimi.ingsw.galaxytruckerproject.network.Socket.ServerMessage;


import it.polimi.ingsw.galaxytruckerproject.network.Socket.ServerHandler;

import java.rmi.RemoteException;

public class TurnedHourglassMessage extends ServerMessage {

    private int turns;

    public TurnedHourglassMessage(int turns) {
        this.turns = turns;
    }

    @Override
    public void processMessage(ServerHandler serverHandler) {
        try {
            serverHandler.getView().notifyTurnedHourglass(turns);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}
