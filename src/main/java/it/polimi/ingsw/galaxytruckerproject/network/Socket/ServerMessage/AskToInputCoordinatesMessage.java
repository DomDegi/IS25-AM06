package it.polimi.ingsw.galaxytruckerproject.network.Socket.ServerMessage;

import it.polimi.ingsw.galaxytruckerproject.client.ClientState;
import it.polimi.ingsw.galaxytruckerproject.client.CoordReqType;
import it.polimi.ingsw.galaxytruckerproject.network.Socket.ServerHandler;

import java.rmi.RemoteException;

/**
 * Represents a message sent by the server to ask the client to input coordinates.
 * This message triggers the server to request the client to provide coordinates based on a specific request type.
 */
public class AskToInputCoordinatesMessage extends ServerMessage {

    /**
     * The type of coordinate request (e.g., a specific action or type of coordinates).
     */
    CoordReqType coordReqType;

    /**
     * Constructs a new AskToInputCoordinatesMessage with the specified coordinate request type.
     *
     * @param coordReqType The type of coordinate request (e.g., for movement or placement).
     */
    public AskToInputCoordinatesMessage(CoordReqType coordReqType) {
        this.coordReqType = coordReqType;
    }

    /**
     * Processes the ask to input coordinates message by setting the appropriate client state,
     * setting the coordinate request type, and invoking the method to prompt the client for coordinates.
     *
     * @param serverHandler The server handler that processes the message.
     */
    @Override
    public void processMessage(ServerHandler serverHandler) {
        try {
            serverHandler.getClientController().setState(ClientState.COORD_REQUEST);
            serverHandler.getClientController().getCoordInputManager().setCoordReqType(coordReqType);
            serverHandler.getView().asksToInputCoordinates(coordReqType);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}
