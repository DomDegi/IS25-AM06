package it.polimi.ingsw.galaxytruckerproject.network.Socket.ClientMessage;

import it.polimi.ingsw.galaxytruckerproject.model.tiles.Coordinates;
import it.polimi.ingsw.galaxytruckerproject.network.Socket.ClientHandler;

import java.rmi.RemoteException;
import java.util.ArrayList;

public class CannonMessage extends ClientMessage {

    private float firePower;

    private ArrayList<Coordinates> coordinates;

    public CannonMessage(float firePower, ArrayList<Coordinates> coordinates) {
        this.firePower = firePower;
        this.coordinates = coordinates;
    }

    @Override
    public void processMessage(ClientHandler clientHandler) {
        try {
            clientHandler.getController().useCannons(firePower, coordinates);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }
}
