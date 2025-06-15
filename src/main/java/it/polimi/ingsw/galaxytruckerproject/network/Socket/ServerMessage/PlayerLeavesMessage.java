package it.polimi.ingsw.galaxytruckerproject.network.Socket.ServerMessage;


import it.polimi.ingsw.galaxytruckerproject.network.Socket.ServerHandler;

public class PlayerLeavesMessage extends ServerMessage {

    private final String playerName;

    public PlayerLeavesMessage(String playerName) {
        this.playerName = playerName;
    }

    @Override
    public void processMessage(ServerHandler serverHandler) {
        serverHandler.getView().notifyPlayerLeftTheGame(playerName);
    }
}
