package it.polimi.ingsw.galaxytruckerproject.network.Socket.ServerMessage;

import it.polimi.ingsw.galaxytruckerproject.network.Socket.ServerHandler;

public class GainedCreditsMessage extends  ServerMessage {

    private String playerName;

    private int credits;

    public GainedCreditsMessage(String playerName, int credits) {
        this.playerName = playerName;
        this.credits = credits;
    }

    @Override
    public void processMessage(ServerHandler serverHandler) {
        serverHandler.getClientController().gainCredit(playerName, credits);
    }
}
