package it.polimi.ingsw.galaxytruckerproject.network.Socket.ClientMessage;

import it.polimi.ingsw.galaxytruckerproject.network.Socket.Message;

public abstract class ClientMessage extends Message{

    public abstract void processMessage(ClientMessage clientHandler);
}
