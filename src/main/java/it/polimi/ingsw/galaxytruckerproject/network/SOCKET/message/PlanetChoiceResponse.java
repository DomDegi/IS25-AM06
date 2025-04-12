package it.polimi.ingsw.galaxytruckerproject.network.SOCKET.message;

public class PlanetChoiceResponse extends Message {

    private int choosenPlanet;

    public PlanetChoiceResponse(String nickname, int choosenPlanet) {
        super(nickname, MessageType.PLANET_CHOICE_RESPONSE);
        this.choosenPlanet = choosenPlanet;
    }

    public int getChoosenPlanet() {
        return choosenPlanet;
    }
}
