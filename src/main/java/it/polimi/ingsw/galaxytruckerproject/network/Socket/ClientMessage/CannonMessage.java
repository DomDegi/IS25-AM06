package it.polimi.ingsw.galaxytruckerproject.network.Socket.ClientMessage;

import it.polimi.ingsw.galaxytruckerproject.model.tiles.Coordinates;
import it.polimi.ingsw.galaxytruckerproject.network.Socket.ClientHandler;

import java.rmi.RemoteException;
import java.util.ArrayList;

/**
 * Represents a message containing information about cannon usage in the game.
 * This message is sent by the client to trigger cannon usage, specifying the
 * firepower and target coordinates.
 */
public class CannonMessage extends ClientMessage {

    /**
     * The power of the cannon shot.
     */
    private float firePower;

    /**
     * The list of coordinates where the cannon shot is aimed.
     */
    private ArrayList<Coordinates> coordinates;

    /**
     * Constructs a new CannonMessage with the specified fire power and target coordinates.
     *
     * @param firePower The power of the cannon shot.
     * @param coordinates The list of coordinates where the cannon shot is aimed.
     */
    public CannonMessage(float firePower, ArrayList<Coordinates> coordinates) {
        this.firePower = firePower;
        this.coordinates = coordinates;
    }

    /**
     * Processes the cannon message by invoking the appropriate method on the
     * client handler's controller, which handles the cannon action.
     *
     * @param clientHandler The client handler that processes the message.
     * @throws RuntimeException if an error occurs during message processing.
     */
    @Override
    public void processMessage(ClientHandler clientHandler) {
        try {
            clientHandler.getController().useCannons(firePower, coordinates);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }
}
