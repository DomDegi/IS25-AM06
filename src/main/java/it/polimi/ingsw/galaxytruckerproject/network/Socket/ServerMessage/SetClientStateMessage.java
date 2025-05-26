package it.polimi.ingsw.galaxytruckerproject.network.Socket.ServerMessage;

import it.polimi.ingsw.galaxytruckerproject.client.ClientState;
import it.polimi.ingsw.galaxytruckerproject.network.Socket.ServerHandler;

import java.rmi.RemoteException;

/**
 * Represents a message sent by the server to set the client's state.
 * This message includes the new client state that should be set on the client side.
 */
public class SetClientStateMessage extends ServerMessage {

    /**
     * The new client state to be set.
     */
    ClientState clientState;

    /**
     * Constructs a new SetClientStateMessage with the specified client state.
     *
     * @param clientState The new client state to be set.
     */
    public SetClientStateMessage(ClientState clientState) {
        this.clientState = clientState;
    }

    /**
     * Processes the set client state message by invoking the appropriate method on the client controller
     * to update the client's state.
     *
     * @param serverHandler The server handler that processes the message.
     */
    @Override
    public void processMessage(ServerHandler serverHandler) {
        serverHandler.getClientController().setState(clientState);
    }
}
