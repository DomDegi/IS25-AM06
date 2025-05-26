package it.polimi.ingsw.galaxytruckerproject.network.Socket.ServerMessage;

import it.polimi.ingsw.galaxytruckerproject.network.Socket.ServerHandler;

import java.rmi.RemoteException;

/**
 * Represents a generic message sent by the server to notify the client with a custom message.
 * This message includes a generic message string to be displayed to the client.
 */
public class GenericServerMessage extends ServerMessage {

    /**
     * The generic message to be displayed to the client.
     */
    private String genericMessage;

    /**
     * Constructs a new GenericServerMessage with the specified message.
     *
     * @param genericMessage The message to be displayed to the client.
     */
    public GenericServerMessage(String genericMessage) {
        this.genericMessage = genericMessage;
    }

    /**
     * Processes the generic server message by invoking the appropriate method on the server's view
     * to display the generic message to the client.
     *
     * @param serverHandler The server handler that processes the message.
     */
    @Override
    public void processMessage(ServerHandler serverHandler) {
        serverHandler.getView().showGenericMessage(genericMessage);
    }
}
