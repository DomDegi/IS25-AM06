package it.polimi.ingsw.galaxytruckerproject.network.Socket.ClientMessage;

import it.polimi.ingsw.galaxytruckerproject.network.Socket.Message;
import it.polimi.ingsw.galaxytruckerproject.network.Socket.ServerHandler;

public abstract class ClientMessage extends Message{

    public abstract void processMessage(ServerHandler serverHandler);
}
