package it.polimi.ingsw.galaxytruckerproject.network.Socket.ClientMessage;

import it.polimi.ingsw.galaxytruckerproject.model.tiles.Coordinates;
import it.polimi.ingsw.galaxytruckerproject.network.Socket.ClientHandler;

import java.util.ArrayList;

/**
 * Represents a message sent by the client to use batteries at specific coordinates in the game.
 * This message includes the coordinates of the locations where batteries are to be used.
 */
public class UseBatteryMessage extends ClientMessage {

    /**
     * The list of coordinates where batteries should be used.
     */
    private ArrayList<Coordinates> coord;

    /**
     * Constructs a new UseBatteryMessage with the specified coordinates for battery use.
     *
     * @param coord The list of coordinates where batteries should be used.
     */
    public UseBatteryMessage(ArrayList<Coordinates> coord) {
        this.coord = coord;
    }

    /**
     * Processes the use battery message by invoking the appropriate method on the client
     * handler's controller to use the batteries at the specified coordinates.
     *
     * @param clientHandler The client handler that processes the message.
     * @throws RuntimeException if an error occurs during message processing.
     */
    @Override
    public void processMessage(ClientHandler clientHandler) {
        try {
            clientHandler.getController().useBatteries(coord);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
