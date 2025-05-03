package it.polimi.ingsw.galaxytruckerproject.network.SOCKET.message;

public class SetPositionRequest extends Message{

    public SetPositionRequest() {
        super(Server.SERVER_NAME, MessageType.SET_POSITION_REQUEST);
    }
}
