package it.polimi.ingsw.galaxytruckerproject.network.Socket.ServerMessage;

import it.polimi.ingsw.galaxytruckerproject.network.Socket.ServerHandler;

import java.util.ArrayList;

/**
 * Represents a message sent by the server to notify the client that certain small card decks are unavailable.
 * This message includes a list of indices corresponding to the unavailable small card decks.
 */
public class NotAvailableCardDeckMessage extends ServerMessage {

    /**
     * A list of indices representing the small card decks that are unavailable.
     */
    private ArrayList<Integer> lockedSmallDecks;

    /**
     * Constructs a new NotAvailableCardDeckMessage with the specified list of unavailable small card decks.
     *
     * @param lockedSmallDecks A list of indices representing the unavailable small card decks.
     */
    public NotAvailableCardDeckMessage(ArrayList<Integer> lockedSmallDecks) {
        this.lockedSmallDecks = lockedSmallDecks;
    }

    /**
     * Processes the not available card decks message by invoking the appropriate method on the client controller
     * to update the client's view with the unavailable small card decks.
     *
     * @param serverHandler The server handler that processes the message.
     */
    @Override
    public void processMessage(ServerHandler serverHandler) {
        try {
            serverHandler.getClientController().decksNotAvailable(lockedSmallDecks);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
