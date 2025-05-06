package it.polimi.ingsw.galaxytruckerproject.network.Socket.ClientMessage;

import it.polimi.ingsw.galaxytruckerproject.network.Socket.ClientHandler;

public class CompletedShipMessage extends ClientMessage {

    public CompletedShipMessage() {}

    @Override
    public void processMessage(ClientHandler clientHandler) {
        try {
            clientHandler.getController().completedShip();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
