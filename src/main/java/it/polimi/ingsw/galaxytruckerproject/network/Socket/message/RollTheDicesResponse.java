package it.polimi.ingsw.galaxytruckerproject.network.Socket.message;

import it.polimi.ingsw.galaxytruckerproject.network.Server;

public class RollTheDicesResponse extends Message{

    private final int roll;

    public RollTheDicesResponse(int roll) {
        super(Server.SERVER_NAME, MessageType.ROLL_THE_DICES_RESPONSE);
        this.roll = roll;
    }

    public int getRoll() {
        return roll;
    }
}
