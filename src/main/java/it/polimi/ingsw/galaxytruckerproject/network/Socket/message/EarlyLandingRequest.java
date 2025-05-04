package it.polimi.ingsw.galaxytruckerproject.network.Socket.message;

public class EarlyLandingRequest extends Message {
    public EarlyLandingRequest(String nickname) {
        super(nickname, MessageType.EARLY_LANDING_REQUEST);

    }
}
