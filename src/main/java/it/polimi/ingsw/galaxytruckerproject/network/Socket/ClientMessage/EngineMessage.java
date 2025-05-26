package it.polimi.ingsw.galaxytruckerproject.network.Socket.ClientMessage;

import it.polimi.ingsw.galaxytruckerproject.model.tiles.Coordinates;
import it.polimi.ingsw.galaxytruckerproject.network.Socket.ClientHandler;

import java.util.ArrayList;

/**
 * Represents a message sent by the client to use the engines in the game.
 * This message includes the number of double engines to be used and the coordinates of the engines.
 */
public class EngineMessage extends ClientMessage {

    /**
     * The number of double engines to be used.
     */
    private int numDoubleEngine;

    /**
     * The list of coordinates where the engines are located.
     */
    private ArrayList<Coordinates> coordinates;

    /**
     * Constructs a new EngineMessage with the specified number of double engines and their coordinates.
     *
     * @param numDoubleEngine The number of double engines to be used.
     * @param coordinates The list of coordinates where the engines are located.
     */
    public EngineMessage(int numDoubleEngine, ArrayList<Coordinates> coordinates) {
        this.numDoubleEngine = numDoubleEngine;
        this.coordinates = coordinates;
    }

    /**
     * Processes the engine message by invoking the appropriate method on the client
     * handler's controller to activate the engines at the specified coordinates.
     *
     * @param clientHandler The client handler that processes the message.
     */
    @Override
    public void processMessage(ClientHandler clientHandler) {
        try {
            clientHandler.getController().useEngines(numDoubleEngine, coordinates);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
