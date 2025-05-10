package it.polimi.ingsw.galaxytruckerproject.network.Socket.ClientMessage;

import it.polimi.ingsw.galaxytruckerproject.model.tiles.Coordinates;
import it.polimi.ingsw.galaxytruckerproject.network.Socket.ClientHandler;

import java.util.ArrayList;

public class RemoveCrewMessage extends ClientMessage {
    private ArrayList<Coordinates> toRemoveFrom;
    public RemoveCrewMessage(ArrayList<Coordinates> toRemoveFrom) {
        this.toRemoveFrom = toRemoveFrom;
    }

    @Override
    public void processMessage(ClientHandler clientHandler) {
        try {
            clientHandler.getController().removeCrew(toRemoveFrom);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
