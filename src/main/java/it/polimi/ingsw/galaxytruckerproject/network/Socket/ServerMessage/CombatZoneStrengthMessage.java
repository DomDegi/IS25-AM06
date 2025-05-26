package it.polimi.ingsw.galaxytruckerproject.network.Socket.ServerMessage;

import it.polimi.ingsw.galaxytruckerproject.network.Socket.ServerHandler;

import java.rmi.RemoteException;

/**
 * Represents a message sent by the server to notify the client about the strength of a player's combat zone.
 * This message includes the player's name and their combat zone strength value.
 */
public class CombatZoneStrengthMessage extends ServerMessage {

    /**
     * The name of the player whose combat zone strength is being notified.
     */
    private String playerName;

    /**
     * The strength of the player's combat zone.
     */
    private float strength;

    /**
     * Constructs a new CombatZoneStrengthMessage with the specified player name and combat zone strength.
     *
     * @param playerName The name of the player whose combat zone strength is being notified.
     * @param strength The strength of the player's combat zone.
     */
    public CombatZoneStrengthMessage(String playerName, float strength) {
        this.playerName = playerName;
        this.strength = strength;
    }

    /**
     * Processes the combat zone strength message by invoking the appropriate method on the server's view
     * to notify the client about the player's combat zone strength.
     *
     * @param serverHandler The server handler that processes the message.
     * @throws RuntimeException if an error occurs during message processing.
     */
    @Override
    public void processMessage(ServerHandler serverHandler) {
        try {
            serverHandler.getView().notifyCombatZoneStrength(playerName, strength);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }
}
