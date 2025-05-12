package it.polimi.ingsw.galaxytruckerproject.network.Socket.ClientMessage;

import it.polimi.ingsw.galaxytruckerproject.model.tiles.Coordinates;
import it.polimi.ingsw.galaxytruckerproject.network.Socket.ClientHandler;

import java.util.ArrayList;

public class ShipErrorManagementMessage extends ClientMessage {
    private ArrayList<Coordinates> toRemove;
    public ShipErrorManagementMessage(ArrayList<Coordinates> toRemove) {
        this.toRemove = toRemove;
    }
    @Override
    public void processMessage(ClientHandler clientHandler) {
        try {
            clientHandler.getController().shipErrorManagement(toRemove);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

