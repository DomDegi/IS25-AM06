package it.polimi.ingsw.galaxytruckerproject.network.Socket.ServerMessage;

import it.polimi.ingsw.galaxytruckerproject.model.cards.Card;
import it.polimi.ingsw.galaxytruckerproject.network.Socket.ServerHandler;

import java.util.ArrayList;
import java.util.Map;

/**
 * Represents a message sent by the server to update the client with the flight board cards.
 * This message includes the flight board cards organized by their corresponding indices.
 */
public class FlightBoardCardsMessage extends ServerMessage {

    /**
     * A map of indices and their corresponding list of flight board cards.
     * Each entry in the map represents a set of cards assigned to a particular index on the flight board.
     */
    private Map<Integer, ArrayList<Card>> flightBoardCards;

    /**
     * Constructs a new FlightBoardCardsMessage with the specified flight board cards.
     *
     * @param flightBoardCards A map of indices and their corresponding list of flight board cards.
     */
    public FlightBoardCardsMessage(Map<Integer, ArrayList<Card>> flightBoardCards) {
        this.flightBoardCards = flightBoardCards;
    }

    /**
     * Processes the flight board cards message by setting the deck of flight board cards in the client controller.
     * This method invokes the appropriate method to update the client's flight board cards.
     *
     * @param serverHandler The server handler that processes the message.
     */
    @Override
    public void processMessage(ServerHandler serverHandler) {
        try {
            serverHandler.getClientController().setDeck(flightBoardCards);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
