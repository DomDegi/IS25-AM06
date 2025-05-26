package it.polimi.ingsw.galaxytruckerproject.network.Socket.ServerMessage;

import it.polimi.ingsw.galaxytruckerproject.network.Socket.ServerHandler;

/**
 * Represents a message sent by the server to notify the client of the current game status.
 * This message includes the current status of the game, which may include various game-related information.
 */
public class CurrentGameStatusMessage extends ServerMessage {

    /**
     * The current status of the game.
     */
    private final String currentGameStatus;

    /**
     * Constructs a new CurrentGameStatusMessage with the specified current game status.
     *
     * @param currentStatus The current status of the game.
     */
    public CurrentGameStatusMessage(String currentStatus) {
        this.currentGameStatus = currentStatus;
    }

    /**
     * Processes the current game status message by updating the client's model with the current game status.
     * This method invokes the appropriate method on the client controller to update the game model.
     *
     * @param serverHandler The server handler that processes the message.
     */
    @Override
    public void processMessage(ServerHandler serverHandler) {
        serverHandler.getClientController().updateModel(currentGameStatus);
    }
}
