package it.polimi.ingsw.galaxytruckerproject.network.Socket.ServerMessage;

import it.polimi.ingsw.galaxytruckerproject.model.player.PlayersColor;
import it.polimi.ingsw.galaxytruckerproject.network.Socket.ServerHandler;

public class PlayerMovementMessage extends ServerMessage {

    private String playerName;
    private int playerPosition;
    private int playerRanking;
    private PlayersColor color;

    public PlayerMovementMessage(String playerName,PlayersColor color ,int playerPosition, int playerRanking) {
        this.playerName = playerName;
        this.playerPosition = playerPosition;
        this.playerRanking = playerRanking;
        this.color = color;
    }

    @Override
    public void processMessage(ServerHandler serverHandler) {
        serverHandler.getClientController().updateFlightboard(playerName,color,playerPosition, playerRanking);
    }
}
