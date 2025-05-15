package it.polimi.ingsw.galaxytruckerproject.network.Socket.ServerMessage;

import it.polimi.ingsw.galaxytruckerproject.network.Socket.ServerHandler;

public class CurrentGameStatusMessage extends ServerMessage {

    private final String currentGameStatus;

    public CurrentGameStatusMessage(String currentStatus) {
        this.currentGameStatus = currentStatus;
    }

    @Override
    public void processMessage(ServerHandler serverHandler) {
        serverHandler.getClientController().updateModel(currentGameStatus);
    }
}
