package it.polimi.ingsw.galaxytruckerproject.network.Socket.ClientMessage;

import it.polimi.ingsw.galaxytruckerproject.network.Socket.ClientHandler;

/**
 * Represents a message sent by the client to request looking at a specific game card.
 * This message includes the index of the game card the client wishes to view.
 */
public class LookGameCardMessage extends ClientMessage {

    /**
     * The index of the game card the client wishes to look at.
     */
    private int index;

    /**
     * Constructs a new LookGameCardMessage with the specified game card index.
     *
     * @param index The index of the game card to be looked at.
     */
    public LookGameCardMessage(int index) {
        this.index = index;
    }

    /**
     * Processes the look game card message by invoking the appropriate method on the client
     * handler's controller to view the specified game card.
     *
     * @param clientHandler The client handler that processes the message.
     */
    public void processMessage(ClientHandler clientHandler) {
        try {
            clientHandler.getController().lookGameCards(index);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
