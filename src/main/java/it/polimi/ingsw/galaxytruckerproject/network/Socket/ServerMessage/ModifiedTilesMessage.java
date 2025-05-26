package it.polimi.ingsw.galaxytruckerproject.network.Socket.ServerMessage;

import it.polimi.ingsw.galaxytruckerproject.model.tiles.Tile;
import it.polimi.ingsw.galaxytruckerproject.network.Socket.ServerHandler;

import java.rmi.RemoteException;
import java.util.ArrayList;

/**
 * Represents a message sent by the server to notify the client about modified tiles.
 * This message includes the player's name and the list of tiles that have been modified.
 */
public class ModifiedTilesMessage extends ServerMessage {

    /**
     * The name of the player whose tiles have been modified.
     */
    private String playerName;

    /**
     * The list of tiles that have been modified.
     */
    private ArrayList<Tile> tiles;

    /**
     * Constructs a new ModifiedTilesMessage with the specified player name and modified tiles.
     *
     * @param playerName The name of the player whose tiles have been modified.
     * @param tiles The list of tiles that have been modified.
     */
    public ModifiedTilesMessage(String playerName, ArrayList<Tile> tiles) {
        this.playerName = playerName;
        this.tiles = tiles;
    }

    /**
     * Processes the modified tiles message by updating the client's tiles and displaying the shipboard
     * if the current player is the one with modified tiles.
     *
     * @param serverHandler The server handler that processes the message.
     */
    @Override
    public void processMessage(ServerHandler serverHandler) {
        // Modify the tiles on the client controller
        serverHandler.getClientController().modifyTiles(playerName, tiles);

        // If the current player has modified tiles, print the updated shipboard
        if (playerName.equals(serverHandler.getClientController().getName())) {
            serverHandler.getView().printShipboard(serverHandler.getClientController().getLightShipBoard());
        }
    }
}
