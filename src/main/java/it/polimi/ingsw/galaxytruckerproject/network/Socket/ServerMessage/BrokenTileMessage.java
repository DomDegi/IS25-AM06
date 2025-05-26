package it.polimi.ingsw.galaxytruckerproject.network.Socket.ServerMessage;

import it.polimi.ingsw.galaxytruckerproject.client.ClientController;
import it.polimi.ingsw.galaxytruckerproject.model.tiles.Coordinates;
import it.polimi.ingsw.galaxytruckerproject.network.Socket.ServerHandler;

import java.rmi.RemoteException;
import java.util.ArrayList;

/**
 * Represents a message sent by the server to notify the client about broken tiles on the ship.
 * This message includes the player's name and the list of coordinates of the broken tiles.
 */
public class BrokenTileMessage extends ServerMessage {

    /**
     * The name of the player who has broken tiles on their ship.
     */
    private String playerName;

    /**
     * The list of coordinates representing the broken tiles on the ship.
     */
    private ArrayList<Coordinates> brokenTiles;

    /**
     * Constructs a new BrokenTileMessage with the specified player name and broken tiles.
     *
     * @param playerName The name of the player who has broken tiles.
     * @param brokenTiles The list of coordinates representing the broken tiles.
     */
    public BrokenTileMessage(String playerName, ArrayList<Coordinates> brokenTiles) {
        this.playerName = playerName;
        this.brokenTiles = brokenTiles;
    }

    /**
     * Processes the broken tile message by updating the client's state with the broken tiles
     * and printing the shipboard if the current player is the one with broken tiles.
     *
     * @param serverHandler The server handler that processes the message.
     */
    @Override
    public void processMessage(ServerHandler serverHandler) {
        ClientController clientController = serverHandler.getClientController();
        clientController.brokenTiles(playerName, brokenTiles);

        // If the current player is the one with broken tiles, update and display the shipboard
        if (playerName.equals(clientController.getName())) {
            clientController.getView().printShipboard(clientController.getLightShipBoard());
        }
    }
}
