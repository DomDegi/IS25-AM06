package it.polimi.ingsw.galaxytruckerproject.network.Socket.ServerMessage;

import it.polimi.ingsw.galaxytruckerproject.network.Socket.ServerHandler;

import java.rmi.RemoteException;

/**
 * Represents a message sent by the server to notify the client about the number of crew members
 * in the player's combat zone.
 * This message includes the player's name and the number of crew members in the combat zone.
 */
public class NotifyCombatZoneCrewMessage extends ServerMessage {

    /**
     * The number of crew members in the player's combat zone.
     */
    int crew;

    /**
     * The name of the player whose combat zone crew information is being notified.
     */
    String playerName;

    /**
     * Constructs a new NotifyCombatZoneCrewMessage with the specified player name and crew count.
     *
     * @param playerName The name of the player whose combat zone crew is being notified.
     * @param crew The number of crew members in the player's combat zone.
     */
    public NotifyCombatZoneCrewMessage(String playerName, int crew) {
        this.crew = crew;
        this.playerName = playerName;
    }

    /**
     * Processes the combat zone crew notification message by invoking the appropriate method on the server's view
     * to notify the client about the player's combat zone crew.
     *
     * @param serverHandler The server handler that processes the message.
     */
    @Override
    public void processMessage(ServerHandler serverHandler) {
        try {
            serverHandler.getView().notifyCombatZoneCrew(playerName, crew);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }
}
