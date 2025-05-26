package it.polimi.ingsw.galaxytruckerproject.network.Socket.ClientMessage;

import it.polimi.ingsw.galaxytruckerproject.network.Socket.ClientHandler;

/**
 * Represents a message sent by the client to signal that the player's ship is completed.
 * This message triggers the server to handle the completion of the ship.
 */
public class CompletedShipMessage extends ClientMessage {

    /**
     * Constructs a new CompletedShipMessage.
     * This constructor is empty as the message does not carry any data.
     */
    public CompletedShipMessage() {}

    /**
     * Processes the completed ship message by invoking the appropriate method on the client
     * handler's controller, signaling the completion of the ship.
     *
     * @param clientHandler The client handler that processes the message.
     * @throws RuntimeException if an error occurs during message processing.
     */
    @Override
    public void processMessage(ClientHandler clientHandler) {
        try {
            clientHandler.getController().completedShip();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
