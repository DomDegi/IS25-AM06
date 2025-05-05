package it.polimi.ingsw.galaxytruckerproject.network.Socket.ServerMessage;

import it.polimi.ingsw.galaxytruckerproject.network.Socket.ServerHandler;

import java.io.IOException;
import java.net.Socket;
import java.rmi.RemoteException;

public class AskColorMessage extends ServerMessage {

    public AskColorMessage() {
       ;
    }

    public void processMessage(ServerHandler serverHandler) {
        try {
            serverHandler.getView().askColor();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

}
