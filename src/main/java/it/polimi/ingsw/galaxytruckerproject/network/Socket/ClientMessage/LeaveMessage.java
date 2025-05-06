package it.polimi.ingsw.galaxytruckerproject.network.Socket.ClientMessage;

import it.polimi.ingsw.galaxytruckerproject.network.Socket.ClientHandler;

public class LeaveMessage extends ClientMessage {

    public LeaveMessage() {
        ;
    }

    public void processMessage(ClientHandler clientHandler) {
        try {
            clientHandler.getController().leave();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
