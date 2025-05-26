package it.polimi.ingsw.galaxytruckerproject.network.Socket.ServerMessage;

import it.polimi.ingsw.galaxytruckerproject.network.Socket.Message;
import it.polimi.ingsw.galaxytruckerproject.network.Socket.ServerHandler;

/**
 * Represents a base class for all server messages.
 * Server messages are sent from the server to the client and may contain various types of data.
 * This class is abstract and requires subclasses to implement the {@link #processMessage(ServerHandler)} method
 * for processing the message on the client side.
 */
public abstract class ServerMessage extends Message {

    /**
     * Processes the server message by invoking the appropriate logic on the client handler.
     * This method must be implemented by subclasses to define how each message should be handled on the client side.
     *
     * @param serverHandler The server handler that processes the message and forwards it to the client controller.
     */
    public abstract void processMessage(ServerHandler serverHandler);
}
