package it.polimi.ingsw.galaxytruckerproject.network.Socket.ServerMessage;

import it.polimi.ingsw.galaxytruckerproject.model.tiles.Tile;
import it.polimi.ingsw.galaxytruckerproject.network.Socket.ServerHandler;

/**
 * Represents a message sent by the server to notify the client that a turned tile should be removed.
 * This message includes the tile that should be removed from the turned tiles.
 */
public class RemoveTurnedTileMessage extends ServerMessage {

    /**
     * The turned tile that should be removed.
     */
    private Tile turnedTile;

    /**
     * Constructs a new RemoveTurnedTileMessage with the specified turned tile.
     *
     * @param turnedTile The tile that should be removed from the turned tiles.
     */
    public RemoveTurnedTileMessage(Tile turnedTile) {
        this.turnedTile = turnedTile;
    }

    /**
     * Processes the remove turned tile message by invoking the appropriate method on the client controller
     * to remove the specified turned tile.
     *
     * @param serverHandler The server handler that processes the message.
     */
    @Override
    public void processMessage(ServerHandler serverHandler) {
        serverHandler.getClientController().removeTurnedTile(turnedTile);
    }
}
