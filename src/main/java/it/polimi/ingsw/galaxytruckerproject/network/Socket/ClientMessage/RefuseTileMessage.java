package it.polimi.ingsw.galaxytruckerproject.network.Socket.ClientMessage;

import it.polimi.ingsw.galaxytruckerproject.network.Socket.ClientHandler;

/**
 * Represents a message sent by the client to refuse a tile.
 * This message triggers the server to handle the refusal of the current tile.
 */
public class RefuseTileMessage extends ClientMessage {

    /**
     * Constructs a new RefuseTileMessage.
     * This constructor is empty as the message does not carry any data.
     */
    public RefuseTileMessage() {
        ;
    }

    /**
     * Processes the refuse tile message by invoking the appropriate method on the client
     * handler's controller to refuse the current tile.
     *
     * @param clientHandler The client handler that processes the message.
     */
    public void processMessage(ClientHandler clientHandler) {
        try {
            clientHandler.getController().refuseTile();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
