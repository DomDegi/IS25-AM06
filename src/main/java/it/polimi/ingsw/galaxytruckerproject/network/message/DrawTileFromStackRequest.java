package it.polimi.ingsw.galaxytruckerproject.network.message;

public class DrawTileFromStackRequest extends DrawTileRequest{

    public DrawTileFromStackRequest(String nickname){
        super(nickname);
        this.drawFrom = MessageType.DRAW_TILE_FROM_STACK_REQUEST;
        //this should never be asked
        this.index = -1;
    }


}
