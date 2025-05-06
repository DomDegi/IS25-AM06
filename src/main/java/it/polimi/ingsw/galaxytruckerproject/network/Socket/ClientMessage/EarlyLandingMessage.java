package it.polimi.ingsw.galaxytruckerproject.network.Socket.ClientMessage;

import it.polimi.ingsw.galaxytruckerproject.network.Socket.ClientHandler;

public class EarlyLandingMessage extends ClientMessage {
    public EarlyLandingMessage() {}

    @Override
    public void processMessage(ClientHandler clientHandler) {
        try {
            clientHandler.getController().earlyLanding();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
