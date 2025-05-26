package it.polimi.ingsw.galaxytruckerproject.network.Socket.ClientMessage;

import it.polimi.ingsw.galaxytruckerproject.model.tiles.Coordinates;
import it.polimi.ingsw.galaxytruckerproject.network.Socket.ClientHandler;

import java.util.ArrayList;

/**
 * Represents a message sent by the client to remove crew members from specific locations.
 * This message includes the coordinates of the locations from which the crew members should be removed.
 */
public class RemoveCrewMessage extends ClientMessage {

    /**
     * The list of coordinates where crew members should be removed.
     */
    private ArrayList<Coordinates> toRemoveFrom;

    /**
     * Constructs a new RemoveCrewMessage with the specified coordinates for removal.
     *
     * @param toRemoveFrom The list of coordinates where crew members should be removed.
     */
    public RemoveCrewMessage(ArrayList<Coordinates> toRemoveFrom) {
        this.toRemoveFrom = toRemoveFrom;
    }

    /**
     * Processes the remove crew message by invoking the appropriate method on the client
     * handler's controller to remove the crew members from the specified coordinates.
     *
     * @param clientHandler The client handler that processes the message.
     * @throws RuntimeException if an error occurs during message processing.
     */
    @Override
    public void processMessage(ClientHandler clientHandler) {
        try {
            clientHandler.getController().removeCrew(toRemoveFrom);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
