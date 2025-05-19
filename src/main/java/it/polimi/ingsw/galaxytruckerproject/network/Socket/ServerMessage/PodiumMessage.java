package it.polimi.ingsw.galaxytruckerproject.network.Socket.ServerMessage;

import it.polimi.ingsw.galaxytruckerproject.model.player.Player;
import it.polimi.ingsw.galaxytruckerproject.network.Socket.ServerHandler;

import java.rmi.RemoteException;
import java.util.ArrayList;

public class PodiumMessage extends ServerMessage {
    private  ArrayList<Player> players;
    public PodiumMessage(ArrayList<Player> players) {
        this.players = players;
    }

    @Override
    public void processMessage(ServerHandler serverHandler) {
        try {
            serverHandler.getView().notifyPodium(players);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }
}
