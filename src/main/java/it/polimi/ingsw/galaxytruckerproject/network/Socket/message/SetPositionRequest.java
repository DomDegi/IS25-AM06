package it.polimi.ingsw.galaxytruckerproject.network.Socket.message;

import it.polimi.ingsw.galaxytruckerproject.network.Server;

public class SetPositionRequest extends Message{

    public SetPositionRequest() {
        super(Server.SERVER_NAME, MessageType.SET_POSITION_REQUEST);
    }
}
