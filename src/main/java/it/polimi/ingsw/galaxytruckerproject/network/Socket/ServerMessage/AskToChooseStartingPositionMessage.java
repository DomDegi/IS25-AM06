package it.polimi.ingsw.galaxytruckerproject.network.Socket.ServerMessage;

import it.polimi.ingsw.galaxytruckerproject.model.Game;
import it.polimi.ingsw.galaxytruckerproject.network.Socket.ServerHandler;

import java.rmi.RemoteException;

public class AskToChooseStartingPositionMessage extends ServerMessage{

    public AskToChooseStartingPositionMessage() {}

    public void processMessage(ServerHandler serverHandler) {
        try {
            serverHandler.getView().asksToChooseStartingPosition();
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }
}
