package it.polimi.ingsw.galaxytruckerproject.network.Socket.ServerMessage;

import it.polimi.ingsw.galaxytruckerproject.model.tiles.Tile;
import it.polimi.ingsw.galaxytruckerproject.network.Socket.ServerHandler;

public class PositionedTileMessage extends ServerMessage{

    private String playerName;

    private Tile tile;

    public PositionedTileMessage(String playerName, Tile tile) {
        this.playerName = playerName;
        this.tile = tile.send();
    }

    @Override
    public void processMessage(ServerHandler serverHandler) {
        serverHandler.getClientController().setTile(playerName, tile);
    }
}
