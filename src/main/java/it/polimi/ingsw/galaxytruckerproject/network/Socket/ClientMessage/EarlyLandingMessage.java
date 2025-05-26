package it.polimi.ingsw.galaxytruckerproject.network.Socket.ClientMessage;

import it.polimi.ingsw.galaxytruckerproject.network.Socket.ClientHandler;

/**
 * Represents a message sent by the client to request an early landing.
 * This message triggers the server to handle the action of performing an early landing.
 */
public class EarlyLandingMessage extends ClientMessage {

    /**
     * Constructs a new EarlyLandingMessage.
     * This constructor is empty as the message does not carry any data.
     */
    public EarlyLandingMessage() {}

    /**
     * Processes the early landing message by invoking the appropriate method on the client
     * handler's controller to perform the early landing.
     *
     * @param clientHandler The client handler that processes the message.
     */
    @Override
    public void processMessage(ClientHandler clientHandler) {
        try {
            clientHandler.getController().earlyLanding();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
