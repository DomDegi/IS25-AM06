package it.polimi.ingsw.galaxytruckerproject.network.Socket.ServerMessage;

import it.polimi.ingsw.galaxytruckerproject.network.Socket.ServerHandler;

public class PlayerMovementMessage extends ServerMessage {

    private String playerName;
    private int playerPosition;
    private int playerRanking;

    public PlayerMovementMessage(String playerName, int playerPosition, int playerRanking) {
        this.playerName = playerName;
        this.playerPosition = playerPosition;
        this.playerRanking = playerRanking;
    }

    @Override
    public void processMessage(ServerHandler serverHandler) {
        serverHandler.getClientController().updateFlightboard(playerName, playerPosition, playerRanking);
    }
}
