package it.polimi.ingsw.galaxytruckerproject.network.Socket.ServerMessage;

import it.polimi.ingsw.galaxytruckerproject.network.Socket.ServerHandler;

import java.rmi.RemoteException;

public class NotifyCombatZoneCrewMessage extends ServerMessage {
    int crew;
    String playerName;
    public NotifyCombatZoneCrewMessage( String playerName , int crew) {
        this.crew = crew;
        this.playerName = playerName;
    }

    @Override
    public void processMessage(ServerHandler serverHandler) {
        try {
            serverHandler.getView().notifyCombatZoneCrew(playerName,crew);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }
}
