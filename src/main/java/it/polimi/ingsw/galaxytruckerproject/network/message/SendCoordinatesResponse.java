package it.polimi.ingsw.galaxytruckerproject.network.message;

import it.polimi.ingsw.galaxytruckerproject.model.tiles.Coordinates;

import java.util.ArrayList;

public class SendCoordinatesResponse extends Message{

    private ArrayList<Coordinates>  coordinates = new ArrayList<>();

    public SendCoordinatesResponse(String  playerName, ArrayList<Coordinates> coordinates){
        super(playerName, MessageType.SEND_COORDINATES_RESPONSE);
        this.coordinates = coordinates;
    }

    public Coordinates getFirst () {
        return coordinates.getFirst();
    }

    public ArrayList<Coordinates> getCoordinates() {
        return coordinates;
    }

    public Coordinates removeFirst() {
        return coordinates.removeFirst();
    }

    public ArrayList<Coordinates> getTheseMany (int number) {
        ArrayList<Coordinates> toSend = new ArrayList<>();
        for (int i = 0; i < number; i++) {
            toSend.add(coordinates.get(i));
        }
        return toSend;
    }

    public boolean isEmpty () {
        return coordinates.isEmpty();
    }
}
