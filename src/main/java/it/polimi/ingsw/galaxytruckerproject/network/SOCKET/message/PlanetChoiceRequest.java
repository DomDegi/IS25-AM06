package it.polimi.ingsw.galaxytruckerproject.network.SOCKET.message;

public class PlanetChoiceRequest extends Message{

    public PlanetChoiceRequest() {
        super(Server.SERVER_NAME, MessageType.PLANET_CHOICE_REQUEST);
    }
}
