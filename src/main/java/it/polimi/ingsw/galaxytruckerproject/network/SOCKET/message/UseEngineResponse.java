package it.polimi.ingsw.galaxytruckerproject.network.SOCKET.message;
import it.polimi.ingsw.galaxytruckerproject.model.tiles.Coordinates;

import java.util.ArrayList;
public class UseEngineResponse extends Message{

    private final int numEngine;
    private final ArrayList<Coordinates> coordinates;
    public UseEngineResponse (String nickname, int numEngine, ArrayList<Coordinates> coordinates) {
        super(nickname, MessageType.USE_ENGINE_RESPONSE);
        this. numEngine= numEngine;
        this.coordinates = coordinates;
        this.messageType= MessageType.USE_ENGINE_RESPONSE;
    }


    public int getNumEngine() {
        return numEngine;
    }

    public ArrayList<Coordinates> getCoordinates() {
        return coordinates;
        }

}
