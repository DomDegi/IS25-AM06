package it.polimi.ingsw.galaxytruckerproject.network.Socket.ClientMessage;

import it.polimi.ingsw.galaxytruckerproject.network.Socket.ClientHandler;

/**
 * Represents a message sent by the client to stop looking at the cards.
 * This message triggers the server to handle the action of stopping the viewing of cards.
 */
public class StopLookingCardsMessage extends ClientMessage {

    /**
     * Constructs a new StopLookingCardsMessage.
     * This constructor is empty as the message does not carry any data.
     */
    public StopLookingCardsMessage() {
        ;
    }

    /**
     * Processes the stop looking at cards message by invoking the appropriate method on the client
     * handler's controller to stop viewing the cards.
     *
     * @param clientHandler The client handler that processes the message.
     */
    public void processMessage(ClientHandler clientHandler) {
        try {
            clientHandler.getController().stopLookingAtCards();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
