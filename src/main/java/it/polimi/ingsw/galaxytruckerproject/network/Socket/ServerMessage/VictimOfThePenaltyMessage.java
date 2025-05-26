package it.polimi.ingsw.galaxytruckerproject.network.Socket.ServerMessage;

import it.polimi.ingsw.galaxytruckerproject.network.Socket.ServerHandler;

/**
 * Represents a message sent by the server to notify the client that a player has become the victim of a penalty.
 * This message includes the name of the player who is receiving the penalty.
 */
public class VictimOfThePenaltyMessage extends ServerMessage {

    /**
     * The name of the player who is the victim of the penalty.
     */
    String playerName;

    /**
     * Constructs a new VictimOfThePenaltyMessage with the specified player's name.
     *
     * @param playerName The name of the player who is the victim of the penalty.
     */
    public VictimOfThePenaltyMessage(String playerName) {
        this.playerName = playerName;
    }

    /**
     * Processes the victim of the penalty message by invoking the appropriate method on the client controller
     * to handle the penalty for the specified player.
     *
     * @param serverHandler The server handler that processes the message.
     */
    @Override
    public void processMessage(ServerHandler serverHandler) {
        serverHandler.getClientController().victimOfThePenalty(playerName);
    }
}
