package it.polimi.ingsw.galaxytruckerproject.network.Socket.message;

public class RollTheDicesRequest extends Message{

    public RollTheDicesRequest(String nickname) {
        super(nickname,  MessageType.ROLL_THE_DICES_REQUEST);
    }
}
