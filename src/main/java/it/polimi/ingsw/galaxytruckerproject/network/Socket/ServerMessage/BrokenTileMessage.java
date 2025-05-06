package it.polimi.ingsw.galaxytruckerproject.network.Socket.ServerMessage;

import it.polimi.ingsw.galaxytruckerproject.client.ClientController;
import it.polimi.ingsw.galaxytruckerproject.model.tiles.Coordinates;
import it.polimi.ingsw.galaxytruckerproject.network.Socket.ServerHandler;

import java.rmi.RemoteException;
import java.util.ArrayList;

public class BrokenTileMessage extends  ServerMessage {

    private String playerName;
    private ArrayList<Coordinates> brokenTiles;

    public BrokenTileMessage(String playerName, ArrayList<Coordinates> brokenTiles) {
        this.playerName = playerName;
        this.brokenTiles = brokenTiles;
    }

    @Override
    public void processMessage(ServerHandler serverHandler) {
        ClientController clientController = serverHandler.getClientController();
        clientController.brokenTiles(playerName, brokenTiles);
        if (playerName.equals(clientController.getName())) {
            clientController.getView().printShipboard(clientController.getLightShipBoard());
        }
    }
}
