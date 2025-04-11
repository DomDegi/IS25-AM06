package it.polimi.ingsw.galaxytruckerproject.network.SOCKET.message;
import it.polimi.ingsw.galaxytruckerproject.model.tiles.Coordinates;

import java.util.ArrayList;
public class UseEngineResponse extends Message{

    private String playerName;
    private int numEngine;
    private ArrayList<Coordinates> coordinates;
    public UseEngineResponse (String nickname, int numEngine, ArrayList<Coordinates> coordinates) {
        super(nickname, MessageType.USE_ENGINE_RESPONSE);
        this.playerName = nickname;
        this. numEngine= numEngine;
        this.coordinates = coordinates;
        this.messageType= MessageType.USE_ENGINE_RESPONSE;
    }

    public String getPlayerName() {
        return playerName;
    }

    public float getNumEngine() {
        return numEngine;
    }

    public ArrayList<Coordinates> getCoordinates() {
        return coordinates;
        }

}
