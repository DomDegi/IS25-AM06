package it.polimi.ingsw.galaxytruckerproject.network.Socket.ClientMessage;
import it.polimi.ingsw.galaxytruckerproject.model.tiles.Coordinates;
import it.polimi.ingsw.galaxytruckerproject.network.Socket.ClientHandler;

import java.util.ArrayList;

public class CoordinatesMessage extends ClientMessage {

    private ArrayList<Coordinates> coordinates;

    public CoordinatesMessage(ArrayList<Coordinates> coordinates) {
        this.coordinates = coordinates;
    }

    @Override
    public void processMessage(ClientHandler clientHandler) {
        //??
    }
}
