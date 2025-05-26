package it.polimi.ingsw.galaxytruckerproject.network.Socket.ServerMessage;

import it.polimi.ingsw.galaxytruckerproject.model.tiles.Tile;
import it.polimi.ingsw.galaxytruckerproject.network.Socket.ServerHandler;

/**
 * Represents a message sent by the server to notify the client that a previously booked tile has been removed.
 * This message includes the player's name and the tile that was removed from the booked tiles.
 */
public class RemovedBookedTileMessage extends ServerMessage {

    /**
     * The name of the player whose booked tile has been removed.
     */
    private String playerName;

    /**
     * The tile that was removed from the booked tiles.
     */
    private Tile tile;

    /**
     * Constructs a new RemovedBookedTileMessage with the specified player name and tile.
     *
     * @param playerName The name of the player whose booked tile is being removed.
     * @param tile The tile that is being removed from the booked tiles.
     */
    public RemovedBookedTileMessage(String playerName, Tile tile) {
        this.playerName = playerName;
        this.tile = tile;
    }

    /**
     * Processes the removed booked tile message by invoking the appropriate method on the client controller
     * to remove the booked tile.
     *
     * @param serverHandler The server handler that processes the message.
     */
    @Override
    public void processMessage(ServerHandler serverHandler) {
        serverHandler.getClientController().removeBookedTile(playerName, tile);
    }
}
