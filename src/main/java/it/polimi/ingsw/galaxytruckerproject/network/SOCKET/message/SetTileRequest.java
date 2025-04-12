package it.polimi.ingsw.galaxytruckerproject.network.SOCKET.message;

import it.polimi.ingsw.galaxytruckerproject.model.tiles.Coordinates;
import it.polimi.ingsw.galaxytruckerproject.model.tiles.Tile;


public class SetTileRequest extends Message {

    private Coordinates coordinates;
    private Tile tile;


    public SetTileRequest(String nickname, Coordinates coordinates, Tile tile) {
        super(nickname, MessageType.SET_TILE_REQUEST);
        this.coordinates = coordinates;
        this.tile = tile;
    }

    public Coordinates getCoordinates() {
        return coordinates;
    }

    public Tile getTile() {
        return tile;
    }
}
