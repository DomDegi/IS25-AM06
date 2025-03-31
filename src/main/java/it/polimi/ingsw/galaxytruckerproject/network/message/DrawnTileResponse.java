package it.polimi.ingsw.galaxytruckerproject.network.message;

import it.polimi.ingsw.galaxytruckerproject.model.tiles.Tile;
import it.polimi.ingsw.galaxytruckerproject.network.Server;

public class DrawnTileResponse extends Message {

    private final Tile drawnTile;

    public DrawnTileResponse(Tile drawnTile) {
        super(Server.SERVER_NAME, MessageType.DRAWN_TILE_RESPONSE);
        this.drawnTile = drawnTile;
    }

    public Tile getDrawnTile() {
        return drawnTile;
    }
}
