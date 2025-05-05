package it.polimi.ingsw.galaxytruckerproject.network.Socket.message;

public class SetPositionResponse extends Message {

    private final int position;

    public SetPositionResponse(String playerName, int position) {
        super(playerName, MessageType.DRAWN_TILE_RESPONSE);
        this.position = position;
    }

    public int getPosition() {
        return position;
    }
}
