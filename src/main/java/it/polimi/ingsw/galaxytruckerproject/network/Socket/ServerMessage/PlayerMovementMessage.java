package it.polimi.ingsw.galaxytruckerproject.network.Socket.ServerMessage;

import it.polimi.ingsw.galaxytruckerproject.model.player.PlayersColor;
import it.polimi.ingsw.galaxytruckerproject.network.Socket.ServerHandler;

/**
 * Represents a message sent by the server to notify the client about a player's movement in the game.
 * This message includes the player's name, their position, their ranking, and their associated color.
 */
public class PlayerMovementMessage extends ServerMessage {

    /**
     * The name of the player whose movement is being notified.
     */
    private String playerName;

    /**
     * The position of the player on the flight board.
     */
    private int playerPosition;

    /**
     * The ranking of the player on the flight board.
     */
    private int playerRanking;

    /**
     * The color associated with the player.
     */
    private PlayersColor color;

    /**
     * Constructs a new PlayerMovementMessage with the specified player details.
     *
     * @param playerName The name of the player whose movement is being notified.
     * @param color The color associated with the player.
     * @param playerPosition The position of the player on the flight board.
     * @param playerRanking The ranking of the player on the flight board.
     */
    public PlayerMovementMessage(String playerName, PlayersColor color, int playerPosition, int playerRanking) {
        this.playerName = playerName;
        this.playerPosition = playerPosition;
        this.playerRanking = playerRanking;
        this.color = color;
    }

    /**
     * Processes the player movement message by updating the client's flight board with the player's new position
     * and ranking, as well as their associated color.
     *
     * @param serverHandler The server handler that processes the message.
     */
    @Override
    public void processMessage(ServerHandler serverHandler) {
        serverHandler.getClientController().updateFlightboard(playerName, color, playerPosition, playerRanking);
    }
}
