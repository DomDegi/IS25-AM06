package it.polimi.ingsw.galaxytruckerproject.network.Socket.message;

public class DrawCardResponse extends Message{
    public DrawCardResponse(String nickname) {
        super(nickname, MessageType.DRAW_CARD_RESPONSE);
    }
}
