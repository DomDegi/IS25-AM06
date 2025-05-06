package it.polimi.ingsw.galaxytruckerproject.network.Socket.ServerMessage;

import it.polimi.ingsw.galaxytruckerproject.model.tiles.Tile;
import it.polimi.ingsw.galaxytruckerproject.network.Socket.ServerHandler;

public class ShowDrawnTileMessage extends  ServerMessage{

    private Tile drawnTile;

    public ShowDrawnTileMessage(Tile drawnTile) {
        this.drawnTile = drawnTile;
    }

    @Override
    public void processMessage(ServerHandler serverHandler) {
        try {
            serverHandler.getView().showDrawnTile(drawnTile);
            serverHandler.getClientController().setTileInHand(drawnTile);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
