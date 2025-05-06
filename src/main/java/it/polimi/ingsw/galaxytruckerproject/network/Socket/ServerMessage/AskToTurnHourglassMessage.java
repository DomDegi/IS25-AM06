package it.polimi.ingsw.galaxytruckerproject.network.Socket.ServerMessage;

import it.polimi.ingsw.galaxytruckerproject.network.Socket.ServerHandler;

import java.rmi.RemoteException;

public class AskToTurnHourglassMessage extends ServerMessage {


    public AskToTurnHourglassMessage() {
    }

    @Override
    public void processMessage(ServerHandler serverHandler) {
        try {
            serverHandler.getView().asksToTurnTheHourglass();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}
