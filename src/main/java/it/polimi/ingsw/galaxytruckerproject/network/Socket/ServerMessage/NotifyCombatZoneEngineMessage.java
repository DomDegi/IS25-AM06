package it.polimi.ingsw.galaxytruckerproject.network.Socket.ServerMessage;

import it.polimi.ingsw.galaxytruckerproject.network.Socket.ServerHandler;

import java.rmi.RemoteException;

public class NotifyCombatZoneEngineMessage extends ServerMessage {

    String playerName;
    float strength;
    public NotifyCombatZoneEngineMessage(String playerName, float strength){
        this.playerName = playerName;
        this.strength = strength;
    }


    @Override
    public void processMessage(ServerHandler serverHandler) {
        try {
            serverHandler.getView().notifyCombatZoneEngine(playerName,strength);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }
}