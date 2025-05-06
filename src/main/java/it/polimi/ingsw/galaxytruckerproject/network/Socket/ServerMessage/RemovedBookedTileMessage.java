package it.polimi.ingsw.galaxytruckerproject.network.Socket.ServerMessage;

import it.polimi.ingsw.galaxytruckerproject.model.tiles.Tile;
import it.polimi.ingsw.galaxytruckerproject.network.Socket.ServerHandler;

public class RemovedBookedTileMessage extends ServerMessage {

    private String playerName;
    private Tile tile;

    public RemovedBookedTileMessage(String playerName, Tile tile) {
        this.playerName = playerName;
        this.tile = tile;
    }

    @Override
    public void processMessage(ServerHandler serverHandler) {
        serverHandler.getClientController().removeBookedTile(playerName, tile);
    }
}
