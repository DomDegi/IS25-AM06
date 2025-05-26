package it.polimi.ingsw.galaxytruckerproject.network.Socket.ServerMessage;

import it.polimi.ingsw.galaxytruckerproject.network.Socket.ServerHandler;

import java.rmi.RemoteException;

/**
 * Represents a message sent by the server to notify the client about the strength of a player's combat zone engine.
 * This message includes the player's name and the strength of their combat zone engine.
 */
public class NotifyCombatZoneEngineMessage extends ServerMessage {

    /**
     * The name of the player whose combat zone engine strength is being notified.
     */
    String playerName;

    /**
     * The strength of the player's combat zone engine.
     */
    float strength;

    /**
     * Constructs a new NotifyCombatZoneEngineMessage with the specified player name and engine strength.
     *
     * @param playerName The name of the player whose combat zone engine strength is being notified.
     * @param strength The strength of the player's combat zone engine.
     */
    public NotifyCombatZoneEngineMessage(String playerName, float strength) {
        this.playerName = playerName;
        this.strength = strength;
    }

    /**
     * Processes the combat zone engine notification message by invoking the appropriate method on the server's view
     * to notify the client about the player's combat zone engine strength.
     *
     * @param serverHandler The server handler that processes the message.
     */
    @Override
    public void processMessage(ServerHandler serverHandler) {
        try {
            serverHandler.getView().notifyCombatZoneEngine(playerName, strength);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }
}
