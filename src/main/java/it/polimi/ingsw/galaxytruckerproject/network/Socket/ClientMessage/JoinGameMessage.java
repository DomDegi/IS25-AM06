package it.polimi.ingsw.galaxytruckerproject.network.Socket.ClientMessage;

import it.polimi.ingsw.galaxytruckerproject.network.Socket.ClientHandler;
import it.polimi.ingsw.galaxytruckerproject.network.Socket.Message;

/**
 * Represents a message sent by the client to request joining an existing game.
 * This message includes the name of the game the client wants to join.
 */
public class JoinGameMessage extends ClientMessage {

    /**
     * The name of the game the client wishes to join.
     */
    public String gameName;

    /**
     * Constructs a new JoinGameMessage with the specified game name.
     *
     * @param gameName The name of the game the client wants to join.
     */
    public JoinGameMessage(String gameName) {
        this.gameName = gameName;
    }

    /**
     * Processes the join game message by invoking the appropriate method on the client
     * handler's controller to join the specified game.
     *
     * @param clientHandler The client handler that processes the message.
     */
    @Override
    public void processMessage(ClientHandler clientHandler) {
        try {
            clientHandler.getController().joinGame(gameName);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
