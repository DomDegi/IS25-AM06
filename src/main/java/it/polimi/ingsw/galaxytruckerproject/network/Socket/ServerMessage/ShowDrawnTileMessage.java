package it.polimi.ingsw.galaxytruckerproject.network.Socket.ServerMessage;

import it.polimi.ingsw.galaxytruckerproject.model.tiles.Tile;
import it.polimi.ingsw.galaxytruckerproject.network.Socket.ServerHandler;

/**
 * Represents a message sent by the server to notify the client about a tile that has been drawn.
 * This message includes the drawn tile to be displayed to the client and added to their hand.
 */
public class ShowDrawnTileMessage extends ServerMessage {

    /**
     * The tile that has been drawn.
     */
    private Tile drawnTile;

    /**
     * Constructs a new ShowDrawnTileMessage with the specified drawn tile.
     *
     * @param drawnTile The tile that has been drawn and should be shown to the client.
     */
    public ShowDrawnTileMessage(Tile drawnTile) {
        this.drawnTile = drawnTile;
    }

    /**
     * Processes the show drawn tile message by invoking the appropriate methods on the server's view and client controller.
     * This displays the drawn tile to the client and adds it to the client's hand.
     *
     * @param serverHandler The server handler that processes the message.
     */
    @Override
    public void processMessage(ServerHandler serverHandler) {
        try {
            // Show the drawn tile to the client
            serverHandler.getView().showDrawnTile(drawnTile);
            // Add the drawn tile to the client's hand
            serverHandler.getClientController().setTileInHand(drawnTile);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
