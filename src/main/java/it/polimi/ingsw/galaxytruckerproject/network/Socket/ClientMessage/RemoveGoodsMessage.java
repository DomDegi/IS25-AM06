package it.polimi.ingsw.galaxytruckerproject.network.Socket.ClientMessage;

import it.polimi.ingsw.galaxytruckerproject.model.tiles.Coordinates;
import it.polimi.ingsw.galaxytruckerproject.network.Socket.ClientHandler;

import java.rmi.RemoteException;
import java.util.ArrayList;

/**
 * Represents a message sent by the client to remove goods from specific locations.
 * This message includes the coordinates of the locations from which the goods should be removed.
 */
public class RemoveGoodsMessage extends ClientMessage {

    /**
     * The list of coordinates from which goods should be removed.
     */
    private ArrayList<Coordinates> fromHere;

    /**
     * Constructs a new RemoveGoodsMessage with the specified coordinates for removal.
     *
     * @param fromHere The list of coordinates from which goods should be removed.
     * @throws RemoteException if a remote communication issue occurs.
     */
    public RemoveGoodsMessage(ArrayList<Coordinates> fromHere) throws RemoteException {
        this.fromHere = fromHere;
    }

    /**
     * Processes the remove goods message by invoking the appropriate method on the client
     * handler's controller to remove the goods from the specified coordinates.
     *
     * @param clientHandler The client handler that processes the message.
     * @throws RuntimeException if an error occurs during message processing.
     */
    @Override
    public void processMessage(ClientHandler clientHandler) {
        try {
            clientHandler.getController().removeGoods(fromHere);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
