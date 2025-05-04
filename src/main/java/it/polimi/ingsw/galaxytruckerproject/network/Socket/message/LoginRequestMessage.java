package it.polimi.ingsw.galaxytruckerproject.network.Socket.message;

public class LoginRequestMessage extends Message {

    public LoginRequestMessage (String nickname, MessageType messageType) {
        super(nickname, MessageType.LOGIN_REQUEST);
    }

}
