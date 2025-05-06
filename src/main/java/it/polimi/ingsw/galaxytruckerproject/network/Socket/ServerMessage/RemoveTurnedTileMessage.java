package it.polimi.ingsw.galaxytruckerproject.network.Socket.ServerMessage;

import it.polimi.ingsw.galaxytruckerproject.model.tiles.Tile;
import it.polimi.ingsw.galaxytruckerproject.network.Socket.ServerHandler;

public class RemoveTurnedTileMessage extends  ServerMessage {

    private Tile turnedTile;

    public RemoveTurnedTileMessage(Tile turnedTile) {
        this.turnedTile = turnedTile;
    }

    @Override
    public void processMessage(ServerHandler serverHandler) {
        serverHandler.getClientController().removeTurnedTile(turnedTile);
    }
}
