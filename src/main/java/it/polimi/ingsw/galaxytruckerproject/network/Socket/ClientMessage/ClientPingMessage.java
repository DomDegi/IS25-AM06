package it.polimi.ingsw.galaxytruckerproject.network.Socket.ClientMessage;

import it.polimi.ingsw.galaxytruckerproject.network.Socket.ClientHandler;

import java.rmi.RemoteException;

/**
 * Represents a ping message sent by the client to check the connectivity with the server.
 * This message triggers the server to respond, verifying that the client-server connection is active.
 */
public class ClientPingMessage extends ClientMessage {

    /**
     * Constructs a new ClientPingMessage.
     * This constructor is empty as the message does not carry any data.
     */
    public ClientPingMessage() {}

    /**
     * Processes the ping message by invoking the appropriate method on the client handler's
     * controller to trigger the server's response, verifying connectivity.
     *
     * @param clientHandler The client handler that processes the message.
     * @throws RuntimeException if an error occurs during message processing.
     */
    @Override
    public void processMessage(ClientHandler clientHandler) {
        try {
            clientHandler.getController().ping();
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }
}
