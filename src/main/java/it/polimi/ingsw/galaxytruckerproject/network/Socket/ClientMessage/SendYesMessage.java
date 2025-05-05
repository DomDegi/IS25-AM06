package it.polimi.ingsw.galaxytruckerproject.network.Socket.ClientMessage;

import it.polimi.ingsw.galaxytruckerproject.network.Socket.ClientHandler;

import java.io.Serializable;

public class SendYesMessage extends ClientMessage {

    public SendYesMessage() {
        ;
    }

    @Override
    public void processMessage(ClientHandler clientHandler) {
        try {
            clientHandler.getController().makeAChoice(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
