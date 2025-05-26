package it.polimi.ingsw.galaxytruckerproject.network.Socket.ClientMessage;

import it.polimi.ingsw.galaxytruckerproject.network.Socket.ClientHandler;

/**
 * Represents a message sent by the client to request the drawing of a card.
 * This message triggers the server to handle the action of drawing a card.
 */
public class DrawCardMessage extends ClientMessage {

    /**
     * Constructs a new DrawCardMessage.
     * This constructor is empty as the message does not carry any data.
     */
    public DrawCardMessage() {}

    /**
     * Processes the draw card message by invoking the appropriate method on the client
     * handler's controller to trigger the drawing of a card.
     *
     * @param clientHandler The client handler that processes the message.
     * @throws RuntimeException if an error occurs during message processing.
     */
    @Override
    public void processMessage(ClientHandler clientHandler) {
        try {
            clientHandler.getController().drawCard();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
