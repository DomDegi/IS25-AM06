package it.polimi.ingsw.galaxytruckerproject.network.Socket.ClientMessage;

import it.polimi.ingsw.galaxytruckerproject.network.Socket.ClientHandler;

/**
 * Represents a message sent by the client to request leaving the current game.
 * This message triggers the server to handle the action of the client leaving the game.
 */
public class LeaveGameMessage extends ClientMessage {

    /**
     * Constructs a new LeaveGameMessage.
     * This constructor is empty as the message does not carry any data.
     */
    public LeaveGameMessage() {
        ;
    }

    /**
     * Processes the leave game message by invoking the appropriate method on the client
     * handler's controller to leave the current game.
     *
     * @param clientHandler The client handler that processes the message.
     */
    @Override
    public void processMessage(ClientHandler clientHandler) {
        try {
            clientHandler.getController().leaveGame();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
