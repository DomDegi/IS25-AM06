package it.polimi.ingsw.galaxytruckerproject.network.Socket.ServerMessage;

import it.polimi.ingsw.galaxytruckerproject.network.Socket.ServerHandler;

public class PlayerJoinedMessage extends ServerMessage {

    private final int expectedPlayers;
    private final int currentPlayers;
    private final boolean reconnected;

    public PlayerJoinedMessage(int expectedPlayers, int currentPlayers,  boolean reconnected) {
        this.expectedPlayers = expectedPlayers;
        this.currentPlayers = currentPlayers;
        this.reconnected = reconnected;
    }

    @Override
    public void processMessage(ServerHandler serverHandler) {
        serverHandler.getView().notifyPlayerJoined(expectedPlayers, currentPlayers,reconnected);
    }
}
