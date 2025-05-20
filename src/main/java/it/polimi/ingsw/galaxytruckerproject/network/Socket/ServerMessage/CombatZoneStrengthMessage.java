package it.polimi.ingsw.galaxytruckerproject.network.Socket.ServerMessage;

import it.polimi.ingsw.galaxytruckerproject.network.Socket.ServerHandler;

import java.rmi.RemoteException;
import java.security.Signature;

public class CombatZoneStrengthMessage extends ServerMessage {

    private String playerName;
    private float strength;
    public CombatZoneStrengthMessage(String playerName, float strength) {
        this.playerName = playerName;
        this.strength = strength;
    }
    @Override
    public void processMessage(ServerHandler serverHandler) {
        try {
            serverHandler.getView().notifyCombatZoneStrength(playerName,strength);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }
}