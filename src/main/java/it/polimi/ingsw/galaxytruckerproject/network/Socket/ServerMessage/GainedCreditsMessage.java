package it.polimi.ingsw.galaxytruckerproject.network.Socket.ServerMessage;

import it.polimi.ingsw.galaxytruckerproject.network.Socket.ServerHandler;

/**
 * Represents a message sent by the server to notify the client that a player has gained credits.
 * This message includes the player's name and the number of credits gained.
 */
public class GainedCreditsMessage extends ServerMessage {

    /**
     * The name of the player who has gained credits.
     */
    private String playerName;

    /**
     * The number of credits gained by the player.
     */
    private int credits;

    /**
     * Constructs a new GainedCreditsMessage with the specified player name and number of credits.
     *
     * @param playerName The name of the player who gained the credits.
     * @param credits The number of credits gained by the player.
     */
    public GainedCreditsMessage(String playerName, int credits) {
        this.playerName = playerName;
        this.credits = credits;
    }

    /**
     * Processes the gained credits message by invoking the appropriate method on the client controller
     * to update the player's credits.
     *
     * @param serverHandler The server handler that processes the message.
     */
    @Override
    public void processMessage(ServerHandler serverHandler) {
        serverHandler.getClientController().gainCredit(playerName, credits);
    }
}
