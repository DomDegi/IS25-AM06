package it.polimi.ingsw.galaxytruckerproject.network.Socket.ClientMessage;

import it.polimi.ingsw.galaxytruckerproject.model.tiles.Coordinates;
import it.polimi.ingsw.galaxytruckerproject.network.Socket.ClientHandler;

import java.util.ArrayList;

/**
 * Represents a message sent by the client to manage ship errors.
 * This message includes the coordinates of the tiles that need to be removed as part of error management.
 */
public class ShipErrorManagementMessage extends ClientMessage {

    /**
     * The list of coordinates representing the tiles to be removed in response to the ship error.
     */
    private ArrayList<Coordinates> toRemove;

    /**
     * Constructs a new ShipErrorManagementMessage with the specified coordinates to be removed.
     *
     * @param toRemove The list of coordinates representing the tiles to be removed.
     */
    public ShipErrorManagementMessage(ArrayList<Coordinates> toRemove) {
        this.toRemove = toRemove;
    }

    /**
     * Processes the ship error management message by invoking the appropriate method on the client
     * handler's controller to manage the ship error and remove the specified tiles.
     *
     * @param clientHandler The client handler that processes the message.
     * @throws RuntimeException if an error occurs during message processing.
     */
    @Override
    public void processMessage(ClientHandler clientHandler) {
        try {
            clientHandler.getController().shipErrorManagement(toRemove);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
