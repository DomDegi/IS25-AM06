package it.polimi.ingsw.galaxytruckerproject.network.Socket.ClientMessage;

import it.polimi.ingsw.galaxytruckerproject.network.Socket.ClientHandler;

/**
 * Represents a message sent by the client to indicate a "No" response.
 * This message triggers the server to handle the action based on the client's "No" choice.
 */
public class SendNoMessage extends ClientMessage {

    /**
     * Constructs a new SendNoMessage.
     * This constructor is empty as the message does not carry any additional data.
     */
    public SendNoMessage() {
        ;
    }

    /**
     * Processes the "No" response message by invoking the appropriate method on the client
     * handler's controller to make a choice based on the client's "No" response.
     *
     * @param clientHandler The client handler that processes the message.
     */
    @Override
    public void processMessage(ClientHandler clientHandler) {
        try {
            clientHandler.getController().makeAChoice(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
