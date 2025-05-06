package it.polimi.ingsw.galaxytruckerproject.network.Socket.ServerMessage;

import it.polimi.ingsw.galaxytruckerproject.model.tiles.Tile;
import it.polimi.ingsw.galaxytruckerproject.network.Socket.ServerHandler;

public class BookedTileMessage extends  ServerMessage {

    private final String playerName;
    private final Tile bookedTile;

    public BookedTileMessage(String playerName, Tile bookedTile) {
        this.playerName = playerName;
        this.bookedTile = bookedTile;
    }

    @Override
    public void processMessage(ServerHandler serverHandler) {
        serverHandler.getClientController().addBookedTile(playerName, bookedTile);
    }
}
