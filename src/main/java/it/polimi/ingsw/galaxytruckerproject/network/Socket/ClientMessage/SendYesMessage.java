package it.polimi.ingsw.galaxytruckerproject.network.Socket.ClientMessage;

import it.polimi.ingsw.galaxytruckerproject.network.Socket.ClientHandler;

import java.io.Serializable;

/**
 * Represents a message sent by the client to indicate a "Yes" response.
 * This message triggers the server to handle the action based on the client's "Yes" choice.
 */
public class SendYesMessage extends ClientMessage {

    /**
     * Constructs a new SendYesMessage.
     * This constructor is empty as the message does not carry any additional data.
     */
    public SendYesMessage() {
        ;
    }

    /**
     * Processes the "Yes" response message by invoking the appropriate method on the client
     * handler's controller to make a choice based on the client's "Yes" response.
     *
     * @param clientHandler The client handler that processes the message.
     */
    @Override
    public void processMessage(ClientHandler clientHandler) {
        try {
            clientHandler.getController().makeAChoice(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
