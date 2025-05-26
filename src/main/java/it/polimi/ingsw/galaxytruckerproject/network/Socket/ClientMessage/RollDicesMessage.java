package it.polimi.ingsw.galaxytruckerproject.network.Socket.ClientMessage;

import it.polimi.ingsw.galaxytruckerproject.network.Socket.ClientHandler;

/**
 * Represents a message sent by the client to request rolling the dice in the game.
 * This message triggers the server to handle the action of rolling the dice.
 */
public class RollDicesMessage extends ClientMessage {

    /**
     * Constructs a new RollDicesMessage.
     * This constructor is empty as the message does not carry any data.
     */
    public RollDicesMessage() {
        ;
    }

    /**
     * Processes the roll dice message by invoking the appropriate method on the client
     * handler's controller to roll the dice.
     *
     * @param clientHandler The client handler that processes the message.
     */
    @Override
    public void processMessage(ClientHandler clientHandler) {
        try {
            clientHandler.getController().rollTheDices();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
