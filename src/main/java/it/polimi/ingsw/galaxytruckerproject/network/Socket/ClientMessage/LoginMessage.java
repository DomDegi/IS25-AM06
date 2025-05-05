package it.polimi.ingsw.galaxytruckerproject.network.Socket.ClientMessage;

public class LoginMessage extends ClientMessage {

    private String nickname;

    public LoginMessage(String nickname) {
        this.nickname = nickname;
    }

    @Override
    public void processMessage(ClientMessage clientHandler) {
        clientHandler.getController().login(nickname);
    }
}
