package it.polimi.ingsw.galaxytruckerproject.network.SOCKET.message;

public class DrawCardRequest extends Message{
    public DrawCardRequest() {
        super(Server.SERVER_NAME, MessageType.DRAW_CARD_REQUEST);
    }
}
