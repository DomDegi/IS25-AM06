package it.polimi.ingsw.galaxytruckerproject.network.Socket.ServerMessage;

import it.polimi.ingsw.galaxytruckerproject.model.tiles.Tile;
import it.polimi.ingsw.galaxytruckerproject.network.Socket.ServerHandler;

/**
 * Represents a message sent by the server to notify the client about a booked tile.
 * This message includes the player's name and the tile that has been booked.
 */
public class BookedTileMessage extends ServerMessage {

    /**
     * The name of the player who booked the tile.
     */
    private final String playerName;

    /**
     * The tile that has been booked by the player.
     */
    private final Tile bookedTile;

    /**
     * Constructs a new BookedTileMessage with the specified player name and booked tile.
     *
     * @param playerName The name of the player who booked the tile.
     * @param bookedTile The tile that has been booked by the player.
     */
    public BookedTileMessage(String playerName, Tile bookedTile) {
        this.playerName = playerName;
        this.bookedTile = bookedTile;
    }

    /**
     * Processes the booked tile message by adding the booked tile to the client's list of booked tiles.
     * This method invokes the appropriate method on the client controller to handle the booking.
     *
     * @param serverHandler The server handler that processes the message.
     */
    @Override
    public void processMessage(ServerHandler serverHandler) {
        serverHandler.getClientController().addBookedTile(playerName, bookedTile);
    }
}
