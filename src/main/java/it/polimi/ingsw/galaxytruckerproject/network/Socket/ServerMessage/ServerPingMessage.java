package it.polimi.ingsw.galaxytruckerproject.network.Socket.ServerMessage;

import it.polimi.ingsw.galaxytruckerproject.network.Socket.ServerHandler;

public class ServerPingMessage extends  ServerMessage {

    public ServerPingMessage() {}

    @Override
    public void processMessage(ServerHandler serverHandler) {
        serverHandler.getClientController().ping();
    }
}
