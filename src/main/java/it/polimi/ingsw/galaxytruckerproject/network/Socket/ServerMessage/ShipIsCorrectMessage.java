package it.polimi.ingsw.galaxytruckerproject.network.Socket.ServerMessage;

import it.polimi.ingsw.galaxytruckerproject.network.Socket.ServerHandler;

import java.rmi.RemoteException;

/**
 * Represents a message sent by the server to notify the client that their ship is correctly assembled.
 * This message informs the client that the ship they have constructed or configured is correct.
 */
public class ShipIsCorrectMessage extends ServerMessage {

    /**
     * Constructs a new ShipIsCorrectMessage.
     * This constructor is empty as the message does not carry additional data.
     */
    public ShipIsCorrectMessage() {}

    /**
     * Processes the "ship is correct" message by invoking the appropriate method on the server's view
     * to notify the client that their ship is correctly assembled.
     *
     * @param serverHandler The server handler that processes the message.
     * @throws RuntimeException if an error occurs while invoking the view method.
     */
    @Override
    public void processMessage(ServerHandler serverHandler) {
        try {
            serverHandler.getView().notifyYourShipIsCorrect();
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }
}
