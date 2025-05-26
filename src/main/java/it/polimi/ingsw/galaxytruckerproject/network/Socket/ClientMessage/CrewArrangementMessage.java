package it.polimi.ingsw.galaxytruckerproject.network.Socket.ClientMessage;

import it.polimi.ingsw.galaxytruckerproject.model.tiles.Tile;
import it.polimi.ingsw.galaxytruckerproject.network.Socket.ClientHandler;

import java.util.ArrayList;

/**
 * Represents a message sent by the client to arrange crew members in the player's ship.
 * This message includes the updated cabins with the assigned crew members.
 */
public class CrewArrangementMessage extends ClientMessage {

    /**
     * The list of updated cabins with assigned crew members.
     */
    private ArrayList<Tile> updatedCabins;

    /**
     * Constructs a new CrewArrangementMessage with the specified updated cabins.
     *
     * @param updatedCabins The list of updated cabins with assigned crew members.
     */
    public CrewArrangementMessage(ArrayList<Tile> updatedCabins) {
        this.updatedCabins = updatedCabins;
    }

    /**
     * Processes the crew arrangement message by invoking the appropriate method on the
     * client handler's controller to assign the crew members to the updated cabins.
     *
     * @param clientHandler The client handler that processes the message.
     * @throws RuntimeException if an error occurs during message processing.
     */
    @Override
    public void processMessage(ClientHandler clientHandler) {
        try {
            clientHandler.getController().pickCrewMembers(updatedCabins);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
