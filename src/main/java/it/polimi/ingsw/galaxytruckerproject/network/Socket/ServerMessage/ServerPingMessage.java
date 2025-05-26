package it.polimi.ingsw.galaxytruckerproject.network.Socket.ServerMessage;

import it.polimi.ingsw.galaxytruckerproject.network.Socket.ServerHandler;

/**
 * Represents a message sent by the server to ping the client.
 * This message triggers the server to check the client's connection status.
 */
public class ServerPingMessage extends ServerMessage {

    /**
     * Constructs a new ServerPingMessage.
     * This constructor is empty as the message does not carry any additional data.
     */
    public ServerPingMessage() {}

    /**
     * Processes the server ping message by invoking the appropriate method on the client controller
     * to handle the ping action. This is typically used to check the connection between the server and the client.
     *
     * @param serverHandler The server handler that processes the message.
     */
    @Override
    public void processMessage(ServerHandler serverHandler) {
        serverHandler.getClientController().ping();
    }
}
