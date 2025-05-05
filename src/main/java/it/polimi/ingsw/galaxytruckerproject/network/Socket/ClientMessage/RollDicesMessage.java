package it.polimi.ingsw.galaxytruckerproject.network.Socket.ClientMessage;

import it.polimi.ingsw.galaxytruckerproject.network.Socket.ClientHandler;

public class RollDicesMessage extends ClientMessage {

    public RollDicesMessage() {
        ;
    }

    @Override
    public void processMessage(ClientHandler clientHandler) {
        try {
            clientHandler.getController().rollTheDices();
        } catch (Exception e) {
            ;
        }
    }
}
