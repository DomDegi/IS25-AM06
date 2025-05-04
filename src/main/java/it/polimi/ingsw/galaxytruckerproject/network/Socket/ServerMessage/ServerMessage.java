package it.polimi.ingsw.galaxytruckerproject.network.Socket.ServerMessage;

import it.polimi.ingsw.galaxytruckerproject.network.Socket.ClientHandler;
import it.polimi.ingsw.galaxytruckerproject.network.Socket.Message;
import it.polimi.ingsw.galaxytruckerproject.network.Socket.ServerHandler;

public abstract class ServerMessage extends Message {

    public abstract void processMessage(ServerHandler serverHandler);
}
