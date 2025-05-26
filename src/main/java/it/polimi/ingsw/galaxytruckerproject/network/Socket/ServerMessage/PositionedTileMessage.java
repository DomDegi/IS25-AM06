package it.polimi.ingsw.galaxytruckerproject.network.Socket.ServerMessage;

import it.polimi.ingsw.galaxytruckerproject.model.tiles.Tile;
import it.polimi.ingsw.galaxytruckerproject.network.Socket.ServerHandler;

/**
 * Represents a message sent by the server to notify the client about a tile that has been positioned.
 * This message includes the player's name and the tile that has been placed on the board.
 */
public class PositionedTileMessage extends ServerMessage {

    /**
     * The name of the player who positioned the tile.
     */
    private String playerName;

    /**
     * The tile that has been positioned on the board.
     */
    private Tile tile;

    /**
     * Constructs a new PositionedTileMessage with the specified player name and tile.
     *
     * @param playerName The name of the player who positioned the tile.
     * @param tile The tile that has been positioned on the board.
     */
    public PositionedTileMessage(String playerName, Tile tile) {
        this.playerName = playerName;
        this.tile = tile;
    }

    /**
     * Processes the positioned tile message by invoking the appropriate method on the client controller
     * to update the board with the positioned tile.
     *
     * @param serverHandler The server handler that processes the message.
     */
    @Override
    public void processMessage(ServerHandler serverHandler) {
        serverHandler.getClientController().setTile(playerName, tile);
    }
}
