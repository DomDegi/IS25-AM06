package it.polimi.ingsw.galaxytruckerproject.network.Socket.ServerMessage;

import it.polimi.ingsw.galaxytruckerproject.network.Socket.ServerHandler;

import java.rmi.RemoteException;

/**
 * Represents a message sent by the server to notify the client of an error.
 * This message includes the error message that needs to be displayed to the client.
 */
public class ErrorMessage extends ServerMessage {

    /**
     * The error message to be displayed to the client.
     */
    private String errorMessage;

    /**
     * Constructs a new ErrorMessage with the specified error message.
     *
     * @param errorMessage The error message to be displayed to the client.
     */
    public ErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    /**
     * Processes the error message by invoking the appropriate method on the server's view
     * to display the error message to the client.
     *
     * @param serverHandler The server handler that processes the message.
     * @throws RuntimeException if an error occurs while displaying the error message.
     */
    @Override
    public void processMessage(ServerHandler serverHandler) {
        try {
            serverHandler.getView().showErrorMessage(errorMessage);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }
}
