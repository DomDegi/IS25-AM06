package it.polimi.ingsw.galaxytruckerproject.network.Socket.message;

import it.polimi.ingsw.galaxytruckerproject.network.Server;

public class PlanetChoiceRequest extends Message{

    public PlanetChoiceRequest() {
        super(Server.SERVER_NAME, MessageType.PLANET_CHOICE_REQUEST);
    }
}
