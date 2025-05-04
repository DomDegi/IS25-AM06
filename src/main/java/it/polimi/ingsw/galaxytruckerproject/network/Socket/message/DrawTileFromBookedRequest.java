package it.polimi.ingsw.galaxytruckerproject.network.Socket.message;

public class DrawTileFromBookedRequest extends DrawTileRequest {

    private final int index;

    public DrawTileFromBookedRequest(String nickname, int index) {
        super(nickname);
        this.index = index;
    }

    public int getTileIndex() {
        return index;
    }
}
