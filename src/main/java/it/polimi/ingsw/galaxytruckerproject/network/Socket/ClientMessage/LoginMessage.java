package it.polimi.ingsw.galaxytruckerproject.network.Socket.ClientMessage;

import it.polimi.ingsw.galaxytruckerproject.network.Socket.ClientHandler;

public class LoginMessage extends ClientMessage {

    private String nickname;

    public LoginMessage(String nickname) {
        this.nickname = nickname;
    }

    @Override
    public void processMessage(ClientHandler clientHandler) {
        try {
            clientHandler.getController().login(nickname);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
