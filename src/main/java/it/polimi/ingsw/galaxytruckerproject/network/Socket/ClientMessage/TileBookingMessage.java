package it.polimi.ingsw.galaxytruckerproject.network.Socket.ClientMessage;

import it.polimi.ingsw.galaxytruckerproject.network.Socket.ClientHandler;

/**
 * Represents a message sent by the client to book a tile in the game.
 * This message triggers the server to handle the action of booking a tile.
 */
public class TileBookingMessage extends ClientMessage {

    /**
     * Constructs a new TileBookingMessage.
     * This constructor is empty as the message does not carry any data.
     */
    public TileBookingMessage() {
        ;
    }

    /**
     * Processes the tile booking message by invoking the appropriate method on the client
     * handler's controller to book a tile.
     *
     * @param clientHandler The client handler that processes the message.
     */
    @Override
    public void processMessage(ClientHandler clientHandler) {
        try {
            clientHandler.getController().bookTile();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
