package it.polimi.ingsw.galaxytruckerproject.network.SOCKET.message;

import it.polimi.ingsw.galaxytruckerproject.model.tiles.Coordinates;

import java.util.ArrayList;

import static it.polimi.ingsw.galaxytruckerproject.network.SOCKET.message.MessageType.USE_CANNON_RESPONSE;

public class UseCannonResponse extends Message{
    private String playerName;
    private float Strength;
    private ArrayList<Coordinates> coordinates;
    public UseCannonResponse(String nickname,float Strength, ArrayList<Coordinates> coordinates) {
        super(nickname,USE_CANNON_RESPONSE);
        this.playerName = nickname;
        this.Strength = Strength;
        this.coordinates = coordinates;
        this.messageType= USE_CANNON_RESPONSE;
    }

    public String getPlayerName() {
        return playerName;
    }

    public float getStrength() {
        return Strength;
    }

    public ArrayList<Coordinates> getCoordinates() {
        return coordinates;
    }
}
