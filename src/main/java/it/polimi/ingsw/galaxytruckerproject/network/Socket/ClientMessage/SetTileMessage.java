package it.polimi.ingsw.galaxytruckerproject.network.Socket.ClientMessage;

import it.polimi.ingsw.galaxytruckerproject.model.tiles.Tile;
import it.polimi.ingsw.galaxytruckerproject.network.Socket.ClientHandler;

public class SetTileMessage extends ClientMessage {

    private Tile tile;

    public SetTileMessage(Tile tile) {
        this.tile = tile;
    }

    @Override
    public void processMessage(ClientHandler clientHandler) {
        try {
            clientHandler.getController().setTile(tile);
        } catch (Exception e) {
            ;
        }
    }
}
