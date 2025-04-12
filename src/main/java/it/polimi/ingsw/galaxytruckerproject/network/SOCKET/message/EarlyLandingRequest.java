package it.polimi.ingsw.galaxytruckerproject.network.SOCKET.message;

public class EarlyLandingRequest extends Message {
    public EarlyLandingRequest(String nickname) {
        super(nickname, MessageType.EARLY_LANDING_REQUEST);

    }
}
