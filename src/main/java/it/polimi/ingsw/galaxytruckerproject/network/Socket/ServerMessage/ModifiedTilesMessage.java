package it.polimi.ingsw.galaxytruckerproject.network.Socket.ServerMessage;

import it.polimi.ingsw.galaxytruckerproject.model.tiles.Tile;
import it.polimi.ingsw.galaxytruckerproject.network.Socket.ServerHandler;

import java.rmi.RemoteException;
import java.util.ArrayList;

public class ModifiedTilesMessage extends ServerMessage {

    private String playerName;
    private ArrayList<Tile> tiles;

    public ModifiedTilesMessage(String playerName, ArrayList<Tile> tiles) {
        this.playerName = playerName;
        this.tiles = tiles;
    }

    @Override
    public void processMessage(ServerHandler serverHandler) {
        serverHandler.getClientController().modifyTiles(playerName, tiles);
        if (playerName.equals(serverHandler.getClientController().getName())) {

            serverHandler.getView().printShipboard(serverHandler.getClientController().getLightShipBoard());

        }
    }
}
