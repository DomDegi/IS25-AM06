package it.polimi.ingsw.galaxytruckerproject.network.Socket.ClientMessage;

import it.polimi.ingsw.galaxytruckerproject.network.Socket.ClientHandler;

/**
 * Represents a message sent by the client to request leaving the current game.
 * This message triggers the server to handle the action of the client leaving the game.
 */
public class LeaveMessage extends ClientMessage {

    /**
     * Constructs a new LeaveMessage.
     * This constructor is empty as the message does not carry any data.
     */
    public LeaveMessage() {
        ;
    }

    /**
     * Processes the leave game message by invoking the appropriate method on the client
     * handler's controller to leave the current game.
     *
     * @param clientHandler The client handler that processes the message.
     */
    public void processMessage(ClientHandler clientHandler) {
        try {
            clientHandler.getController().leaveGame();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
