package it.polimi.ingsw.galaxytruckerproject.network.Socket.ServerMessage;

import it.polimi.ingsw.galaxytruckerproject.model.GameMode;
import it.polimi.ingsw.galaxytruckerproject.network.Socket.ServerHandler;

/**
 * Represents a message sent by the server to set the game mode on the client.
 * This message includes the game mode that should be applied to the client.
 */
public class SetGameModeMessage extends ServerMessage {

    /**
     * The game mode to be set on the client.
     */
    private GameMode gameMode;

    /**
     * Constructs a new SetGameModeMessage with the specified game mode.
     *
     * @param gameMode The game mode to be set on the client.
     */
    public SetGameModeMessage(GameMode gameMode) {
        this.gameMode = gameMode;
    }

    /**
     * Processes the set game mode message by invoking the appropriate method on the client controller
     * to update the client's game mode.
     *
     * @param serverHandler The server handler that processes the message.
     */
    @Override
    public void processMessage(ServerHandler serverHandler) {
        serverHandler.getClientController().setGameMode(this.gameMode);
    }
}
