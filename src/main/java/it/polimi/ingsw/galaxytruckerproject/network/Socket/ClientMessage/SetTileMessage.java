package it.polimi.ingsw.galaxytruckerproject.network.Socket.ClientMessage;

import it.polimi.ingsw.galaxytruckerproject.model.tiles.Tile;
import it.polimi.ingsw.galaxytruckerproject.network.Socket.ClientHandler;

/**
 * Represents a message sent by the client to set a specific tile on the game board.
 * This message includes the tile to be placed on the board.
 */
public class SetTileMessage extends ClientMessage {

    /**
     * The tile to be set on the game board.
     */
    private Tile tile;

    /**
     * Constructs a new SetTileMessage with the specified tile.
     *
     * @param tile The tile to be set on the board.
     */
    public SetTileMessage(Tile tile) {
        this.tile = tile.send();
    }

    /**
     * Processes the set tile message by invoking the appropriate method on the client
     * handler's controller to set the specified tile on the board.
     *
     * @param clientHandler The client handler that processes the message.
     */
    @Override
    public void processMessage(ClientHandler clientHandler) {
        try {
            clientHandler.getController().setTile(tile);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
