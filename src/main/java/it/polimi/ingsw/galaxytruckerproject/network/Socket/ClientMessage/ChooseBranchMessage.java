package it.polimi.ingsw.galaxytruckerproject.network.Socket.ClientMessage;

import it.polimi.ingsw.galaxytruckerproject.model.tiles.Coordinates;
import it.polimi.ingsw.galaxytruckerproject.network.Socket.ClientHandler;

import java.util.ArrayList;

/**
 * Represents a message sent by the client to choose a branch in the game.
 * This message includes the coordinates that represent the chosen branch.
 */
public class ChooseBranchMessage extends ClientMessage {

    /**
     * The list of coordinates representing the chosen branch.
     */
    private ArrayList<Coordinates> coord;

    /**
     * Constructs a new ChooseBranchMessage with the specified coordinates.
     *
     * @param coord The list of coordinates representing the chosen branch.
     */
    public ChooseBranchMessage(ArrayList<Coordinates> coord) {
        this.coord = coord;
    }

    /**
     * Processes the choose branch message by invoking the appropriate method on the
     * client handler's controller, which handles the action of choosing a branch.
     *
     * @param clientHandler The client handler that processes the message.
     * @throws RuntimeException if an error occurs during message processing.
     */
    @Override
    public void processMessage(ClientHandler clientHandler) {
        try {
            clientHandler.getController().chooseBranch(coord);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
