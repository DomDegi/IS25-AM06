package it.polimi.ingsw.galaxytruckerproject.network.Socket.ServerMessage;

import it.polimi.ingsw.galaxytruckerproject.model.tiles.Tile;
import it.polimi.ingsw.galaxytruckerproject.network.Socket.ServerHandler;

/**
 * Represents a message sent by the server to notify the client about a new turned tile.
 * This message includes the tile that has been turned and needs to be added to the client's collection of turned tiles.
 */
public class NewTurnedTileMessage extends ServerMessage {

    /**
     * The tile that has been turned.
     */
    private Tile turnedTile;

    /**
     * Constructs a new NewTurnedTileMessage with the specified turned tile.
     *
     * @param turnedTile The tile that has been turned and should be added to the client's collection of turned tiles.
     */
    public NewTurnedTileMessage(Tile turnedTile) {
        this.turnedTile = turnedTile;
    }

    /**
     * Processes the new turned tile message by adding the turned tile to the client's collection of turned tiles.
     * This method invokes the appropriate method on the client controller to update the client's state.
     *
     * @param serverHandler The server handler that processes the message.
     */
    @Override
    public void processMessage(ServerHandler serverHandler) {
        serverHandler.getClientController().addTurnedTile(turnedTile);
    }
}
