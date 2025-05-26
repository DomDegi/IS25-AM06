package it.polimi.ingsw.galaxytruckerproject.network.Socket.ClientMessage;

import it.polimi.ingsw.galaxytruckerproject.model.GameMode;
import it.polimi.ingsw.galaxytruckerproject.network.Socket.ClientHandler;

/**
 * Represents a message sent by the client to create a new game.
 * This message includes the game name, the number of players, and the game mode.
 */
public class CreateGameMessage extends ClientMessage {

    /**
     * The name of the game to be created.
     */
    private String gameName;

    /**
     * The number of players in the game.
     */
    private int playerCount;

    /**
     * The game mode chosen for the new game.
     */
    private GameMode mode;

    /**
     * Constructs a new CreateGameMessage with the specified game name, player count, and game mode.
     *
     * @param gameName The name of the game to be created.
     * @param playerCount The number of players in the game.
     * @param mode The game mode chosen for the new game.
     */
    public CreateGameMessage(String gameName, int playerCount, GameMode mode) {
        this.gameName = gameName;
        this.playerCount = playerCount;
        this.mode = mode;
    }

    /**
     * Processes the create game message by invoking the appropriate method on the
     * client handler's controller to create a new game with the specified parameters.
     *
     * @param clientHandler The client handler that processes the message.
     */
    @Override
    public void processMessage(ClientHandler clientHandler) {
        try {
            clientHandler.getController().createGame(gameName, playerCount, mode);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
