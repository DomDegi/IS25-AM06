package it.polimi.ingsw.galaxytruckerproject.network.Socket.message;

import it.polimi.ingsw.galaxytruckerproject.network.Server;

public class DrawCardRequest extends Message{
    public DrawCardRequest() {
        super(Server.SERVER_NAME, MessageType.DRAW_CARD_REQUEST);
    }
}
