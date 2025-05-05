package it.polimi.ingsw.galaxytruckerproject.network.Socket.ClientMessage;

import it.polimi.ingsw.galaxytruckerproject.network.Socket.ClientHandler;

public class DrawCardMessage extends ClientMessage{

    public DrawCardMessage() {}

    @Override
    public void processMessage(ClientHandler clientHandler) {
        try {
            clientHandler.getController().drawCard();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
