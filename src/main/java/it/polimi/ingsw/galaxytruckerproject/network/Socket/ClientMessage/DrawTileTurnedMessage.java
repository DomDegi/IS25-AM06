package it.polimi.ingsw.galaxytruckerproject.network.Socket.ClientMessage;

import it.polimi.ingsw.galaxytruckerproject.network.Socket.ClientHandler;

/**
 * Represents a message sent by the client to request drawing a tile from the turned tiles stack.
 * This message includes the index of the tile in the turned stack that is to be drawn.
 */
public class DrawTileTurnedMessage extends ClientMessage {

    /**
     * The index of the tile to be drawn from the turned tiles stack.
     */
    private int index;

    /**
     * Constructs a new DrawTileTurnedMessage with the specified index for the turned tile.
     *
     * @param index The index of the tile to be drawn from the turned tiles stack.
     */
    public DrawTileTurnedMessage(int index) {
        this.index = index;
    }

    /**
     * Processes the draw tile turned message by invoking the appropriate method on the client
     * handler's controller to draw the specified tile from the turned stack.
     *
     * @param clientHandler The client handler that processes the message.
     */
    @Override
    public void processMessage(ClientHandler clientHandler) {
        try {
            clientHandler.getController().drawTileFromTurned(index);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
