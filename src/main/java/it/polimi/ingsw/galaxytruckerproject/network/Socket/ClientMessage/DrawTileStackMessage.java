package it.polimi.ingsw.galaxytruckerproject.network.Socket.ClientMessage;

import it.polimi.ingsw.galaxytruckerproject.network.Socket.ClientHandler;

import java.net.Socket;

/**
 * Represents a message sent by the client to request drawing a tile from the stack.
 * This message triggers the server to handle the action of drawing a tile from the tile stack.
 */
public class DrawTileStackMessage extends ClientMessage {

    /**
     * Constructs a new DrawTileStackMessage.
     * This constructor is empty as the message does not carry any data.
     */
    public DrawTileStackMessage() {
        ;
    }

    /**
     * Processes the draw tile stack message by invoking the appropriate method on the client
     * handler's controller to draw a tile from the stack.
     *
     * @param clientHandler The client handler that processes the message.
     */
    public void processMessage(ClientHandler clientHandler) {
        try {
            clientHandler.getController().drawTileFromStack();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
