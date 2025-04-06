package it.polimi.ingsw.galaxytruckerproject.network.SOCKET.message;

public class AcceptMessage extends Message{

    public AcceptMessage(String nickname) {
        super(nickname, MessageType.ACCEPT_MESSAGE);
    }
}
