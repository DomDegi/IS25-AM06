package it.polimi.ingsw.galaxytruckerproject.network.SOCKET.message;

public class DrawTileFromTurnedRequest extends DrawTileRequest {

    private final int index;

    public DrawTileFromTurnedRequest(String nickname, int index) {
        super(nickname);
        this.index = index;
    }

    public int getTileIndex () {
        return index;
    }
}
