package it.polimi.ingsw.galaxytruckerproject.network.Socket.ClientMessage;

import it.polimi.ingsw.galaxytruckerproject.network.Socket.ClientHandler;

import java.net.Socket;

public class DrawTileStackMessage extends ClientMessage {

    public DrawTileStackMessage () {
        ;
    }

    public void processMessage(ClientHandler clientHandler) {
        try {
            clientHandler.getController().drawTileFromStack();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
