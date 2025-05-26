package it.polimi.ingsw.galaxytruckerproject.network.Socket.ServerMessage;

import it.polimi.ingsw.galaxytruckerproject.network.Socket.ServerHandler;

/**
 * Represents a message sent by the server to notify the client when a player joins the game.
 * This message includes information about the expected number of players, the current number of players,
 * and whether the player has reconnected.
 */
public class PlayerJoinedMessage extends ServerMessage {

    /**
     * The expected number of players in the game.
     */
    private final int expectedPlayers;

    /**
     * The current number of players in the game.
     */
    private final int currentPlayers;

    /**
     * A flag indicating whether the player has reconnected to the game.
     */
    private final boolean reconnected;

    /**
     * Constructs a new PlayerJoinedMessage with the specified number of expected players, current players, and reconnection status.
     *
     * @param expectedPlayers The expected number of players in the game.
     * @param currentPlayers The current number of players in the game.
     * @param reconnected A flag indicating whether the player has reconnected.
     */
    public PlayerJoinedMessage(int expectedPlayers, int currentPlayers, boolean reconnected) {
        this.expectedPlayers = expectedPlayers;
        this.currentPlayers = currentPlayers;
        this.reconnected = reconnected;
    }

    /**
     * Processes the player joined message by invoking the appropriate method on the server's view
     * to notify the client about the new player joining the game.
     *
     * @param serverHandler The server handler that processes the message.
     */
    @Override
    public void processMessage(ServerHandler serverHandler) {
        serverHandler.getView().notifyPlayerJoined(expectedPlayers, currentPlayers, reconnected);
    }
}
