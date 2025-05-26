package it.polimi.ingsw.galaxytruckerproject.network.Socket.ServerMessage;

import it.polimi.ingsw.galaxytruckerproject.network.Socket.ServerHandler;

import java.rmi.RemoteException;

/**
 * Represents a message sent by the server to notify the client that the hourglass has been turned.
 * This message includes the number of turns that have been completed.
 */
public class TurnedHourglassMessage extends ServerMessage {

    /**
     * The number of turns that have been completed for the hourglass.
     */
    private int turns;

    /**
     * Constructs a new TurnedHourglassMessage with the specified number of turns.
     *
     * @param turns The number of turns that have been completed for the hourglass.
     */
    public TurnedHourglassMessage(int turns) {
        this.turns = turns;
    }

    /**
     * Processes the turned hourglass message by invoking the appropriate method on the server's view
     * to notify the client that the hourglass has been turned and the number of turns completed.
     *
     * @param serverHandler The server handler that processes the message.
     */
    @Override
    public void processMessage(ServerHandler serverHandler) {
        try {
            serverHandler.getView().notifyTurnedHourglass(turns);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}
