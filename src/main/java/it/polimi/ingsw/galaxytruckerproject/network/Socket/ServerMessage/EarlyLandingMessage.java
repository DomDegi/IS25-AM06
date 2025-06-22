package it.polimi.ingsw.galaxytruckerproject.network.Socket.ServerMessage;

import it.polimi.ingsw.galaxytruckerproject.client.ClientState;
import it.polimi.ingsw.galaxytruckerproject.network.Socket.ServerHandler;

import java.rmi.RemoteException;

/**
 * Represents a message sent by the server to notify the client about an early landing.
 * This message triggers the server to notify the client that an early landing has occurred.
 */
public class EarlyLandingMessage extends ServerMessage {

    /**
     * Processes the early landing message by notifying the client about the early landing
     * and updating the client's state to "WAIT".
     *
     * @param serverHandler The server handler that processes the message.
     */
    @Override
    public void processMessage(ServerHandler serverHandler) {
        try {
            // Notify the client about the early landing.
            serverHandler.getView().notifyEarlyLanding();
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }

        // Set the client's state to WAIT after the early landing notification.
        serverHandler.getClientController().setState(ClientState.LANDING);
    }
}
