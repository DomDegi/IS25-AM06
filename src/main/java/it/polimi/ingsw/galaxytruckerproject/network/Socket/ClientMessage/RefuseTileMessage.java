package it.polimi.ingsw.galaxytruckerproject.network.Socket.ClientMessage;

import it.polimi.ingsw.galaxytruckerproject.network.Socket.ClientHandler;

public class RefuseTileMessage extends ClientMessage {

    public RefuseTileMessage() {
        ;
    }

    public void processMessage(ClientHandler clientHandler) {
        try {
            clientHandler.getController().refuseTile();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
