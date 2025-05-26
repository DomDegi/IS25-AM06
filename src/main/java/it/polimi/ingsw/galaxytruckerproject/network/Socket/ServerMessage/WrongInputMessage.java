package it.polimi.ingsw.galaxytruckerproject.network.Socket.ServerMessage;

import it.polimi.ingsw.galaxytruckerproject.network.Socket.ServerHandler;

import java.rmi.RemoteException;

/**
 * Represents a message sent by the server to notify the client that the input provided by the client was incorrect.
 * This message prompts the client to handle the incorrect input and roll back to a previous valid state.
 */
public class WrongInputMessage extends ServerMessage {

    /**
     * Constructs a new WrongInputMessage.
     * This constructor is empty as the message does not carry additional data.
     */
    public WrongInputMessage() {}

    /**
     * Processes the wrong input message by notifying the client that their input was incorrect.
     * The client is also prompted to roll back to a previous valid state.
     *
     * @param serverHandler The server handler that processes the message.
     */
    @Override
    public void processMessage(ServerHandler serverHandler) {
        try {
            // Notify the client about the incorrect input
            serverHandler.getView().showWrongInputMessage();
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
        // Roll back the client's state to a previous valid state
        serverHandler.getClientController().rollBackState();
    }
}
