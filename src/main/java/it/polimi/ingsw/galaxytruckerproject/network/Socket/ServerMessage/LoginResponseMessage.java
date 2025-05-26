package it.polimi.ingsw.galaxytruckerproject.network.Socket.ServerMessage;

import it.polimi.ingsw.galaxytruckerproject.network.Socket.ServerHandler;

import java.rmi.RemoteException;

/**
 * Represents a message sent by the server in response to a client's login attempt.
 * This message includes a boolean indicating whether the login was successful.
 */
public class LoginResponseMessage extends ServerMessage {

    /**
     * A boolean indicating the success or failure of the login attempt.
     */
    private boolean success;

    /**
     * Constructs a new LoginResponseMessage with the specified login success status.
     *
     * @param success A boolean indicating if the login attempt was successful.
     */
    public LoginResponseMessage(boolean success) {
        this.success = success;
    }

    /**
     * Processes the login response message by invoking the appropriate method on the server's view
     * to display the login response to the client.
     *
     * @param serverHandler The server handler that processes the message.
     */
    @Override
    public void processMessage(ServerHandler serverHandler) {
        try {
            serverHandler.getView().showLoginResponse(success);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}
