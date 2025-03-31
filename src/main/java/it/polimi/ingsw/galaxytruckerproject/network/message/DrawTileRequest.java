package it.polimi.ingsw.galaxytruckerproject.network.message;

public abstract class DrawTileRequest extends Message{

    protected MessageType drawFrom;

    protected int index;

    public DrawTileRequest(String nickname) {
        super(nickname, MessageType.DRAW_TILE_REQUEST);
    }

    public MessageType DrawFromWhere() {
        return drawFrom;
    }

    public int getTileIndex() {
        return index;
    }
}
