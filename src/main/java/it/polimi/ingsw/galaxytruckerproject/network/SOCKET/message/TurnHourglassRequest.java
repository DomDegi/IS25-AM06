package it.polimi.ingsw.galaxytruckerproject.network.SOCKET.message;

public class TurnHourglassRequest extends Message {

    public TurnHourglassRequest(String nickname) {
        super(nickname, MessageType.TURN_HOURGLASS_REQUEST);
    }
}
