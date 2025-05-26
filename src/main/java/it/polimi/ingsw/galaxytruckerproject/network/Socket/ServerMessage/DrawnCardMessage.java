package it.polimi.ingsw.galaxytruckerproject.network.Socket.ServerMessage;

import it.polimi.ingsw.galaxytruckerproject.client.ClientState;
import it.polimi.ingsw.galaxytruckerproject.model.cards.Card;
import it.polimi.ingsw.galaxytruckerproject.network.Socket.ServerHandler;

import java.rmi.RemoteException;

/**
 * Represents a message sent by the server to notify the client about a drawn card.
 * This message includes the drawn card that has been selected by the server.
 */
public class DrawnCardMessage extends ServerMessage {

    /**
     * The drawn card that has been selected by the server.
     */
    private Card card;

    /**
     * Constructs a new DrawnCardMessage with the specified drawn card.
     *
     * @param card The card that has been drawn by the server.
     */
    public DrawnCardMessage(Card card) {
        this.card = card;
    }

    /**
     * Processes the drawn card message by notifying the client about the drawn card,
     * updating the client's state to "WAIT", and displaying the drawn card to the client.
     *
     * @param serverHandler The server handler that processes the message.
     */
    @Override
    public void processMessage(ServerHandler serverHandler) {
        try {
            serverHandler.getView().notifyDrawnCard(card);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }

        // Update the client's state and set the drawn card as the displayed card.
        serverHandler.getClientController().setState(ClientState.WAIT);
        serverHandler.getClientController().setDisplayedCard(card);
    }
}
