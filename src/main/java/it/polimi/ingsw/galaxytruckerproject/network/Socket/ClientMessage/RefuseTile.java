package it.polimi.ingsw.galaxytruckerproject.network.Socket.ClientMessage;

import it.polimi.ingsw.galaxytruckerproject.network.Socket.ClientHandler;

public class RefuseTile extends ClientMessage {

    public RefuseTile() {
        ;
    }

    public void processMessage(ClientHandler clientHandler) {
        try {
            clientHandler.getController().refuseTile();
        } catch (Exception e) {
            ;
        }
    }
}
