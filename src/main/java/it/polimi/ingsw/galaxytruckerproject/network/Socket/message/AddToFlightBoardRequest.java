package it.polimi.ingsw.galaxytruckerproject.network.Socket.message;

public class AddToFlightBoardRequest extends Message {
    public final int position;

    public AddToFlightBoardRequest(String playerName, int position) {
        super(playerName,MessageType.ADD_TO_FLIGHTBOARD_REQUEST);
        this.position = position;
    }

    public int getPosition() {
        return position;
    }
}
