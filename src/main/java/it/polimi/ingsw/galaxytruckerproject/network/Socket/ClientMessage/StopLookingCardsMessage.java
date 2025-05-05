package it.polimi.ingsw.galaxytruckerproject.network.Socket.ClientMessage;

import it.polimi.ingsw.galaxytruckerproject.network.Socket.ClientHandler;

public class StopLookingCardsMessage extends ClientMessage {

    public StopLookingCardsMessage() {
        ;
    }

    public void processMessage(ClientHandler clientHandler) {
        try {
            clientHandler.getController().stopLookingAtCards();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
