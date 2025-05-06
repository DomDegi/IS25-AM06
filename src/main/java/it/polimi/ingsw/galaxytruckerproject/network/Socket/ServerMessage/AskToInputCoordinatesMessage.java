package it.polimi.ingsw.galaxytruckerproject.network.Socket.ServerMessage;

import it.polimi.ingsw.galaxytruckerproject.client.ClientState;
import it.polimi.ingsw.galaxytruckerproject.client.CoordReqType;
import it.polimi.ingsw.galaxytruckerproject.network.Socket.ServerHandler;

import java.rmi.RemoteException;

public class AskToInputCoordinatesMessage extends ServerMessage{

    CoordReqType coordReqType;

    public AskToInputCoordinatesMessage(CoordReqType coordReqType) {
        this.coordReqType = coordReqType;
    }

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
