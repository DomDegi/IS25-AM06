package it.polimi.ingsw.galaxytruckerproject.network.Socket.ServerMessage;

import it.polimi.ingsw.galaxytruckerproject.network.Socket.ServerHandler;

import java.rmi.RemoteException;

/**
 * Represents a message sent by the server to notify the client that the allotted time has expired.
 * This message informs the client that the time for their current task or action has ended.
 */
public class TimeExpiredMessage extends ServerMessage {

    /**
     * Constructs a new TimeExpiredMessage.
     * This constructor is empty as the message does not carry additional data.
     */
    public TimeExpiredMessage() {}

    /**
     * Processes the time expired message by invoking the appropriate method on the server's view
     * to notify the client that the time has expired.
     *
     * @param serverHandler The server handler that processes the message.
     */
    @Override
    public void processMessage(ServerHandler serverHandler) {
        try {
            serverHandler.getView().notifyEndOfTime();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}
