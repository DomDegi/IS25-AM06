package it.polimi.ingsw.galaxytruckerproject.network.Socket.ClientMessage;

import it.polimi.ingsw.galaxytruckerproject.model.tiles.Coordinates;
import it.polimi.ingsw.galaxytruckerproject.network.Socket.ClientHandler;

import java.util.ArrayList;

public class EngineMessage extends ClientMessage{

    private int numDoubleEngine;
    private ArrayList<Coordinates> coordinates;

    public EngineMessage(int numDoubleEngine, ArrayList<Coordinates> coordinates) {
        this.numDoubleEngine = numDoubleEngine;
        this.coordinates = coordinates;
    }

    @Override
    public void processMessage(ClientHandler clientHandler) {
        try {
            clientHandler.getController().useEngines(numDoubleEngine, coordinates);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
