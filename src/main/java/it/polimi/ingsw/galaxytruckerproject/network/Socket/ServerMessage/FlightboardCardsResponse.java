package it.polimi.ingsw.galaxytruckerproject.network.Socket.ServerMessage;

import it.polimi.ingsw.galaxytruckerproject.network.Socket.ServerHandler;

/**
 * Represents a message sent by the server to respond with a flightboard card.
 * This message prompts the client to display the currently selected flightboard card.
 */
public class FlightboardCardsResponse extends ServerMessage {

    /**
     * Constructs a new FlightboardCardsResponse.
     * This constructor is empty as the message does not carry any data.
     */
    public FlightboardCardsResponse() {}

    /**
     * Processes the flightboard cards response message by invoking the appropriate method on the server's view
     * to display the currently selected flightboard card to the client.
     *
     * @param serverHandler The server handler that processes the message.
     */
    @Override
    public void processMessage(ServerHandler serverHandler) {
        try {
            serverHandler.getView().showCard(serverHandler.getClientController().getDisplayedCard());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
