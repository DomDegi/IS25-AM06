package it.polimi.ingsw.galaxytruckerproject.network.Socket.ClientMessage;

import it.polimi.ingsw.galaxytruckerproject.network.Socket.ClientHandler;
import it.polimi.ingsw.galaxytruckerproject.network.Socket.Message;

public class JoinGameMessage extends ClientMessage {

    public String gameName;

    public JoinGameMessage(String gameName) {
        this.gameName = gameName;
    }

    @Override
    public void processMessage(ClientHandler clientHandler) {
        try {
            clientHandler.getController().joinGame(gameName);
        } catch (Exception e) {
            ;
        }
    }

}
