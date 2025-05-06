package it.polimi.ingsw.galaxytruckerproject.network.Socket.ServerMessage;

import it.polimi.ingsw.galaxytruckerproject.client.ClientState;
import it.polimi.ingsw.galaxytruckerproject.network.Socket.ServerHandler;

import java.rmi.RemoteException;

public class PlayerLandedOnPlanetMessage extends ServerMessage {

    private String playerName;

    private int planet;

    public PlayerLandedOnPlanetMessage(String playerName, int planet) {
        this.playerName = playerName;
        this.planet = planet;
    }

    @Override
    public void processMessage(ServerHandler serverHandler) {
        if (playerName.equals(serverHandler.getClientController().getName())) {
            serverHandler.getClientController().setState(ClientState.MANAGE_GOODS);
        }
        try {
            serverHandler.getView().notifyPlayerLandedOnPlanet(playerName,planet);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }
}
