package it.polimi.ingsw.galaxytruckerproject.network.SOCKET.message;

public class LoginRequestMessage extends Message {

    public LoginRequestMessage (String nickname, MessageType messageType) {
        super(nickname, MessageType.LOGIN_REQUEST);
    }

}
