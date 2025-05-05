package it.polimi.ingsw.galaxytruckerproject.network.Socket.ClientMessage;

import it.polimi.ingsw.galaxytruckerproject.network.Socket.ClientHandler;

public class LeaveGameMessage extends ClientMessage {

    public LeaveGameMessage() {
        ;
    }

    @Override
    public void processMessage(ClientHandler clientHandler) {
        try {
            clientHandler.getController().leaveGame();
        } catch (Exception e) {
            ;
        }
    }
}
