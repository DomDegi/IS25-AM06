package it.polimi.ingsw.galaxytruckerproject.network.SOCKET.message;

public class DrawCardResponse extends Message{
    public DrawCardResponse(String nickname) {
        super(nickname, MessageType.DRAW_CARD_RESPONSE);
    }
}
