package it.polimi.ingsw.galaxytruckerproject.network.Socket.ClientMessage;

import it.polimi.ingsw.galaxytruckerproject.network.Socket.ClientHandler;

public class LookGameCardMessage extends ClientMessage {

    private int index;

    public LookGameCardMessage(int index) {
        this.index = index;
    }

    public void processMessage(ClientHandler clientHandler) {
        try {
            clientHandler.getController().lookGameCards(index);
        } catch (Exception e) {
            ;
        }
    }
}
