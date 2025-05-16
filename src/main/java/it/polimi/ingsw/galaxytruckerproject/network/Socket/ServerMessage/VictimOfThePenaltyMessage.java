package it.polimi.ingsw.galaxytruckerproject.network.Socket.ServerMessage;

import it.polimi.ingsw.galaxytruckerproject.network.Socket.ServerHandler;

public class VictimOfThePenaltyMessage extends ServerMessage {
    String playerName;
    public VictimOfThePenaltyMessage(String playerName) {
        this.playerName = playerName;
    }

    @Override
    public void processMessage(ServerHandler serverHandler) {
        serverHandler.getClientController().victimOfThePenalty(playerName);
    }
}
