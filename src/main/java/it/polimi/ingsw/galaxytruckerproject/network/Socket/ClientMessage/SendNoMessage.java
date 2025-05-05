package it.polimi.ingsw.galaxytruckerproject.network.Socket.ClientMessage;

import it.polimi.ingsw.galaxytruckerproject.network.Socket.ClientHandler;

public class SendNoMessage extends ClientMessage {

    public SendNoMessage() {
        ;
    }

    @Override
    public void processMessage(ClientHandler clientHandler) {
        try {
            clientHandler.getController().makeAChoice(false);
        } catch (Exception e) {
            ;
        }
    }
}
