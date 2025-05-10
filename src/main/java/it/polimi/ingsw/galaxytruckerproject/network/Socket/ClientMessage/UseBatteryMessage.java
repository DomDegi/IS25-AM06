package it.polimi.ingsw.galaxytruckerproject.network.Socket.ClientMessage;

import it.polimi.ingsw.galaxytruckerproject.model.tiles.Coordinates;
import it.polimi.ingsw.galaxytruckerproject.network.Socket.ClientHandler;

import java.util.ArrayList;

public class UseBatteryMessage extends ClientMessage {
    private ArrayList<Coordinates> coord;
    public UseBatteryMessage(ArrayList<Coordinates> coord) {
        this.coord = coord;
    }

    @Override
    public void processMessage(ClientHandler clientHandler) {
        try {
            clientHandler.getController().useBatteries(coord);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
