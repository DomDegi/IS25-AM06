package it.polimi.ingsw.galaxytruckerproject.network.Socket.ServerMessage;

import it.polimi.ingsw.galaxytruckerproject.network.Socket.ServerHandler;

public class PlayerDisconnectedMessage extends ServerMessage {

    private final String playerName;

    public PlayerDisconnectedMessage(String playerName) {
        this.playerName = playerName;
    }

    @Override
    public void processMessage(ServerHandler serverHandler) {
        serverHandler.getView().notifyPlayerDisconnected(playerName);
    }
}
